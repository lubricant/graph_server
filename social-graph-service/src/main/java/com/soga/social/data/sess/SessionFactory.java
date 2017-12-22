package com.soga.social.data.sess;

import com.soga.social.data.SessionDB.Session;

public abstract class SessionFactory {
	
	public abstract Session initialize();
	public abstract Session deserialize(byte[] data);
	public abstract byte[] serialize(Session sess);
	
	
	public static SessionFactory useArraySession() {
		return new ArraySession.ArraySessionFactory();
	}
	
	public static SessionFactory useBloomSession() {
		return new BloomSession.BloomSessionFactory();
	}
	
}
