package com.soga.social.service.test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.soga.social.service.SocialGraphServer;
import com.soga.social.service.SocialServiceClient;
import com.soga.social.service.TraversalNode;
import com.soga.social.service.TraversalTree;
import com.soga.social.service.data.Properties;

public class SocialServiceTraverTest {
	
	SocialServiceClient client;
	
	@Before
	public void setUp() {
		SocialGraphServer.start(false);
		client = SocialServiceClient.newClient().bind("localhost", 3366).build();
	}
	
	@After
	public void cleanUp() throws IOException {
		client.close();
		SocialGraphServer.shutdown(true);
	}
	
	public void prepareData() throws Exception {
		
		int i = 0;
		for (char c: new char[]{'A', 'B', 'C', 'X'}) {
			String name = "李小" + c;
			client.createPerson(name);
			client.updatePerson(name, 
					new Properties().
					setInt("age", 25+(i++)).
					setString("company", "合作社-"+c).
					setString("hometown", "李家庄"));
		}
		
		i = 0;
		for (char c: new char[]{'A', 'B', 'C', 'Y'}) {
			String name = "王老" + c;
			client.createPerson(name);
			client.updatePerson(name, 
					new Properties().
					setInt("age", 25+(i++)).
					setString("company", "合作社-"+c).
					setString("hometown", "王屋村"));
		} 
		
		i = 0;
		for (char c: new char[]{'X', 'Y', 'Z'}) {
			String name = "牛" + c;
			client.createPerson(name);
			client.updatePerson(name, 
					new Properties().
					setInt("age", 25+(i++)).
					setString("company", "合作社-"+c).
					setString("hometown", "牛栏山"));
		} 
		
		client.connectPerson("李小A", "李小B");
		client.connectPerson("李小A", "李小C");
		client.connectPerson("李小A", "李小X");
		client.connectPerson("李小B", "李小C");
		client.connectPerson("李小B", "李小X");
		client.connectPerson("李小C", "李小X");
		client.updateConnection("李小A", "李小B", new Properties().setString("type", "townee"));
		client.updateConnection("李小A", "李小C", new Properties().setString("type", "townee"));
		client.updateConnection("李小A", "李小X", new Properties().setString("type", "townee"));
		client.updateConnection("李小B", "李小C", new Properties().setString("type", "townee"));
		client.updateConnection("李小B", "李小X", new Properties().setString("type", "townee"));
		client.updateConnection("李小C", "李小X", new Properties().setString("type", "townee"));
		
		client.connectPerson("王老A", "王老B");
		client.connectPerson("王老A", "王老C");
		client.connectPerson("王老A", "王老Y");
		client.connectPerson("王老B", "王老C");
		client.connectPerson("王老B", "王老Y");
		client.connectPerson("王老C", "王老Y");
		client.updateConnection("王老A", "王老B", new Properties().setString("type", "townee"));
		client.updateConnection("王老A", "王老C", new Properties().setString("type", "townee"));
		client.updateConnection("王老A", "王老Y", new Properties().setString("type", "townee"));
		client.updateConnection("王老B", "王老C", new Properties().setString("type", "townee"));
		client.updateConnection("王老B", "王老Y", new Properties().setString("type", "townee"));
		client.updateConnection("王老C", "王老Y", new Properties().setString("type", "townee"));
		
		client.connectPerson("牛X", "牛Y");
		client.connectPerson("牛X", "牛Z");
		client.connectPerson("牛Y", "牛Z");
		client.updateConnection("牛X", "牛Y", new Properties().setString("type", "townee"));
		client.updateConnection("牛X", "牛Z", new Properties().setString("type", "townee"));
		client.updateConnection("牛Y", "牛Z", new Properties().setString("type", "townee"));
		
		client.connectPerson("李小A", "王老A");
		client.connectPerson("李小B", "王老B");
		client.connectPerson("李小C", "王老C");
		client.connectPerson("李小X", "牛X");
		client.connectPerson("王老Y", "牛Y");
		client.updateConnection("李小A", "王老A", new Properties().setString("type", "workmate"));
		client.updateConnection("李小B", "王老B", new Properties().setString("type", "workmate"));
		client.updateConnection("李小C", "王老C", new Properties().setString("type", "workmate"));
		client.updateConnection("李小X", "牛X", new Properties().setString("type", "workmate"));
		client.updateConnection("王老Y", "牛Y", new Properties().setString("type", "workmate"));
	}

	private void traverseNode(TraversalNode node, long ticket, int depth) throws Exception {
		List<String> id = new ArrayList<>();
		for (TraversalNode adj: node.getAdjoinList()) {
			id.add(adj.getConnection().getDst());
		}
		System.out.println(String.format("depth: %d, data: %s", depth, id));
		for (TraversalNode adj: node.getAdjoinList()) {
			TraversalTree tree = client.traverseGraph(adj.getConnection().getDst(), ticket, true);
			TraversalNode root = tree.getRoot();
			traverseNode(root, ticket, depth + 1);
		}
	}
	
	public void testTraversal() throws Exception {
		TraversalTree tree = client.traverseGraph("李小A", null, true);
		long ticket = tree.getTicket();
		TraversalNode root = tree.getRoot();
		System.out.println("TICKET: " + ticket);
		System.out.println("ROOT: " + root);
		System.out.println("-----------------------------------------------------------");
		traverseNode(root, ticket, 1);
	}
	
	@Test
	public void testPerson() {
		try {
			prepareData();
			testTraversal();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
