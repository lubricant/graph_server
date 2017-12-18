package com.soga.social.data.model;

import org.neo4j.graphdb.Label;

public enum GraphLabels implements Label {
	PERSON {
		public String[] uniqueness() {
			return new String[] { "pid" };
		}
	};
	
	public String[] uniqueness() { return null; };
	public String[][] indices() { return null; };
}
