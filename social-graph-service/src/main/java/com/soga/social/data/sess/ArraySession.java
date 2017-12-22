package com.soga.social.data.sess;

import java.util.Arrays;

import com.google.common.primitives.ImmutableLongArray;
import com.soga.social.data.SessionDB;
import com.soga.social.data.SessionDB.Session;

public class ArraySession implements SessionDB.Session {

	long[] oldArray = null;
	LongArray newArray = new LongArray(16);
	
	ArraySession() {} 
	
	ArraySession(byte[] data) {
		
		if (data.length % 8 != 0) 
			throw new IllegalArgumentException("Not a valid long array data.");
		
		oldArray = new long[data.length / 8];
		for (int i=0; i<oldArray.length; i++) {
			oldArray[i] = SessionDB.bytesToLong(data, i << 3);
		}
		Arrays.sort(oldArray);
	}
	
	@Override
	public void visit(long nodeId) {
		newArray.add(nodeId);
	}

	@Override
	public boolean notVisited(long nodeId) {
		for (int i=0; i<newArray.size(); i++) {
			if (newArray.array[i] == nodeId)
				return true;
		}
		
		return (oldArray == null) ? false: 
			Arrays.binarySearch(oldArray, nodeId) >= 0;
	}

	public byte[] toBytes() {
		
		long[] newArray = this.newArray.array;
		int newLen = this.newArray.size(), oldLen = (oldArray==null)? 0: oldArray.length ;
		
		int megIdx = 0;
		byte[] megData = new byte[(oldLen + newLen) * 8];
		
		while (newLen > 0) {
			SessionDB.longToBytes(newArray[--newLen], megData, (megIdx++) << 3);
		}
		
		while (oldLen > 0) {
			SessionDB.longToBytes(oldArray[--oldLen], megData, (megIdx++) << 3);
		}
		
		return megData;
	}
	
	
	static class ArraySessionFactory extends SessionFactory {

		@Override
		public Session initialize() {
			return new ArraySession();
		}

		public @Override
		Session deserialize(byte[] data) {
			return new ArraySession(data);
		}

		@Override
		public byte[] serialize(Session sess) {
			if (sess instanceof ArraySession) {
				ArraySession session = (ArraySession) sess;
				return session.toBytes();
			}
			throw new IllegalArgumentException("Expect a array session.");
		}
		
	}
	
}


/**
 * see {@link ImmutableLongArray#Builder}
 */
class LongArray {
	
	long[] array;
	private int count = 0; // <= array.length

	LongArray(long[] array) {
		this.array = array;
		this.count = array.length;
	}
	
	LongArray(int initialCapacity) {
		array = new long[initialCapacity];
	}

	public long get(int index) {
		return array[index];
	}
	
	public void set(int index, long value) {
		array[index] = value;
	}
	
	public long remove(int index) {
        if (index + 1 < count)
            System.arraycopy(array, index+1, array, index, count-index-1);
        return array[--count];
	}
	
	public void swap(int from, int to) {
		long temp = array[to];
		array[to] = array[from];
		array[from] = temp;
	}
	
	public int size() {
		return count;
	}
	
	public void add(long value) {
		ensureRoomFor(1);
		array[count] = value;
		count += 1;
	}

	public void addAll(long[] values) {
		ensureRoomFor(values.length);
		System.arraycopy(values, 0, array, count, values.length);
		count += values.length;
	}

	public int copyTo(long[] target, int offset) {
		if (target.length - offset < count)
			throw new IndexOutOfBoundsException("Not enouth space to receive data.");
		System.arraycopy(array, 0, target, offset, count);
		return offset + count;
	}
	
	private void ensureRoomFor(int numberToAdd) {
		int newCount = count + numberToAdd; // TODO(kevinb): check overflow now?
		if (newCount > array.length) {
			long[] newArray = new long[expandedCapacity(array.length, newCount)];
			System.arraycopy(array, 0, newArray, 0, count);
			this.array = newArray;
		}
	}

	private static int expandedCapacity(int oldCapacity, int minCapacity) {
		if (minCapacity < 0) {
			throw new AssertionError("cannot store more than MAX_VALUE elements");
		}
		// careful of overflow!
		int newCapacity = oldCapacity + (oldCapacity >> 1) + 1;
		if (newCapacity < minCapacity) {
			newCapacity = Integer.highestOneBit(minCapacity - 1) << 1;
		}
		if (newCapacity < 0) {
			newCapacity = Integer.MAX_VALUE; // guaranteed to be >= newCapacity
		}
		return newCapacity;
	}

	public String toString() {
		return Arrays.toString(array);
	}
}
