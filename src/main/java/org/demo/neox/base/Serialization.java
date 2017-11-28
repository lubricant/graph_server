package org.demo.neox.base;


import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.commons.lang3.Validate;

import io.netty.util.concurrent.FastThreadLocal;
import io.protostuff.LinkedBuffer;
import io.protostuff.ProtostuffIOUtil;
import io.protostuff.Schema;
import io.protostuff.runtime.RuntimeSchema;
import io.vertx.core.buffer.Buffer;

public class Serialization {
	
	private static class ProtostuffTup<T> {
		
		private final Schema<T> schema;
		private final LinkedBuffer buffer;
		
		private byte[] bytes;
		private long useNum, totalTraffic;

		ProtostuffTup(Class<T> clazz) {
			this.schema = RuntimeSchema.createFrom(clazz);
			this.buffer = LinkedBuffer.allocate(LinkedBuffer.MIN_BUFFER_SIZE);
		}
		
		int averageUsage(int len) {
			return (int) ((totalTraffic += len) / ++useNum);
		}
	}
	
    private final static FastThreadLocal<Map<Class<?>, ProtostuffTup<?>>>
    protoHolder = new FastThreadLocal<Map<Class<?>, ProtostuffTup<?>>>() {
        protected Map<Class<?>, ProtostuffTup<?>> initialValue() throws Exception {
            return new HashMap<>();
        }
    };

    @SuppressWarnings("unchecked")
	private final static <T> ProtostuffTup<T> parseClazz(Class<T> clazz) {
        Map<Class<?>, ProtostuffTup<?>> protoTupMap = protoHolder.get();
        ProtostuffTup<?> protoTup = protoTupMap.get(clazz);
        if (protoTup == null) {
        	protoTupMap.put(clazz, protoTup = new ProtostuffTup<>(clazz));
        }
        return (ProtostuffTup<T>) protoTup;
    }

    static <T> void writeMessage(final Buffer buf, T message, Class<T> clazz) throws Exception {
    	ProtostuffTup<T> protoTup = parseClazz(clazz);
    	
    	protoTup.buffer.clear();
    	int len = ProtostuffIOUtil.writeTo(protoTup.buffer, message, protoTup.schema);
    	
    	buf.appendInt(len); // write length
    	LinkedBuffer.writeTo(new OutputStream() {
    		public void write(byte bytes[], int off, int len) throws IOException  {
    			buf.appendBytes(bytes, off, len);
    		}
			public void write(int b) throws IOException {
				throw new UnsupportedOperationException("Ignore");
			}
		}, protoTup.buffer);
    	
    }

	static <T> T readMessage(Buffer buf, final int pos, Class<T> clazz) throws Exception {
		final int len = buf.getInt(pos);
		Validate.isTrue(buf.length() >= len + 4);
		
		ProtostuffTup<T> protoTup = parseClazz(clazz);
    	int bytesUsage = protoTup.averageUsage(len);
		
        if (protoTup.bytes == null || protoTup.bytes.length < len) {
        	protoTup.bytes = buf.getBytes(pos+4, pos+4+len);
        } else {
        	buf.getBytes(pos+4, pos+4+len, protoTup.bytes);
        	if (protoTup.bytes.length > (bytesUsage << 1) && bytesUsage > 16) {
        		protoTup.bytes = new byte[bytesUsage + (bytesUsage >> 2)];
        	}
        }
        
        T message = protoTup.schema.newMessage();
        ProtostuffIOUtil.mergeFrom(protoTup.bytes, message, protoTup.schema);
    	return message;
    }
}
