package com.soga.social.service;

import com.soga.social.service.SocialGraphServiceGrpc.SocialGraphServiceBlockingStub;

import io.grpc.ManagedChannel;
import io.grpc.netty.NettyChannelBuilder;

public class SocialServiceClient {
	
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

	public Result updatePerson(String userId) {
		return stub().updatePerson(Person.newBuilder().setId(userId).build());
	}
	
	public Result updateConnection(String userA, String userB) {
		return stub().updateConnection(Connection.newBuilder().setSrc(userA).setDst(userB).build());
	}
	
	public TraversalTree traverseGraph(String root, int depth, long token) {
		return stub().traverseGraph(TraversalDesc.newBuilder().setRoot(root).setDepth(depth).setToken(token).build());
	}
	
}
