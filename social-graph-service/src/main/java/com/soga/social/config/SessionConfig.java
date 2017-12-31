package com.soga.social.config;

import com.typesafe.config.Config;

public class SessionConfig {

	private static final String SESS_CONFIG = "session";
	
	private final String storeDir;
	private final String logDir;
	private final boolean useBloom;
	private final boolean clearDir;
	
	SessionConfig() {
		Config rootConfig = ConfigLoader.configInstance;
		Config rocksdbConfig = rootConfig.getConfig(SESS_CONFIG);
		
		this.storeDir = rocksdbConfig.getString("storeDir");
		this.logDir = rocksdbConfig.getString("logDir");
		this.useBloom = rocksdbConfig.getBoolean("useBloom");
		this.clearDir = rocksdbConfig.getBoolean("clearDir");
	}

	public String getStoreDir() {
		return storeDir;
	}

	public String getLogDir() {
		return logDir;
	}

	public boolean isUseBloom() {
		return useBloom;
	}

	public boolean isClearDir() {
		return clearDir;
	}
	
	
}
