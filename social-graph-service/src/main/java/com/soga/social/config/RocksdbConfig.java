package com.soga.social.config;

import com.typesafe.config.Config;

public class RocksdbConfig {

	RocksdbConfig() {
		Config rootConfig = ConfigLoader.configInstance;
		Config rocksdbConfig = rootConfig.getConfig("rocksdb");
	}
	
}
