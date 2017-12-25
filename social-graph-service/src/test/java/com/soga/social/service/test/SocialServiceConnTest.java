package com.soga.social.service.test;

import java.io.IOException;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.soga.social.service.ConnPerson;
import com.soga.social.service.SocialGraphServer;
import com.soga.social.service.SocialServiceClient;
import com.soga.social.service.TraversalNode;
import com.soga.social.service.TraversalTree;
import com.soga.social.service.data.Properties;

public class SocialServiceConnTest {
	
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
	
	public void testCreateConn() {
		try {
			client.createPerson("A"); 
			client.createPerson("B");
			boolean result = client.connectPerson("A", "B");
			System.out.println(result);
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}
	
	public void testSelectConn() {
		try {
			TraversalTree result = client.traverseGraphOnce("A", 1, true);
			if (result == null) {
				System.out.println("NOT FOUND");
			} else {
				List<TraversalNode> list = result.getRoot().getAdjoinList();
				System.out.println(list);
			}
		} catch (Throwable t) {
			t.printStackTrace();
		}
		
	}
	
	public void testUpdateConn(boolean addProp) {
		Properties props = new Properties();
		props.setBool("x", true);
		if (addProp)
			props.setInt("y", 567);
		else
			props.remove("y");
		try {
			boolean result = client.updateConnection("B", "A", props);
			System.out.println(result);
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}
	
	public void testRemoveConn() {
		try {
			ConnPerson result = client.disconnectPerson("A", "B");
			Properties props = Properties.wrap(result.getConnection().getPropsMap());
			System.out.println(props.getAllProps());
			System.out.println("REMOVE");
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}
	
	@Test
	public void testPerson() {
		testCreateConn();
		testSelectConn();
		testUpdateConn(true);
		testSelectConn();
		testUpdateConn(false);
		testSelectConn();
		testRemoveConn();
		testSelectConn();
	}
	
}
