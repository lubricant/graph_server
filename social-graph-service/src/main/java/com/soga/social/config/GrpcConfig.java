package com.soga.social.config;

import com.typesafe.config.Config;

public class GrpcConfig {
	
	private final int port;
	
	GrpcConfig() {
		Config rootConfig = ConfigLoader.configInstance;
		Config grpcConfig = rootConfig.getConfig("grpc");
		
		port = grpcConfig.getInt("port");
	}

	public int getPort() {
		return port;
	}

}
