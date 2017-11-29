package org.demo.neox.rpc;

import org.apache.commons.lang3.Validate;

import io.vertx.core.buffer.Buffer;

public class Protocol {

	private static final int START_FLAG = 0x233;
	private static final int END_FLAG = 0x666;

	/**
	 * START_FLAG: 4 [0-4]
	 * SEQUENCE: 8 [4-12]
	 * MARKER: 4 [12-16]
	 * MESSAGE: ? [16-N]
	 * END_FLAT: 4 [N-N+4]
	 * */
	public static <T> Buffer writeBuffer(
			long sequence, int marker, T message, Class<T> clazz) throws Exception {
		Buffer buffer = Buffer.buffer();
		buffer.appendInt(START_FLAG);
		buffer.appendLong(sequence);
		buffer.appendInt(marker);
		if (message != null)
			Serialization.writeMessage(buffer, message, clazz);
		buffer.appendInt(END_FLAG);
		return buffer;
	}

	public static void checkBuffer(Buffer buffer) {
		int length = buffer.length();
		Validate.isTrue(length >= 20);
		Validate.isTrue(buffer.getInt(0) == START_FLAG);
		Validate.isTrue(buffer.getInt(length-4) == END_FLAG);
	}

	public static long readSequence(Buffer buffer) {
		return buffer.getLong(4);
	}

	public static int readMarker(Buffer buffer) {
		return buffer.getInt(12);
	}

	public static <T> T readMessage(Buffer buffer, Class<T> clazz) throws Exception {
		if (buffer.length() == 20)
			return null;
		return Serialization.readMessage(buffer, 16, clazz);
	}

}
