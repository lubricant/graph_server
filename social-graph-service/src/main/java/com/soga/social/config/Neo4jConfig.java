package com.soga.social.config;

import java.util.List;

import com.typesafe.config.Config;

public class Neo4jConfig {

	private final String storeDir;
	
	Neo4jConfig() {
		Config rootConfig = ConfigLoader.configInstance;
		Config neo4jConfig = rootConfig.getConfig("neo4j");
		
		this.storeDir = neo4jConfig.getString("storeDir");
	}

	public String getStoreDir() {
		return storeDir;
	}

}
