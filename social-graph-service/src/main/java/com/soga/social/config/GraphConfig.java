package com.soga.social.config;

import com.typesafe.config.Config;

public class GraphConfig {

	private static final String GRAPH_CONFIG = "graph";
	
	private final String storeDir;
	
	GraphConfig() {
		Config rootConfig = ConfigLoader.configInstance;
		Config neo4jConfig = rootConfig.getConfig(GRAPH_CONFIG);
		
		this.storeDir = neo4jConfig.getString("storeDir");
	}

	public String getStoreDir() {
		return storeDir;
	}

}
