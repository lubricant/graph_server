package com.soga.social.config;

import com.typesafe.config.Config;

public class RocksdbConfig {

	private final String storeDir;
	private final String logDir;
	
	RocksdbConfig() {
		Config rootConfig = ConfigLoader.configInstance;
		Config rocksdbConfig = rootConfig.getConfig("rocksdb");
		
		this.storeDir = rocksdbConfig.getString("storeDir");
		this.logDir = rocksdbConfig.getString("logDir");
	}

	public String getStoreDir() {
		return storeDir;
	}

	public String getLogDir() {
		return logDir;
	}
	
	
}
