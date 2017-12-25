package com.soga.social.service;

import java.io.Closeable;
import java.io.IOException;

import com.google.protobuf.Any;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Message;
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

	private void check(Result res) throws Exception {
		if (res.getState() == Result.Status.FAILURE) {
			throw new Exception(res.getHint());
		}
	}
	
	private <T extends Message> T unpack(Result res, Class<T> type) throws InvalidProtocolBufferException {
		Any data = res.getData();
		if (data.getTypeUrl().isEmpty())
			return null;
		return data.unpack(type);
	}
	
	public boolean createPerson(String userId) throws Exception {
		Result result = stub().createPerson(PersonKey.newBuilder().setId(userId).build());
		check(result);
		return result.getState() == Result.Status.SUCCESS;
	}
	
	public PersonConn removePerson(String userId) throws Exception {
		Result result = stub().removePerson(PersonKey.newBuilder().setId(userId).build());
		check(result);
		return unpack(result, PersonConn.class);
	}
	
	public boolean connectPerson(String userA, String userB) throws Exception {
		Result result = stub().connectPerson(ConnectionKey.newBuilder().setSrc(userA).setDst(userB).build());
		check(result);
		return result.getState() == Result.Status.SUCCESS;
	}

	public ConnPerson disconnectPerson(String userA, String userB) throws Exception {
		Result result = stub().disconnectPerson(ConnectionKey.newBuilder().setSrc(userA).setDst(userB).build());
		check(result);
		return unpack(result, ConnPerson.class);
	}

	public boolean updatePerson(String userId, Properties userProps) throws Exception {
		if (userProps == null || userProps.getProps().isEmpty()) {
			throw new IllegalArgumentException("Properties is empty.");
		}
		Result result = stub().updatePerson(
		Person.newBuilder().setId(userId).putAllProps(userProps.getProps()).build());
		check(result);
		return result.getState() == Result.Status.SUCCESS;
	}
	
	public boolean updateConnection(String userA, String userB, Properties connProps) throws Exception {
		if (connProps == null || connProps.getProps().isEmpty()) {
			throw new IllegalArgumentException("Properties is empty.");
		}
		Result result = stub().updateConnection(
		Connection.newBuilder().setSrc(userA).setDst(userB).putAllProps(connProps.getProps()).build());
		check(result);
		return result.getState() == Result.Status.SUCCESS;
	}
	
	public TraversalTree traverseGraphOnce(String root, int depth, boolean connected) throws Exception {
		Result result = stub().traverseGraph(TraversalDesc.newBuilder().
				setRoot(root).
				setDepth(depth).
				setConnected(connected).
				setOneshot(true).build());
		check(result);
		return unpack(result, TraversalTree.class);
	}
	
	public TraversalTree traverseGraph(String root, Long ticket, boolean connected) throws Exception {
		Result result = stub().traverseGraph(TraversalDesc.newBuilder().
				setRoot(root).
				setDepth(1).
				setConnected(connected).
				setTicket(ticket == null ? -1: ticket).
				setOneshot(false).build());
		check(result);
		return unpack(result, TraversalTree.class);
	}

}
