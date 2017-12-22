package com.soga.social.config;

import com.typesafe.config.Config;

public class RpcConfig {
	
	private static final String RPC_CONFIG = "rpc";
	
	private final int port;
	
	RpcConfig() {
		Config rootConfig = ConfigLoader.configInstance;
		Config grpcConfig = rootConfig.getConfig(RPC_CONFIG);
		
		port = grpcConfig.getInt("port");
	}

	public int getPort() {
		return port;
	}

}
