package org.demo.neox.base;

import org.apache.commons.lang3.Validate;

import io.vertx.core.buffer.Buffer;

public class Protocol {

	private static final int START_FLAG = 0x233;
	private static final int END_FLAG = 0x666;
	
	static <T> Buffer wrapMessage(long sequence, T message, Class<T> clazz) throws Exception {
		Buffer buffer = Buffer.buffer();
		buffer.appendInt(START_FLAG);
		buffer.appendLong(sequence);
		Serialization.writeMessage(buffer, message, clazz);
		buffer.appendInt(END_FLAG);
		return buffer;
	}
	
	static <T> T unwrapMessage(Buffer buffer, Class<T> clazz, long[] sequence) throws Exception {
		Validate.isTrue(buffer.getInt(0) == START_FLAG);
		Validate.isTrue(buffer.getInt(buffer.length()-4) == END_FLAG);
		sequence[0] = buffer.getLong(4);
		T message = Serialization.readMessage(buffer, 4+8, clazz);
		return message;
	}
	
}
