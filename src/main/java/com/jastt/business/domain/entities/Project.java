package com.jastt.business.domain.entities;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import com.jastt.business.domain.entities.Issue;
import com.jastt.business.domain.entities.Permission;
import com.jastt.business.domain.entities.Server;

class Project implements Serializable{
	
	private Integer id;
	private Server server;
	private String key;
	private String name;
	private Set<Permission> permissions = new HashSet<Permission>(0);
	private Set<Issue> issues = new HashSet<Issue>(0);
	
	
	
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public Server getServer() {
		return server;
	}
	
	public void setServer(Server server) {
		this.server = server;
	}
	
	public String getKey() {
		return key;
	}
	
	public void setKey(String key) {
		this.key = key;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public Set<Permission> getPermissions() {
		return permissions;
	}
	
	public void setPermissions(Set<Permission> permissions) {
		this.permissions = permissions;
	}
	
	public Set<Issue> getIssues() {
		return issues;
	}
	
	public void setIssues(Set<Issue> issues) {
		this.issues = issues;
	}
	
}
