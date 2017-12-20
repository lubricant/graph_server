package com.soga.social.service.data;

import java.util.HashMap;
import java.util.Map;

import com.google.protobuf.Any;
import com.google.protobuf.BoolValue;
import com.google.protobuf.DoubleValue;
import com.google.protobuf.FloatValue;
import com.google.protobuf.Int32Value;
import com.google.protobuf.Int64Value;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Message;
import com.google.protobuf.StringValue;

import proto.type.BoolList;
import proto.type.DoubleList;
import proto.type.FloatList;
import proto.type.IntList;
import proto.type.LongList;
import proto.type.Null;
import proto.type.StringList;


public class Properties {

	private Map<String, Any> propsMap;
	
	public Properties() {
		this(new HashMap<>());
	}
	
	public Properties(Map<String, Any> propsMap) {
		if (propsMap == null)
			throw new NullPointerException();
		this.propsMap = propsMap;
	}
	
	public Map<String, Any> getProps() {
		return propsMap;
	}
	
	public <T extends Message> T get(String key, Class<T> clazz) {
		Any any = propsMap.get(key);
		if (any == null || any.is(Null.class))
			return null;
		try {
			return any.unpack(clazz);
		} catch (InvalidProtocolBufferException e) {
			throw new IllegalArgumentException(e);
		}
	}
	
	public void remove(String key) {
		propsMap.put(key, Any.pack(Null.getDefaultInstance()));
	}
	
	public Properties setBool(String key, boolean val) {
		propsMap.put(key, Any.pack(BoolValue.of(val)));
		return this;
	}
	public Properties setInt(String key, int val) {
		propsMap.put(key, Any.pack(Int32Value.of(val)));
		return this;
	}
	public Properties setLong(String key, long val) {
		propsMap.put(key, Any.pack(Int64Value.of(val)));
		return this;
	}
	public Properties setFloat(String key, float val) {
		propsMap.put(key, Any.pack(FloatValue.of(val)));
		return this;
	}
	public Properties setDouble(String key, double val) {
		propsMap.put(key, Any.pack(DoubleValue.of(val)));
		return this;
	}
	public Properties setString(String key, String val) {
		propsMap.put(key, Any.pack(StringValue.of(val)));
		return this;
	}
	
	public Properties setBoolList(String key, boolean[] vals) {
		BoolList.Builder builder = BoolList.newBuilder();
		for (boolean b: vals) builder.addValue(b);
		propsMap.put(key, Any.pack(builder.build()));
		return this;
	}
	public Properties setIntList(String key, int[] vals) {
		IntList.Builder builder = IntList.newBuilder();
		for (int i: vals) builder.addValue(i);
		propsMap.put(key, Any.pack(builder.build()));
		return this;
	}
	public Properties setLongList(String key, long[] vals) {
		LongList.Builder builder = LongList.newBuilder();
		for (long l: vals) builder.addValue(l);
		propsMap.put(key, Any.pack(builder.build()));
		return this;
	}
	public Properties setFloatList(String key, float[] vals) {
		FloatList.Builder builder = FloatList.newBuilder();
		for (float f: vals) builder.addValue(f);
		propsMap.put(key, Any.pack(builder.build()));
		return this;
	}
	public Properties setDoubleList(String key, double[] vals) {
		DoubleList.Builder builder = DoubleList.newBuilder();
		for (double d: vals) builder.addValue(d);
		propsMap.put(key, Any.pack(builder.build()));
		return this;
	}
	public Properties setStringList(String key, String[] vals) {
		StringList.Builder builder = StringList.newBuilder();
		for (String s: vals) builder.addValue(s);
		propsMap.put(key, Any.pack(builder.build()));
		return this;
	}
	
	public BoolValue getBool(String key) {
		return get(key, BoolValue.class);
	}
	public Int32Value getInt(String key) {
		return get(key, Int32Value.class);
	}
	public Int64Value getLong(String key) {
		return get(key, Int64Value.class);
	}
	public FloatValue getFloat(String key) {
		return get(key, FloatValue.class);
	}
	public DoubleValue getDouble(String key) {
		return get(key, DoubleValue.class);
	}
	public StringValue getString(String key) {
		return get(key, StringValue.class);
	}
	
	public BoolList getBoolList(String key) {
		return get(key, BoolList.class);
	}
	public IntList getInt32List(String key) {
		return get(key, IntList.class);
	}
	public LongList getInt64List(String key) {
		return get(key, LongList.class);
	}
	public FloatList getFloatList(String key) {
		return get(key, FloatList.class);
	}
	public DoubleList getDoubleList(String key) {
		return get(key, DoubleList.class);
	}
	public StringList getStringList(String key) {
		return get(key, StringList.class);
	}
}
