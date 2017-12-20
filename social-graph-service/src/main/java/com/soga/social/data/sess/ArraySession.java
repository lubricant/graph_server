package com.soga.social.data.sess;

import java.util.Arrays;

import com.google.common.primitives.ImmutableLongArray;
import com.soga.social.data.SessionDB;

public class ArraySession implements SessionDB.Session {

	LongArray oldArray = null, newArray;
	
	ArraySession() {
		newArray = new LongArray(16);
	}
	
	ArraySession(long[] oldVal) {
		newArray = new LongArray(16);
		oldArray = new LongArray(oldVal);
		oldArray.sort();
	}
	
	@Override
	public void visit(long nodeId) {
		newArray.add(nodeId);
	}

	@Override
	public boolean notVisited(long nodeId) {
		for (int i=0; i<newArray.size(); i++) {
			if (newArray.get(i) == nodeId)
				return true;
		}
		return (oldArray == null) ? false: oldArray.biSearch(nodeId);
	}

	public long[] mergeArray() {
		if (oldArray == null)
			return newArray.copy();
		
		newArray.sort();
		
		int oldIdx = 0, newIdx = 0, megIdx = 0;
		int oldLen = oldArray.size(), newLen = newArray.size();
		
		long[] mergedArray = new long[oldLen + newLen];
		while (oldIdx < oldLen && newIdx < newLen) {
			long oldVal = oldArray.get(oldIdx);
			long newVal = newArray.get(newIdx);
			if (oldVal < newVal) {
				mergedArray[megIdx++] = oldVal;
				++oldIdx;
			} else {
				mergedArray[megIdx++] = newVal;
				++newIdx;
			}
		}
		
		while (oldIdx < oldLen) {
			mergedArray[megIdx++] = oldArray.get(oldIdx++);
		}
		while (newIdx < newLen) {
			mergedArray[megIdx++] = newArray.get(newIdx++);
		}
		return mergedArray;
	}
}


/**
 * see {@link ImmutableLongArray#Builder}:
 */
class LongArray {
	
	private long[] array;
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

	public long[] copy() {
		return Arrays.copyOf(array, count);
	}
	
	public void sort() {
		Arrays.sort(array, 0, count);
	}
	
	public boolean biSearch(long key) {
		return Arrays.binarySearch(array, 0, count, key) >= 0;
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
