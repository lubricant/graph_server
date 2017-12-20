package com.soga.social.service;

import java.io.Closeable;
import java.io.IOException;

import com.soga.social.service.SocialGraphServiceGrpc.SocialGraphServiceBlockingStub;
import com.soga.social.service.data.Properties;

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

	public Result updatePerson(String userId, Properties userProps) {
		if (userProps == null || userProps.getProps().isEmpty()) {
			throw new IllegalArgumentException("Properties is empty.");
		}
		return stub().updatePerson(
				Person.newBuilder().setId(userId).putAllProps(userProps.getProps()).build());
	}
	
	public Result updateConnection(String userA, String userB, Properties connProps) {
		if (connProps == null || connProps.getProps().isEmpty()) {
			throw new IllegalArgumentException("Properties is empty.");
		}
		return stub().updateConnection(
				Connection.newBuilder().setSrc(userA).setDst(userB).putAllProps(connProps.getProps()).build());
	}
	
	public TraversalTree traverseGraphOnce(String root, int depth) {
		return stub().traverseGraph(TraversalDesc.newBuilder().setRoot(root).setDepth(depth).setOneshot(true).build());
	}
	
	public TraversalTree traverseGraph(String root, Long ticket) {
		return stub().traverseGraph(TraversalDesc.newBuilder().setRoot(root).setDepth(1).setOneshot(false).
				setTicket(ticket == null ? -1: ticket).build());
	}

}
