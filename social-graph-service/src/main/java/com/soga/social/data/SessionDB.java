package com.soga.social.data;

import java.io.Closeable;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.commons.lang3.Validate;
import org.rocksdb.CompressionType;
import org.rocksdb.Options;
import org.rocksdb.RocksDB;
import org.rocksdb.RocksDBException;
import org.rocksdb.WriteOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.soga.social.config.ConfigLoader;
import com.soga.social.config.SessionConfig;
import com.soga.social.data.sess.SessionFactory;

public class SessionDB implements Closeable {

	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	public static interface Session {
		void visit(long nodeId);
		boolean notVisited(long nodeId);
	}
	
	static {
		RocksDB.loadLibrary();
	}
	
	private final static AtomicLong ticketSeq = 
			new AtomicLong();
	
	public static long acquireTicket() {
		return ticketSeq.incrementAndGet() & Long.MAX_VALUE;
	}

    private RocksDB dbInstance;
    private SessionFactory sessFactory;
	
    public SessionDB() throws Exception {
    	
    	SessionConfig config = ConfigLoader.getSessionConfig();
    	
    	Path dir = Paths.get(config.getStoreDir());
    	
    	Validate.isTrue(!Files.exists(dir) || Files.isDirectory(dir), "Session directory is not a directory: %s", dir);
    	Validate.isTrue(!Files.exists(dir) || Files.isWritable(dir), "Session directory is not writable: %s", dir);
    	
    	if (Files.exists(dir)) {
    		logger.info("Cleaning session directory.");
    		Files.walkFileTree(dir, new SimpleFileVisitor<Path>() {
    		    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
    		    		throws IOException {
    		    	Files.delete(file);
    		        return FileVisitResult.CONTINUE;
    		   }
    		});
    	}
    	
    	Options opts = new Options();
    	opts.setCreateIfMissing(true);
    	opts.setDbLogDir(config.getLogDir());
    	opts.setCompressionType(CompressionType.NO_COMPRESSION);
    	
    	if (config.isUseBloom())
    		sessFactory = SessionFactory.useBloomSession();
    	else
    		sessFactory = SessionFactory.useArraySession();
    	
    	dbInstance = RocksDB.open(opts, config.getStoreDir());
    }
    
	public void close() throws IOException {
		if (dbInstance != null) {
			dbInstance.close();
			dbInstance = null;
		}
	}
	
	public Session restoreSess(long ticket) throws RocksDBException {
		byte[] key = longToBytes(ticket);
		byte[] value = dbInstance.get(key);
		if (value == null)
			return sessFactory.initialize(); 
		else
			return sessFactory.deserialize(value); 
	}
	
	public void storeSess(long ticket, Session sess) throws RocksDBException {
		byte[] key = longToBytes(ticket);
		byte[] value = sessFactory.serialize(sess);
		try (WriteOptions opts = new WriteOptions()) {
			dbInstance.put(opts.setDisableWAL(true), key, value);
		}
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
		return  ((y[i] & 0xFFL) << 56) | 
				((y[i+1] & 0xFFL) << 48) | 
				((y[i+2] & 0xFFL) << 40) | 
				((y[i+3] & 0xFFL)<< 32) | 
				((y[i+4] & 0xFFL)<< 24) | 
				((y[i+5] & 0xFFL)<< 16) | 
				((y[i+6] & 0xFFL)<< 8) | 
				(y[i+7] & 0xFFL); 
	}
	
}
