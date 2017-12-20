package com.soga.social.data.sess;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;
import com.soga.social.data.SessionDB;
import com.soga.social.data.SessionDB.Session;

public class BloomSession implements SessionDB.Session {
	
	private final BloomFilter<Long> bloomFilter;
	
	private BloomSession(BloomFilter<Long> filter) {
		bloomFilter = filter;
	}

	@Override
	public void visit(long nodeId) {
		bloomFilter.put(nodeId);
	}

	@Override
	public boolean notVisited(long nodeId) {
		return !bloomFilter.mightContain(nodeId);
	}
	
	static class BloomSessionFactory extends SessionFactory<BloomSession> {

		Session initialize() {
			return new BloomSession(
					BloomFilter.create(Funnels.longFunnel(), 500));
		}
		
		Session deserialize(final byte[] data) {
			try {
				return new BloomSession(BloomFilter.readFrom(new InputStream() {
					int i = 0;
					public int read() throws IOException {
						return i >= data.length ? -1: Byte.toUnsignedInt(data[i++]); 
					}
				}, Funnels.longFunnel()));
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}

		byte[] serialize(Session sess) {
			if (sess instanceof BloomSession) {
				BloomSession session = (BloomSession) sess;
				ByteArrayOutputStream stream = new ByteArrayOutputStream();
				try {
					session.bloomFilter.writeTo(stream);
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
				return stream.toByteArray();
			}
			throw new IllegalArgumentException("Expect a bloom session.");
		}

	}
	

	
}
