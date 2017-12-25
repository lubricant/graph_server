package com.soga.social.service.test;

import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.soga.social.service.Result;
import com.soga.social.service.SocialGraphServer;
import com.soga.social.service.SocialServiceClient;
import com.soga.social.service.data.Properties;

public class SocialServiceTest {
	
	SocialServiceClient client;
	
	@Before
	public void setUp() {
		new Thread(()->SocialGraphServer.main(null)).start();
		client = SocialServiceClient.newClient().bind("localhost", 3366).build();
	}
	
	@After
	public void cleanUp() throws IOException {
		client.close();
	}
	
	@Test
	public void testCreatePerson() {
		try {
			boolean result = client.createPerson("123");
			System.out.println(result);
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}
	
//	@Test
	public void testUpdatePerson() {
		Properties props = new Properties();
		try {
			boolean result = client.updatePerson("123", props);
			System.out.println(result);
		} catch (Throwable t) {
			t.printStackTrace();
		}
		
	}
	
}
