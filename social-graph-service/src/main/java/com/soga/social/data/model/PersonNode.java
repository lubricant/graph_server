package com.soga.social.data.model;

import java.util.Map;

public class PersonNode {
	
	private String pid;
	private Map<String,Object> props;

	public static PersonNode of(Object pid, Map<String, Object> props) {
		PersonNode node = new PersonNode();
		node.pid = String.valueOf(pid);
		node.props= props;
		return node;
	}
	
	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public Map<String,Object> getProps() {
		return props;
	}

	public void setProps(Map<String,Object> props) {
		this.props = props;
	}
	
}
