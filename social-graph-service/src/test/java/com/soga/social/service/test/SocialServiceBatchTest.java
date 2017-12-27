package com.soga.social.service.test;

import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.soga.social.service.SocialGraphServer;
import com.soga.social.service.SocialServiceClient;
import com.soga.social.service.TraversalTree;

public class SocialServiceBatchTest {
	
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
	
	
	public void createPerson(int totalSize) {
		int reportBatch = 500;
		try {
			long start = System.currentTimeMillis(), beg = start;
			System.out.println("--------------------------------------------------------------");
			System.out.println("-------------------------  Insert Node  ----------------------");
			System.out.println("--------------------------------------------------------------");
			for (int i=1; i<=totalSize; i++) {
				client.createPerson(String.valueOf(i));
				if (i%reportBatch == 0) {
					long now = System.currentTimeMillis();
					long avg = (now-beg)/i;
					System.out.println(String.format("Inserted size: %d, cost time: %d s (%d ms)", 
							i, (now-beg)/1000, avg));
					beg = now;
				}
			}
			System.out.println("---------------------------------------------------------------");
			long totalMills = System.currentTimeMillis() - start;
			System.out.println(String.format("Total size: %d ", totalSize));
			System.out.println(String.format("Total cost: %d s", totalMills/1000));
			System.out.println("---------------------------------------------------------------");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void createConnection(int totalNodes, int connSize, boolean random) {
		int reportBatch = 100;
		try {
			long start = System.currentTimeMillis(), beg = start;
			System.out.println("--------------------------------------------------------------");
			System.out.println("-------------------------  Insert Conn  ----------------------");
			System.out.println("--------------------------------------------------------------");
			for (int i=1; i<=totalNodes; i++) {
				if (!random) {
					for (int j=1; j<=connSize; j++) {
						client.connectPerson(String.valueOf(i), String.valueOf(i+j));
					}
				} else {
					int[] rand = new int[connSize];
					Arrays.setAll(rand, x->ThreadLocalRandom.current().nextInt(1, totalNodes+1));
					for (int r: rand) {
						client.connectPerson(String.valueOf(i), String.valueOf(r));
					}
				}
				
				if (i%reportBatch == 0) {
					long now = System.currentTimeMillis();
					long avg = (now-beg)/i;
					System.out.println(String.format("Inserted size: %d, cost time: %d s (%d ms)", 
							i, (now-beg)/1000, avg));
					beg = now;
				}
			}
			System.out.println("---------------------------------------------------------------");
			long totalMills = System.currentTimeMillis() - start;
			System.out.println(String.format("Total size: %d ", totalNodes * connSize));
			System.out.println(String.format("Total cost: %d s", totalMills/1000));
			System.out.println("---------------------------------------------------------------");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void queryPerson(int totalNodes, int maxDepth) {
		try {
			System.out.println("-------------------------------------------------------------");
			System.out.println("-------------------------  Query Node  ----------------------");
			System.out.println("-------------------------------------------------------------");
			for (int i=1; i<=totalNodes; i++) {
				long start = System.currentTimeMillis();
				
				TraversalTree tree = client.traverseGraphOnce(String.valueOf(i), maxDepth, true);
				System.out.println(tree);
				System.out.println("-------------------------------------------------------------");
				
				long now = System.currentTimeMillis();
				long avg = (now-start)/i;
				System.out.println(String.format("Cost time: %d s (%d ms)", (now-start)/1000, avg));
				
			}
			System.out.println("-------------------------------------------------------------");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void iterPerson(int totalNodes, int maxDepth) {
		try {
			System.out.println("-------------------------------------------------------------");
			System.out.println("-------------------------  Query Node  ----------------------");
			System.out.println("-------------------------------------------------------------");
			for (int i=1; i<=totalNodes; i++) {
				long start = System.currentTimeMillis();
				
				TraversalTree tree = client.traverseGraphOnce(String.valueOf(i), maxDepth, true);
				System.out.println(tree);
				System.out.println("-------------------------------------------------------------");
				
				long now = System.currentTimeMillis();
				long avg = (now-start)/i;
				System.out.println(String.format("Cost time: %d s (%d ms)", (now-start)/1000, avg));
				
			}
			System.out.println("-------------------------------------------------------------");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testBatch() {
//		createPerson(10000);
//		createConnection(1000, 10, true);
		queryPerson(1, 3);
	}
	
}
