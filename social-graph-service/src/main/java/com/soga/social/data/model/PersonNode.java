package com.soga.social.data.model;

import java.util.Map;

import org.neo4j.graphdb.Node;

public class PersonNode {
	
	private String pid;
	private Map<String,Object> props;

	public static PersonNode of(Node node) {
		Map<String,Object> props = node.getAllProperties();
		Object pid = props.remove("pid");
		
		PersonNode person = new PersonNode(); 
		person.pid = String.valueOf(pid);
		person.props= props;
		return person;
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
