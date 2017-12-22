package com.soga.social.service;

import java.io.Closeable;
import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.protobuf.Any;
import com.soga.social.data.GraphDB;
import com.soga.social.data.SessionDB;
import com.soga.social.service.SocialGraphServiceGrpc.SocialGraphServiceImplBase;

import io.grpc.stub.StreamObserver;

public class SocialGraphServiceImp extends SocialGraphServiceImplBase 
implements Closeable {
    
	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	private final GraphDB graphDB;
	private final SessionDB sessDB;
	
	public SocialGraphServiceImp() throws Exception {
		graphDB = new GraphDB();
		sessDB = new SessionDB();
	}
	
	public void close() throws IOException {
		graphDB.close();
		sessDB.close();
	}
	
	public void createPerson(PersonKey request, StreamObserver<Result> responseObserver) {
    	responseObserver.onNext(
    			Result.newBuilder().setState(Result.Status.SUCCESS).build());
    }
	
    public void updatePerson(Person request, StreamObserver<Result> responseObserver) {
    	logger.error("====================================================================");
    	Map<String, Any> props = request.getPropsMap();
    	for (Entry<String, Any> entry: props.entrySet()) {
    		logger.error("Receive : {}  {} ", entry.getKey(), entry.getValue());
    	}
    	logger.error("====================================================================");
    	responseObserver.onNext(
    			Result.newBuilder().setState(Result.Status.SUCCESS).build());
    }


}
