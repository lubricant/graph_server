package com.soga.social.data;

import java.io.Closeable;
import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicLong;

import org.rocksdb.CompressionType;
import org.rocksdb.Options;
import org.rocksdb.RocksDB;
import org.rocksdb.RocksDBException;

import com.soga.social.config.RocksdbConfig;

public class SessionDB implements Closeable {

	public static interface Session {
		long id();
		void visit(long nodeId);
		boolean notVisited(long nodeId);
	}
	
	static {
		RocksDB.loadLibrary();
	}
	
	private final static AtomicLong ticketSeq = 
			new AtomicLong();
	
	public static long newTicket() {
		return ticketSeq.incrementAndGet();
	}

    private RocksDB dbInstance;
	
    public SessionDB(RocksdbConfig config) throws Exception {
    	Options opts = new Options();
    	opts.setCreateIfMissing(true);
    	opts.setDbLogDir(config.getLogDir());
    	opts.setCompressionType(CompressionType.SNAPPY_COMPRESSION);
    	
    	dbInstance = RocksDB.open(opts, config.getStoreDir());
    }
    
	public void close() throws IOException {
		if (dbInstance != null) {
			dbInstance.close();
			dbInstance = null;
		}
	}
	
	public Session acquireSess() {
		return null; 
	}
	
	public Session restoreSess(long ticket) throws RocksDBException {
		byte[] key = longToBytes(ticket);
		dbInstance.get(key);
		dbInstance.put(key, null);
		return null; 
	}
	
	public void storeSess(long ticket, Session sess) {
		
	}
	
	final static byte[] longToBytes(long x) {
		byte[] y = {
			(byte) ((x >>> 56) & 0xFF), 
			(byte) ((x >>> 48) & 0xFF), 
			(byte) ((x >>> 40) & 0xFF), 
			(byte) ((x >>> 32) & 0xFF), 
			(byte) ((x >>> 24) & 0xFF), 
			(byte) ((x >>> 16) & 0xFF), 
			(byte) ((x >>> 8) & 0xFF), 
			(byte) (x & 0xFF)};
		return y;
	}
	
	public static void main(String[] args) {
		System.out.println(Arrays.toString(longToBytes(0)));
		System.out.println(Arrays.toString(longToBytes(-1)));
		System.out.println(Arrays.toString(longToBytes(16)));
		System.out.println(Arrays.toString(longToBytes(-16)));
	}
}
