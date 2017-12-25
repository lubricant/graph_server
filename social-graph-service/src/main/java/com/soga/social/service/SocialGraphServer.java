package com.soga.social.service;

import java.io.Closeable;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.soga.social.config.ConfigLoader;
import com.soga.social.config.RpcConfig;

import io.grpc.Server;
import io.grpc.ServerBuilder;

public final class SocialGraphServer {
	
	private SocialGraphServer() {}
	
	private final static Logger logger = LoggerFactory.getLogger(SocialGraphServer.class);
	
	private static Server serverInstance;
	private static Closeable resource;
	
	public static void start(boolean blocked) {
		
		try {
			
			RpcConfig config = ConfigLoader.getRpcConfig();
			SocialGraphServiceImp service = new SocialGraphServiceImp();
			
			resource = service;
			serverInstance = ServerBuilder.forPort(config.getPort()).addService(service).build();
			serverInstance.start();
			
		} catch (Exception e) {
			logger.error("Server fail to start.", e);
			shutdown(false);
			return;
		}
		
		try {
			logger.info("Server has started successfully.");
			if (blocked)
				serverInstance.awaitTermination();
		} catch (InterruptedException e) {
			logger.error("Server is interrupted.", e);
		}
	}
	
	public static void shutdown(boolean gracefully) {
		logger.warn("Server is shutting down.");
		
		if (resource != null) {
			try (Closeable r = resource) {
			} catch (IOException ex) {
				logger.error("Resource fail to close.", ex);
			} 
			resource = null;
		}

		if (serverInstance != null && !serverInstance.isShutdown()) {
			if (! gracefully)
				serverInstance.shutdownNow();	
			else
				serverInstance.shutdown();
			serverInstance = null;
		}
	}
	
	public static void main(String[] args) {
		Runtime.getRuntime().addShutdownHook(new Thread(()->SocialGraphServer.shutdown(true)));
		SocialGraphServer.start(true);
	}
}
