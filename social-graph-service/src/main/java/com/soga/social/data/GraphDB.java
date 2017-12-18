package com.soga.social.data;

import java.io.Closeable;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.graphdb.schema.ConstraintType;
import org.neo4j.graphdb.schema.IndexCreator;
import org.neo4j.graphdb.schema.Schema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Streams;
import com.soga.social.config.ConfigLoader;
import com.soga.social.config.Neo4jConfig;
import com.soga.social.data.model.ConnEdge;
import com.soga.social.data.model.GraphLabels;
import com.soga.social.data.model.GraphRelations;
import com.soga.social.data.model.PersonNode;

public class GraphDB implements Closeable {

	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	private GraphDatabaseService dbInstance;
	
	public GraphDB(Neo4jConfig config) throws Exception {
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
				ConnEdge conn = ConnEdge.of(nodePid, otherPid, r.getAllProperties());
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
			
			Map<String, Object> props = person.getProps();
			node.removeProperty("");
			node.setProperty("", null);
			node.hasProperty("");
			
			final long nodeId = node.getId();
			final Object nodePid = node.getProperty("pid");
			
			Streams.stream(node.getRelationships(GraphRelations.CONNECTION)).map( r -> {
				Object otherPid = (r.getStartNodeId() == nodeId ? r.getEndNode(): r.getStartNode()).getProperty("pid");
				ConnEdge conn = ConnEdge.of(nodePid, otherPid, r.getAllProperties());
				r.delete();
				return conn;
			});
			
			person.setProps(node.getAllProperties());
			node.delete();
			
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
			
			if (relation != null) {
				conn.setProps(relation.getAllProperties());
			}
			
			tx.success();
		}
	}
	
	public void traverse() {
		
	}
	
	public static void main(String[] args) throws Exception {
		new GraphDB(ConfigLoader.getNeo4jConfig()).close();
	}
}
