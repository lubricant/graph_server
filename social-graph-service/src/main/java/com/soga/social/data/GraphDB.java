package com.soga.social.data;

import java.io.Closeable;
import java.io.IOException;
import java.nio.file.Paths;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.graphdb.schema.Schema;

import com.google.common.collect.Streams;
import com.soga.social.config.Neo4jConfig;
import com.soga.social.data.model.GraphLabels;

public class GraphDB implements Closeable {

	private GraphDatabaseService dbInstance;
	
	public GraphDB(Neo4jConfig config) throws Exception {
		dbInstance = new GraphDatabaseFactory().
                newEmbeddedDatabase(Paths.get(config.getStoreDir()).toFile());
		
		Schema schema = dbInstance.schema();
		Streams.stream(schema.getIndexes(GraphLabels.PERSON)).map(i->i.getPropertyKeys());

	}

	public void close() throws IOException {
		if (dbInstance != null) {
			dbInstance.shutdown();
			dbInstance = null;
		}
	}
	
	public void createPerson(String id) {
		try (Transaction tx = dbInstance.beginTx()) {
			Node node = dbInstance.createNode(GraphLabels.PERSON);
			node.setProperty("id", id);
			
			tx.success();
		}
	}
	
	public void createConnection(String aid, String bid) {
		try (Transaction tx = dbInstance.beginTx()) {

			tx.success();
		}
	}
}
