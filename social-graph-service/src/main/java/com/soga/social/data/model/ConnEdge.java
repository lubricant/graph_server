package com.soga.social.data.model;

import java.util.Map;

public class ConnEdge {
	
	private String src;
	private String dst;
	private Map<String,Object> props;
	
	public static ConnEdge of(Object src, Object dst, Map<String, Object> props) {
		ConnEdge edge = new ConnEdge();
		edge.src = String.valueOf(src);
		edge.dst = String.valueOf(dst);
		edge.props = props;
		return edge;
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
