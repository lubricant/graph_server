package com.soga.social.service.test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.ImmutableList;
import com.soga.social.service.SocialGraphServer;
import com.soga.social.service.SocialServiceClient;
import com.soga.social.service.TraversalNode;
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
		int reportBatch = 1000;
		try {
			long start = System.currentTimeMillis(), beg = start;
			System.out.println("--------------------------------------------------------------");
			System.out.println("-------------------------  Insert Node  ----------------------");
			System.out.println("--------------------------------------------------------------");
			for (int i=1; i<=totalSize; i++) {
				client.createPerson(String.valueOf(i));
				if (i%reportBatch == 0) {
					long now = System.currentTimeMillis();
					long avg = (now-beg)/reportBatch;
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
		int reportBatch = 1000;
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
					long avg = (now-beg)/reportBatch;
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
	
	private List<String> reduce(List<String> list, TraversalNode node){
		list.add(node.getPerson().getId());
		node.getAdjoinList().stream().reduce(list, this::reduce, (a,b)->{a.addAll(b); return a;});
		return list;
	}
	
	public void queryPerson(String personId, int maxDepth) {
		try {
			System.out.println("-------------------------------------------------------------");
			System.out.println("-------------------------  Query Node  ----------------------");
			System.out.println("-------------------------------------------------------------");
			
			long start = System.currentTimeMillis();
			TraversalTree tree = client.traverseGraphOnce(personId, maxDepth, true);
//			System.out.println(tree);
			
			List<String> nodeIds = tree.getRoot().getAdjoinList().stream().reduce(
					new ArrayList<>(), this::reduce, (a,b)->{a.addAll(b); return a;});
			
			nodeIds = new ArrayList<>(new HashSet<>(nodeIds));
			Collections.sort(nodeIds);
			System.out.println(tree.getRoot().getPerson().getId() + " - " + nodeIds.size());
			System.out.println(nodeIds);
			
			System.out.println("=============================================================");
			long now = System.currentTimeMillis();
			System.out.println(String.format("Cost time: %d s (%d ms)", (now-start)/1000, (now-start)));
			System.out.println("-------------------------------------------------------------");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public Long iterPerson(String personId, int maxDepth, Long ticket) {
		try {
			System.out.println("-------------------------------------------------------------");
			System.out.println("-------------------------  Query Node  ----------------------");
			System.out.println("-------------------------------------------------------------");
			
			long start = System.currentTimeMillis();
			
			Set<String> nodeSet = new HashSet<>();
			List<List<String>> queues = ImmutableList.of(
					new ArrayList<>(), 
					new ArrayList<>()
			);
			
			int totalSize = 0;
			int mainQueue = 0;
			queues.get(mainQueue).add(personId);
			for (int i=0; i<maxDepth; i++, mainQueue++) {
				List<String> mainQue = queues.get(mainQueue % 2);
				List<String> subQue = queues.get((mainQueue + 1) % 2);
				
				if (mainQue.isEmpty()) 
					break;
				else subQue.clear();
				
				for (String pid: mainQue) {
					TraversalTree tree = client.traverseGraph(pid, ticket, true);
					ticket = tree.getTicket();
					
//					System.out.println(tree);
					List<String> nodeIds = tree.getRoot().getAdjoinList().stream().reduce(
							new ArrayList<>(), this::reduce, (a,b)->{a.addAll(b); return a;});
					
					nodeIds = new ArrayList<>(new HashSet<>(nodeIds));
					Collections.sort(nodeIds);
					System.out.println(tree.getRoot().getPerson().getId() + " - " + nodeIds.size());
					System.out.println(nodeIds);
					
					TraversalNode node = tree.getRoot();
					List<TraversalNode> adjNodes = node.getAdjoinList();
					for (TraversalNode adj: adjNodes) {
						subQue.add(adj.getConnection().getDst());
					}
					
					nodeSet.addAll(subQue);
					totalSize += tree.getSize();
				}
				
			}
			
			
			List<String> nodeIds =  new ArrayList<>(nodeSet);
			Collections.sort(nodeIds);
			System.out.println("=============================================================");
			System.out.println(nodeIds.size());
			System.out.println(nodeIds);
			System.out.println("=============================================================");
			long now = System.currentTimeMillis();
			System.out.println(String.format("Cost time: %d s (%d ms)", (now-start)/1000, (now-start)));
			System.out.println(String.format("Total size: %d", totalSize));
			
			System.out.println("-------------------------------------------------------------");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return ticket;
	}
	
	@Test
	public void testBatch() throws InterruptedException {
//		createPerson(20);
//		createConnection(20, 3, true);
//		queryPerson("1", 2);
		
		Long ticket = null;
		while (true) {
			ticket = iterPerson("1", 1, ticket);
			Thread.sleep(TimeUnit.SECONDS.toMillis(2));
		}
		
	}
	
}
