package com.soga.social.data.model;

import java.util.Map;

import org.neo4j.graphdb.Relationship;

public class ConnEdge {
	
	private String src;
	private String dst;
	private Map<String,Object> props;
	
	public static ConnEdge of(
			Object src, Object dst) {
		ConnEdge conn = new ConnEdge();
		conn.src = String.valueOf(src);
		conn.dst = String.valueOf(dst);
		return conn;
	}
	
	public static ConnEdge of(
			Object src, Object dst, Relationship edge) {
		
		ConnEdge conn = new ConnEdge();
		conn.src = String.valueOf(src);
		conn.dst = String.valueOf(dst);
		conn.props = edge.getAllProperties();
		return conn;
	}
	
	public static ConnEdge of(
			String src, String dst, Map<String,Object> props) {
		ConnEdge conn = new ConnEdge();
		conn.src = String.valueOf(src);
		conn.dst = String.valueOf(dst);
		conn.props = props;
		return conn;
	}
	
	public String getSrc() {
		return src;
	}
	public void setSrc(String src) {
		this.src = src;
	}
	public String getDst() {
		return dst;
	}
	public void setDst(String dst) {
		this.dst = dst;
	}
	public Map<String,Object> getProps() {
		return props;
	}
	public void setProps(Map<String,Object> props) {
		this.props = props;
	}
}
