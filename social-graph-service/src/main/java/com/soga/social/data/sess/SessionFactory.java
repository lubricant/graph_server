package com.soga.social.data.sess;

import com.soga.social.data.SessionDB.Session;

public abstract class SessionFactory<T extends Session> {
	
	abstract Session initialize();
	abstract Session deserialize(byte[] data);
	abstract byte[] serialize(Session sess);
	
}
