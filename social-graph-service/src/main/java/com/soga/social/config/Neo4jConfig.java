package com.soga.social.config;

import com.typesafe.config.Config;

public class Neo4jConfig {

	Neo4jConfig() {
		Config rootConfig = ConfigLoader.configInstance;
		Config neo4jConfig = rootConfig.getConfig("neo4j");
	}
}
