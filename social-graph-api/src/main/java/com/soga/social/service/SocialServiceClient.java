package com.soga.social.service;

import java.io.Closeable;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.google.protobuf.Any;
import com.soga.social.service.SocialGraphServiceGrpc.SocialGraphServiceBlockingStub;

import io.grpc.ManagedChannel;
import io.grpc.netty.NettyChannelBuilder;

public class SocialServiceClient implements Closeable {
	
	public static SocialServiceClientBuilder newClient() {
		return new SocialServiceClientBuilder();
	}
	
	private final ManagedChannel channel;
	
	SocialServiceClient(SocialServiceClientBuilder config) {
		channel = NettyChannelBuilder.
				forAddress(config.host, config.port).
				usePlaintext(true).
				build();
	}
	
	public void close() throws IOException {
		if (! channel.isShutdown())
			channel.shutdown();
	}
	
	private SocialGraphServiceBlockingStub stub() {
		return SocialGraphServiceGrpc.newBlockingStub(channel);
	}
	
	public Result createPerson(String userId) {
		return stub().createPerson(PersonKey.newBuilder().setId(userId).build());
	}
	
	public PersonConn removePerson(String userId) {
		return stub().removePerson(PersonKey.newBuilder().setId(userId).build());
	}
	
	public Result connectPerson(String userA, String userB) {
		return stub().connectPerson(ConnectionKey.newBuilder().setSrc(userA).setDst(userB).build());
	}

	public ConnPerson disconnectPerson(String userA, String userB) {
		return stub().disconnectPerson(ConnectionKey.newBuilder().setSrc(userA).setDst(userB).build());
	}

	public Result updatePerson(String userId, Map<String, Object> propMap) {
		Map<String, Any> props = new HashMap<>();
		/*props.put("", Any.pack(""));*/
		return stub().updatePerson(Person.newBuilder().setId(userId).build());
	}
	
	public Result updateConnection(String userA, String userB) {
		return stub().updateConnection(Connection.newBuilder().setSrc(userA).setDst(userB).build());
	}
	
	public TraversalTree traverseGraphOnce(String root, int depth) {
		return stub().traverseGraph(TraversalDesc.newBuilder().setRoot(root).setDepth(depth).setOneshot(true).build());
	}
	
	public TraversalTree traverseGraph(String root, int depth, Long ticket) {
		return stub().traverseGraph(TraversalDesc.newBuilder().setRoot(root).setDepth(depth).setOneshot(false).
				setTicket(ticket == null ? -1: ticket).build());
	}

}
