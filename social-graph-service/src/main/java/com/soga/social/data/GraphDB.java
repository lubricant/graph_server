package com.soga.social.data;

import java.io.Closeable;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Path;
import org.neo4j.graphdb.PropertyContainer;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.graphdb.schema.ConstraintType;
import org.neo4j.graphdb.schema.IndexCreator;
import org.neo4j.graphdb.schema.Schema;
import org.neo4j.graphdb.traversal.Evaluation;
import org.neo4j.graphdb.traversal.Evaluator;
import org.neo4j.graphdb.traversal.Evaluators;
import org.neo4j.graphdb.traversal.TraversalBranch;
import org.neo4j.graphdb.traversal.TraversalDescription;
import org.neo4j.graphdb.traversal.Uniqueness;
import org.neo4j.graphdb.traversal.UniquenessFactory;
import org.neo4j.graphdb.traversal.UniquenessFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Streams;
import com.soga.social.config.ConfigLoader;
import com.soga.social.config.GraphConfig;
import com.soga.social.data.SessionDB.Session;
import com.soga.social.data.model.ConnEdge;
import com.soga.social.data.model.GraphLabels;
import com.soga.social.data.model.GraphRelations;
import com.soga.social.data.model.PersonNode;
import com.soga.social.data.model.TraverPath;

public class GraphDB implements Closeable {

	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	private GraphDatabaseService dbInstance;
	
	public GraphDB() throws Exception {
		
		GraphConfig config = ConfigLoader.getGraphConfig();
		
		dbInstance = new GraphDatabaseFactory().
                newEmbeddedDatabase(Paths.get(config.getStoreDir()).toFile());
		
		try (Transaction tx = dbInstance.beginTx()) {
			Schema schema = dbInstance.schema();
			
			for (GraphLabels label: GraphLabels.values()) {
				
				Set<String> uniqueKeys = 
				Streams.stream(schema.getConstraints(label)).
				filter(c->c.getConstraintType() == ConstraintType.UNIQUENESS).
				map(c->Streams.stream(c.getPropertyKeys()).sorted().reduce(StringUtils::join).get()).
				collect(Collectors.toSet());
				
				Set<String> indexKeys = 
				Streams.stream(schema.getIndexes(label)).
				map(i->Streams.stream(i.getPropertyKeys()).sorted().reduce(StringUtils::join).get()).
				collect(Collectors.toSet());
				
				String[] uniqueProp = label.uniqueness(); 
				if (ArrayUtils.isNotEmpty(uniqueProp)) {
					for (String prop: uniqueProp) {
						if (! uniqueKeys.contains(prop)) {
							schema.constraintFor(label).assertPropertyIsUnique(prop).create();
							uniqueKeys.add(prop);
						}
					}
				}
				
				String[][] indexProps = label.indices();
				if (ArrayUtils.isNotEmpty(indexProps)) {
					for (String[] props: indexProps) {
						String propKey = Stream.of(props).sorted().reduce(StringUtils::join).get();
						if (! indexKeys.contains(propKey)) {
							IndexCreator creator = schema.indexFor(label);
							for (String prop: props) 
								creator.on(prop);
							creator.create();
						}
					}
				}
			}

			logger.info("Initialize Neo4j schema successfully.");
			logger.info("Neo4j constrains: {}", schema.getConstraints());
			logger.info("Neo4j indexes: {}", schema.getIndexes());
			
			tx.success();
		}
		
	}

	public void close() throws IOException {
		if (dbInstance != null) {
			dbInstance.shutdown();
			dbInstance = null;
		}
	}
	
	public int createPerson(PersonNode person) {
		try (Transaction tx = dbInstance.beginTx()) {
			
			if (dbInstance.findNode(GraphLabels.PERSON, "pid", person.getPid()) != null)
				return 0;
			
			Node node = dbInstance.createNode(GraphLabels.PERSON);
			node.setProperty("pid", person.getPid());
			tx.success();
			return 1;
		}
	}
	
	public int createConnection(ConnEdge conn) {
		try (Transaction tx = dbInstance.beginTx()) {
			Node srcNode = dbInstance.findNode(GraphLabels.PERSON, "pid", conn.getSrc());
			Node dstNode = dbInstance.findNode(GraphLabels.PERSON, "pid", conn.getDst());
			
			if (srcNode == null || dstNode == null)
				return -1;
			
			final long srcId = srcNode.getId(), dstId = dstNode.getId();
			Predicate<Relationship> equals = r -> 
			(r.getStartNodeId() == srcId && r.getEndNodeId() == dstId) ||
			(r.getStartNodeId() == dstId && r.getEndNodeId() == srcId);
			
			boolean exist = 
			Streams.stream(srcNode.getRelationships(GraphRelations.CONNECTION)).filter(equals).findFirst().isPresent() ||
			Streams.stream(dstNode.getRelationships(GraphRelations.CONNECTION)).filter(equals).findFirst().isPresent() ;
			
			if (!exist) {
				srcNode.createRelationshipTo(dstNode, GraphRelations.CONNECTION);
			}
			
			tx.success();
			return exist ? 0 : 1;
		}
	}
	
	public List<ConnEdge> removePerson(PersonNode person) {
		try (Transaction tx = dbInstance.beginTx()) {
			Node node = dbInstance.findNode(GraphLabels.PERSON, "pid", person.getPid());
			if (node == null) 
				return null;
			
			Map<String,Object> props = node.getAllProperties();
			
			final long nodeId = node.getId();
			final Object nodePid = props.remove("pid");
			
			List<ConnEdge> result = new ArrayList<>();
			for (Relationship relation: node.getRelationships(GraphRelations.CONNECTION)) {
				Object otherPid = (relation.getStartNodeId() == nodeId ? relation.getEndNode(): relation.getStartNode()).getProperty("pid");
				result.add(ConnEdge.of(nodePid, otherPid, relation));
				relation.delete();
			}
			
			person.setProps(props);
			node.delete();
			tx.success();
			return result;
		}
	}
	
	public List<PersonNode> removeConnection(ConnEdge conn) {
		try (Transaction tx = dbInstance.beginTx()) {
			
			Node srcNode = dbInstance.findNode(GraphLabels.PERSON, "pid", conn.getSrc());
			Node dstNode = dbInstance.findNode(GraphLabels.PERSON, "pid", conn.getDst());
			
			final long srcId = srcNode.getId(), dstId = dstNode.getId();
			Predicate<Relationship> equals = r -> 
			(r.getStartNodeId() == srcId && r.getEndNodeId() == dstId) ||
			(r.getStartNodeId() == dstId && r.getEndNodeId() == srcId);
			
			Relationship relation = 
					Streams.stream(srcNode.getRelationships(GraphRelations.CONNECTION)).filter(equals).findFirst().orElse(null);
			
			if (relation == null) {
				return null;
			}
			
			List<PersonNode> result = new ArrayList<>();
			result.add(PersonNode.of(srcNode));
			result.add(PersonNode.of(dstNode));
			
			conn.setProps(relation.getAllProperties());
			relation.delete();
			
			tx.success();
			return result;
		}
	}
	
	public boolean updatePerson(PersonNode person) {
		try (Transaction tx = dbInstance.beginTx()) {
			Node node = dbInstance.findNode(GraphLabels.PERSON, "pid", person.getPid());
			if (node == null) 
				return false;
			
			for (Entry<String,Object> prop: person.getProps().entrySet()) {
				if (prop.getValue() != null)
					node.setProperty(prop.getKey(), prop.getValue());
				else
					node.removeProperty(prop.getKey());
			}
			
			tx.success();
			return true;
		}
	}
	
	public boolean updateConnection(ConnEdge conn) {
		try (Transaction tx = dbInstance.beginTx()) {
			
			Node srcNode = dbInstance.findNode(GraphLabels.PERSON, "pid", conn.getSrc());
			Node dstNode = dbInstance.findNode(GraphLabels.PERSON, "pid", conn.getDst());
			
			final long srcId = srcNode.getId(), dstId = dstNode.getId();
			Predicate<Relationship> equals = r -> 
			(r.getStartNodeId() == srcId && r.getEndNodeId() == dstId) ||
			(r.getStartNodeId() == dstId && r.getEndNodeId() == srcId);
			
			Relationship relation = 
					Streams.stream(srcNode.getRelationships(GraphRelations.CONNECTION)).filter(equals).findFirst().orElse(null);
			
			if (relation != null) {
				for (Entry<String,Object> prop: conn.getProps().entrySet()) {
					if (prop.getValue() != null)
						relation.setProperty(prop.getKey(), prop.getValue());
					else
						relation.removeProperty(prop.getKey());
				}
			}
			
			tx.success();
			return relation != null;
		}
	}
	
	public TraverPath traverse(String personId, boolean connected, int depth) {
		
		if (depth < 0 || depth > 6) {
			throw new IllegalArgumentException("Depth should be between 0 and 6.");
		}
		
		return traverse(personId, connected, depth, null, Uniqueness.NODE_PATH, Evaluators.atDepth(depth));
	}
	
	public TraverPath traverse(String personId, boolean connected, final int depth, final Session session) {
		
		if (depth < 0 || depth > 6) {
			throw new IllegalArgumentException("Depth should be between 0 and 6.");
		}
		
		UniquenessFactory uniqueness = new UniquenessFactory() {
			public boolean eagerStartBranches() {
				return true;
			}
			public UniquenessFilter create(Object optParam) {
				return new UniquenessFilter() {
					public boolean checkFirst(TraversalBranch branch) {
						return session.notVisited(branch.startNode().getId());
					}
					public boolean check(TraversalBranch branch) {
						return session.notVisited(branch.endNode().getId());
					}
				};
			}
		};
		
		Evaluator evaluator = new Evaluator() {
			public Evaluation evaluate(Path path) {
				if (path.length() > depth) {
					return Evaluation.EXCLUDE_AND_PRUNE;
				}
				
				long nodeId = path.endNode().getId();
				boolean notVisited = session.notVisited(nodeId);
				
				if (notVisited) session.visit(nodeId);
				return Evaluation.of(notVisited, path.length() <= depth);
			}
		};
		
		return traverse(personId, connected, depth, session, uniqueness, evaluator);
	}

	private TraverPath traverse(String pid, boolean connected, int depth, Session session, 
			UniquenessFactory uniqueness, Evaluator evaluator) {
		
		try (Transaction tx = dbInstance.beginTx()) {
			Node root = dbInstance.findNode(GraphLabels.PERSON, "pid", pid);
			if (root == null) {
				return null;
			}
			
			TraverPath origin = TraverPath.of(root.getId(), PersonNode.of(root));
			if (depth == 0) {
				return origin;
			}
			
			TraversalDescription traversal = dbInstance.traversalDescription().
				relationships(GraphRelations.CONNECTION).
				evaluator(evaluator).
				uniqueness(uniqueness).
				depthFirst();
			
			int size = 0;
			for (Path path: traversal.traverse(root)) {
				
				TraverPath parent = null;
				Relationship relation = null;
				
				for (PropertyContainer entity: path) {
					if (parent == null) {
						if (((Node)entity).getId() != root.getId())
							throw new IllegalStateException("Path is not start with root.");
						parent = origin;
						continue;
					} 
					
					if (relation == null) {
						relation = (Relationship) entity;
						continue;
					}
					
					Node node = (Node) entity;
					TraverPath next = parent.getBranches().get(node.getId());
					
					if (next == null) {
						parent.getBranches().put(node.getId(), next = 
							TraverPath.of(node.getId(), PersonNode.of(node)));
						
						if (connected) {
							next.setEdge(ConnEdge.of(parent.getNode().getPid(), next.getNode().getPid(), relation));
						}
						
						size++;
					}
					
					parent = next;
					relation = null;
				}
			}
			
			tx.success();
			origin.setSize(size);
			return origin;
		}
	}
	
//	private void fetchConnection(Node node, List<ConnEdge> edges, Session sess) {
//		for (Relationship relation: node.getRelationships(GraphRelations.CONNECTION)) {
//			Node other = null;
//			if (node.getId() != relation.getStartNodeId()) {
//				other = relation.getStartNode();
//			} 
//			if (node.getId() != relation.getEndNodeId()) {
//				other = relation.getEndNode();
//			}
//			if (sess == null || sess.notVisited(other.getId())) {
//				ConnEdge.of(node.getProperty("pid"), other.getProperty("pid"), relation);
//			}
//		}
//	}
}
