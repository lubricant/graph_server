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
import com.soga.social.data.sess.SessionFactory;

public class SessionDB implements Closeable {

	public static interface Session {
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
    private SessionFactory<Session> sessFactory;
	
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
	
	private static byte[] longToBytes(long x) {
		byte[] y = {0,0,0,0,0,0,0,0};
		longToBytes(x, y, 0);
		return y;
	}
	
	public final static void longToBytes(long x, byte[] y, int i) {
		y[i] = (byte) ((x >>> 56) & 0xFF);
		y[i+1] = (byte) ((x >>> 48) & 0xFF); 
		y[i+2] = (byte) ((x >>> 40) & 0xFF); 
		y[i+3] = (byte) ((x >>> 32) & 0xFF); 
		y[i+4] = (byte) ((x >>> 24) & 0xFF); 
		y[i+5] = (byte) ((x >>> 16) & 0xFF); 
		y[i+6] = (byte) ((x >>> 8) & 0xFF); 
		y[i+7] = (byte) (x & 0xFF);
	}
	
	public final static long bytesToLong(byte[] y, int i) {
		return  (y[i] << 56) | (y[i+1] << 28) | (y[i+2] << 40) | (y[i+3] << 32) | 
				(y[i+4] << 24) | (y[i+5] << 16) | (y[i+6] << 8) | y[i+7] ; 
	}
	
	public static void main(String[] args) {
		System.out.println(Arrays.toString(longToBytes(0)));
		System.out.println(Arrays.toString(longToBytes(-1)));
		System.out.println(Arrays.toString(longToBytes(16)));
		System.out.println(Arrays.toString(longToBytes(-16)));
	}
}
