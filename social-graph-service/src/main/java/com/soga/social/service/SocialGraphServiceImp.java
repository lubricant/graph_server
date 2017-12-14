package com.soga.social.service;

import com.soga.social.service.SocialGraphServiceGrpc.SocialGraphServiceImplBase;

import io.grpc.stub.StreamObserver;

public class SocialGraphServiceImp extends SocialGraphServiceImplBase {
    
	public void createPerson(PersonKey request, StreamObserver<Result> responseObserver) {
    	responseObserver.onNext(
    			Result.newBuilder().setState(Result.Status.SUCCESS).build());
    }
	
}
