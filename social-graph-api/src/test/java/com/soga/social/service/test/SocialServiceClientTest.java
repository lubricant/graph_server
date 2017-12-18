package com.soga.social.service.test;

import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.soga.social.service.SocialServiceClient;

public class SocialServiceClientTest {
	
	SocialServiceClient client;
	
	@Before
	public void setUp() {
		client = SocialServiceClient.newClient().bind("localhost", 3366).build();
	}
	
	@After
	public void cleanUp() throws IOException {
		client.close();
	}
	
	@Test
	public void testCreatePerson() {
		client.createPerson("123");
	}
	
	@Test
	public void testUpdatePerson() {
		client.updatePerson("123", null);
	}
	
}
