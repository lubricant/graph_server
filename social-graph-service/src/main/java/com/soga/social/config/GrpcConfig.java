package com.soga.social.config;

import com.typesafe.config.Config;

public class GrpcConfig {
	
	private int port;
	
	GrpcConfig() {
		Config rootConfig = ConfigLoader.configInstance;
		Config grpcConfig = rootConfig.getConfig("grpc");
		
		this.port = grpcConfig.getInt("port");
	}
	
}
