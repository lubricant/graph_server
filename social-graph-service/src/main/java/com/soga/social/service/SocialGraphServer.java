package com.soga.social.service;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.grpc.Server;
import io.grpc.ServerBuilder;

public final class SocialGraphServer {
	
	private SocialGraphServer() {}
	
	private final static Logger logger = LoggerFactory.getLogger(SocialGraphServer.class);
	
	private static Server serverInstance;
	
	public static void start() {
		
		try {
			
			serverInstance = ServerBuilder.forPort(2333).
					addService(new SocialGraphServiceImp()).
					build();
			
			serverInstance.start();
		} catch (Exception e) {
			logger.error("服务启动失败", e);
			if (serverInstance != null && !serverInstance.isShutdown()) {
				serverInstance.shutdownNow();	
			}
			return;
		}
		
		try {
			logger.info("服务已启动");
			server.awaitTermination();
		} catch (InterruptedException e) {
			logger.error("服务被中断", e);
		}
	}
	
	public static void shutdown() {
		logger.warn("服务关闭中");
		if (serverInstance != null) {
			serverInstance.shutdown();
		}
	}
	
	public static void main(String[] args) {
		Runtime.getRuntime().addShutdownHook(new Thread(SocialGraphServer::shutdown));
		SocialGraphServer.start();
	}
}
