package com.soga.social.data;

import java.io.Closeable;
import java.io.IOException;

import org.rocksdb.Options;
import org.rocksdb.RocksDB;

import com.soga.social.config.RocksdbConfig;

public class SessionDB implements Closeable {

	static {
		RocksDB.loadLibrary();
	}
    
    private RocksDB dbInstance;
	
    public SessionDB(RocksdbConfig config) throws Exception {
    	Options opts = new Options();
    	opts.setCreateIfMissing(true);
    	opts.setDbLogDir(config.getLogDir());
    	
    	dbInstance = RocksDB.open(opts, config.getStoreDir());
    }
    
	public void close() throws IOException {
		if (dbInstance != null) {
			dbInstance.close();
			dbInstance = null;
		}
	}

}
