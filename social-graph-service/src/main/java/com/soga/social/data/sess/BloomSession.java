package com.soga.social.data.sess;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;
import com.soga.social.data.SessionDB;
import com.soga.social.data.SessionDB.Session;

public class BloomSession implements SessionDB.Session {
	
	private BloomFilter<Long> bloomFilter;
	
	static class BloomSessionFactory extends SessionFactory<BloomSession> {
		
		Session deserialize(final byte[] data) {
			try {
				BloomFilter<Long> filter = BloomFilter.readFrom(new InputStream() {
					int i = 0;
					public int read() throws IOException {
						return i >= data.length ? -1: Byte.toUnsignedInt(data[i++]); 
					}
				}, Funnels.longFunnel());
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
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
	
	public static void main(String[] args) throws IOException {
		BloomFilter<Long> filter = BloomFilter.create(Funnels.longFunnel(), 1);
		
		filter.put(1L);
		
		filter.put(988L);
		
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		filter.writeTo(stream);
		byte[] bytes = stream.toByteArray();
		
		BloomFilter<Long> filterB = BloomFilter.readFrom(new InputStream() {
			int i = 0;
			public int read() throws IOException {
				return i >= bytes.length ? -1: Byte.toUnsignedInt(bytes[i++]); 
			}
		}, Funnels.longFunnel());
		
	}

	@Override
	public long id() {
		return 0;
	}

	@Override
	public void visit(long nodeId) {
	}

	@Override
	public boolean notVisited(long nodeId) {
		return false;
	}
	
}
