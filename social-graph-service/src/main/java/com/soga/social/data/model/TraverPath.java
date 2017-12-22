package com.soga.social.data.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TraverPath {
	
	private long id;
	private PersonNode node;
	private List<ConnEdge> edges;
	private Map<Long, TraverPath> branches;
	
	public static TraverPath of(long id, PersonNode node) {
		TraverPath path = new TraverPath();
		path.id = id;
		path.node = node;
		path.edges = new ArrayList<>(8);
		path.branches = new HashMap<>(8);
		return path;
	}
	
	public PersonNode getNode() {
		return node;
	}
	public void setNode(PersonNode node) {
		this.node = node;
	}
	public List<ConnEdge> getEdges() {
		return edges;
	}
	public void setEdges(List<ConnEdge> edges) {
		this.edges = edges;
	}

	public Map<Long, TraverPath> getBranches() {
		return branches;
	}

	public void setBranches(Map<Long, TraverPath> branches) {
		this.branches = branches;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
}
