package com.soga.social.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.soga.social.config.ConfigLoader;
import com.soga.social.config.GrpcConfig;

import io.grpc.Server;
import io.grpc.ServerBuilder;

public final class SocialGraphServer {
	
	private SocialGraphServer() {}
	
	private final static Logger logger = LoggerFactory.getLogger(SocialGraphServer.class);
	
	private static Server serverInstance;
	
	public static void start() {
		
		try {
			
			GrpcConfig config = ConfigLoader.getGrpcConfig();
			
			serverInstance = ServerBuilder.forPort(config.getPort()).
					addService(new SocialGraphServiceImp()).
					build();
			
			serverInstance.start();
			
		} catch (Exception e) {
			logger.error("Server fail to start.", e);
			if (serverInstance != null && !serverInstance.isShutdown()) {
				serverInstance.shutdownNow();	
			}
			return;
		}
		
		try {
			logger.info("Server has started successfully.");
			serverInstance.awaitTermination();
		} catch (InterruptedException e) {
			logger.error("Server is interrupted.", e);
		}
	}
	
	public static void shutdown() {
		logger.warn("Server is shutting down.");
		if (serverInstance != null) {
			serverInstance.shutdown();
		}
	}
	
	public static void main(String[] args) {
		Runtime.getRuntime().addShutdownHook(new Thread(SocialGraphServer::shutdown));
		SocialGraphServer.start();
	}
}
