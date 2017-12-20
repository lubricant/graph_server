package com.soga.social.data.sess;

import com.soga.social.data.SessionDB;

public class HeapSession implements SessionDB.Session {

	@Override
	public void visit(long nodeId) {
	}

	@Override
	public boolean notVisited(long nodeId) {
		return false;
	}

}
