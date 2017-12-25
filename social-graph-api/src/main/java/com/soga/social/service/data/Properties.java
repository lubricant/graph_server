package com.soga.social.service.data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

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
import proto.type.StringList;


public class Properties {

	private Map<String, Any> propsMap;
	
	public Properties() {
		this(new HashMap<>());
	}
	
	private Properties(Map<String, Any> propsMap) {
		this.propsMap = propsMap;
	}
	
	public static Properties wrap(Map<String, Any> propsMap) {
		if (propsMap == null)
			throw new NullPointerException();
		return new Properties(propsMap);
	}
	
	public static Properties parse(Map<String, Object> propsMap) {
		if (propsMap == null)
			throw new NullPointerException();
		
		Properties props = new Properties();
		for (Entry<String, Object> prop: propsMap.entrySet()) {
			String key = prop.getKey();
			Object val = prop.getValue();
			if (val == null) {
				props.remove(key);
			} else {
				Class<?> clazz = val.getClass();
				if (! clazz.isArray()) {
					if (clazz == String.class) {
						props.setString(key, (String) val);
					} else if (clazz == Boolean.class) {
						props.setBool(key, (Boolean) val);
					} else if (clazz == Integer.class) {
						props.setInt(key, (Integer) val);
					} else if (clazz == Long.class) {
						props.setLong(key, (Long) val);
					} else if (clazz == Float.class) {
						props.setFloat(key, (Float) val);
					} else if (clazz == Double.class) {
						props.setDouble(key, (Double) val);
					} else {
						throw new IllegalArgumentException(
								String.format("Property with key '%s' is not a basic value.", key));
					}
				} else {
					if (clazz == String[].class) {
						props.setStringList(key, (String[]) val);
					} else if (clazz == boolean[].class) {
						props.setBoolList(key, (boolean[]) val);
					} else if (clazz == int[].class) {
						props.setIntList(key, (int[]) val);
					} else if (clazz == long[].class) {
						props.setLongList(key, (long[]) val);
					} else if (clazz == float[].class) {
						props.setFloatList(key, (float[]) val);
					} else if (clazz == double[].class) {
						props.setDoubleList(key, (double[]) val);
					} else {
						throw new IllegalArgumentException(
								String.format("Property with key '%s' is not a basic value.", key));
					}
				}
			}
		}
		
		return props;
	}
	
	public Map<String, Any> getProps() {
		return propsMap;
	}
	
	public Map<String, Object> getAllProps() throws Exception {
		Map<String, Object> allPropsMap = new HashMap<>(propsMap.size());
		for (Entry<String, Any> prop: propsMap.entrySet()) {
			String key = prop.getKey();
			Any val = prop.getValue();
			
			if (val == null || val.getTypeUrl().isEmpty()) {
				allPropsMap.put(key, null);
			} else {
				if (val.is(StringValue.class))
					allPropsMap.put(key, val.unpack(StringValue.class).getValue());
				else if (val.is(BoolValue.class))
					allPropsMap.put(key, val.unpack(BoolValue.class).getValue());
				else if (val.is(Int32Value.class))
					allPropsMap.put(key, val.unpack(Int32Value.class).getValue());
				else if (val.is(Int64Value.class))
					allPropsMap.put(key, val.unpack(Int64Value.class).getValue());
				else if (val.is(FloatValue.class))
					allPropsMap.put(key, val.unpack(FloatValue.class).getValue());
				else if (val.is(DoubleValue.class))
					allPropsMap.put(key, val.unpack(DoubleValue.class).getValue());
				else if (val.is(StringList.class)) {
					allPropsMap.put(key, val.unpack(StringList.class).getValueList().toArray());
				} else if (val.is(BoolList.class)) {
					List<Boolean> list = val.unpack(BoolList.class).getValueList();
					boolean[] array = new boolean[list.size()];
					for (int i=0; i<list.size(); i++)
						array[i] = list.get(i);
					allPropsMap.put(key, array);
				} else if (val.is(IntList.class)) {
					List<Integer> list = val.unpack(IntList.class).getValueList();
					int[] array = new int[list.size()];
					for (int i=0; i<list.size(); i++)
						array[i] = list.get(i);
					allPropsMap.put(key, array);
				} else if (val.is(LongList.class)) {
					List<Long> list = val.unpack(LongList.class).getValueList();
					long[] array = new long[list.size()];
					for (int i=0; i<list.size(); i++)
						array[i] = list.get(i);
					allPropsMap.put(key, array);
				} else if (val.is(FloatList.class)) {
					List<Float> list = val.unpack(FloatList.class).getValueList();
					float[] array = new float[list.size()];
					for (int i=0; i<list.size(); i++)
						array[i] = list.get(i);
					allPropsMap.put(key, array);
				} else if (val.is(DoubleList.class)) {
					List<Double> list = val.unpack(DoubleList.class).getValueList();
					double[] array = new double[list.size()];
					for (int i=0; i<list.size(); i++)
						array[i] = list.get(i);
					allPropsMap.put(key, array);
				} else {
					throw new IllegalArgumentException(
							String.format("Property with key '%s' is not a basic value.", key));
				}
			}
		}
		return allPropsMap;
	}
	
	public <T extends Message> T get(String key, Class<T> clazz) {
		Any any = propsMap.get(key);
		if (any == null || any.getTypeUrl().isEmpty())
			return null;
		try {
			return any.unpack(clazz);
		} catch (InvalidProtocolBufferException e) {
			throw new IllegalArgumentException(e);
		}
	}
	
	public void remove(String key) {
		propsMap.put(key, Any.getDefaultInstance());
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
