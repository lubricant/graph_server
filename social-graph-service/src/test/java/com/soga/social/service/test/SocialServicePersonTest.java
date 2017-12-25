package com.soga.social.service.test;

import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.soga.social.service.PersonConn;
import com.soga.social.service.SocialGraphServer;
import com.soga.social.service.SocialServiceClient;
import com.soga.social.service.TraversalTree;
import com.soga.social.service.data.Properties;

public class SocialServicePersonTest {
	
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
	
	public void testCreatePerson() {
		try {
			boolean result = client.createPerson("123");
			System.out.println(result);
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}
	
	public void testSelectPerson() {
		try {
			TraversalTree result = client.traverseGraphOnce("123", 0, true);
			if (result == null) {
				System.out.println("NOT FOUND");
			} else {
				Properties props = Properties.wrap(result.getRoot().getPerson().getPropsMap());
				System.out.println(props.getAllProps());
			}
		} catch (Throwable t) {
			t.printStackTrace();
		}
		
	}
	
	public void testUpdatePerson(boolean addProp) {
		Properties props = new Properties();
		props.setBool("os", true);
		if (addProp)
			props.setInt("len", 567);
		else
			props.remove("len");
		try {
			boolean result = client.updatePerson("123", props);
			System.out.println(result);
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}
	
	public void testRemovePerson() {
		try {
			PersonConn result = client.removePerson("123");
			Properties props = Properties.wrap(result.getPerson().getPropsMap());
			System.out.println(props.getAllProps());
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}
	
	@Test
	public void testPerson() {
		testCreatePerson();
		testSelectPerson();
		testUpdatePerson(true);
		testSelectPerson();
		testUpdatePerson(false);
		testSelectPerson();
		testRemovePerson();
		testSelectPerson();
	}
	
}
