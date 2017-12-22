package com.soga.social.data;

import java.io.Closeable;
import java.io.IOException;
import java.nio.file.Paths;
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
import org.neo4j.graphdb.traversal.TraversalBranch;
import org.neo4j.graphdb.traversal.TraversalDescription;
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
	
	public void createPerson(PersonNode person) {
		try (Transaction tx = dbInstance.beginTx()) {
			
			if (dbInstance.findNode(GraphLabels.PERSON, "pid", person.getPid()) != null)
				return;
			
			Node node = dbInstance.createNode(GraphLabels.PERSON);
			node.setProperty("pid", person.getPid());
			tx.success();
		}
	}
	
	public void createConnection(ConnEdge conn) {
		try (Transaction tx = dbInstance.beginTx()) {
			Node srcNode = dbInstance.findNode(GraphLabels.PERSON, "pid", conn.getSrc());
			Node dstNode = dbInstance.findNode(GraphLabels.PERSON, "pid", conn.getDst());
			
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
		}
	}
	
	public void removePerson(PersonNode person) {
		try (Transaction tx = dbInstance.beginTx()) {
			Node node = dbInstance.findNode(GraphLabels.PERSON, "pid", person.getPid());
			if (node == null) 
				return;
			
			final long nodeId = node.getId();
			final Object nodePid = node.getProperty("pid");
			
			Streams.stream(node.getRelationships(GraphRelations.CONNECTION)).map( r -> {
				Object otherPid = (r.getStartNodeId() == nodeId ? r.getEndNode(): r.getStartNode()).getProperty("pid");
				ConnEdge conn = ConnEdge.of(nodePid, otherPid, r);
				r.delete();
				return conn;
			});
			
			person.setProps(node.getAllProperties());
			node.delete();
			
			tx.success();
		}
	}
	
	public void removeConnection(ConnEdge conn) {
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
				conn.setProps(relation.getAllProperties());
			}
			
			tx.success();
		}
	}
	
	public void updatePerson(PersonNode person) {
		try (Transaction tx = dbInstance.beginTx()) {
			Node node = dbInstance.findNode(GraphLabels.PERSON, "pid", person.getPid());
			if (node == null) 
				return;
			
			for (Entry<String,Object> prop: person.getProps().entrySet()) {
				if (prop.getValue() != null)
					node.setProperty(prop.getKey(), prop.getValue());
				else
					node.removeProperty(prop.getKey());
			}
			
			tx.success();
		}
	}
	
	public void updateConnection(ConnEdge conn) {
		try (Transaction tx = dbInstance.beginTx()) {
			
			Node srcNode = dbInstance.findNode(GraphLabels.PERSON, "pid", conn.getSrc());
			Node dstNode = dbInstance.findNode(GraphLabels.PERSON, "pid", conn.getDst());
			
			final long srcId = srcNode.getId(), dstId = dstNode.getId();
			Predicate<Relationship> equals = r -> 
			(r.getStartNodeId() == srcId && r.getEndNodeId() == dstId) ||
			(r.getStartNodeId() == dstId && r.getEndNodeId() == srcId);
			
			Relationship relation = 
					Streams.stream(srcNode.getRelationships(GraphRelations.CONNECTION)).filter(equals).findFirst().orElse(null);
			
			for (Entry<String,Object> prop: conn.getProps().entrySet()) {
				if (prop.getValue() != null)
					relation.setProperty(prop.getKey(), prop.getValue());
				else
					relation.removeProperty(prop.getKey());
			}
			
			tx.success();
		}
	}
	
	public TraverPath traverse(String personId, final int depth, final Session session) {
		
		if (depth < 1 || depth > 6) {
			throw new IllegalArgumentException("Depth should be between 1 and 6.");
		}
		
		UniquenessFactory sessUniq = new UniquenessFactory() {
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
		
		Evaluator sessEval = new Evaluator() {
			public Evaluation evaluate(Path path) {
				
				long nodeId = path.endNode().getId();
				boolean notVisited = session.notVisited(nodeId);
				
				if (notVisited) session.visit(nodeId);
				
				return Evaluation.of(notVisited, 
						(path.length() - 1) >= depth);
			}
		};
		
		
		try (Transaction tx = dbInstance.beginTx()) {
			Node root = dbInstance.findNode(GraphLabels.PERSON, "pid", personId);
			if (root == null) {
				throw new IllegalArgumentException("Person with %s is not existed.");
			}
			
			TraversalDescription traversal = dbInstance.traversalDescription().
				relationships(GraphRelations.CONNECTION).
				evaluator(sessEval).
				uniqueness(sessUniq).
				depthFirst();
			
			TraverPath origin = TraverPath.of(root.getId(), PersonNode.of(root));
			for (Path path: traversal.traverse(root)) {
				
				TraverPath parent = null;
				Relationship relation = null;
				
				for (PropertyContainer pc: path) {
					if (parent == null) {
						parent = origin;
					} else {
						if (relation == null) {
							relation = (Relationship) pc;
							
						} else {
							Node node = (Node) pc;
							TraverPath next = parent.getBranches().get(node.getId());
							
							if (next == null) {
								parent.getBranches().put(node.getId(), next = 
										TraverPath.of(node.getId(), PersonNode.of(node)));
								next.getEdges().add(
										ConnEdge.of(parent.getNode().getPid(), next.getNode().getPid(), relation));
 							}
							
							relation = null;
						}
					}
				}
			}
			
			tx.success();
			return origin;
		}
	}
	
}
