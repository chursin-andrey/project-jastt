package com.jastt.business.domain.entities;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import com.jastt.business.domain.entities.Project;
import com.jastt.business.domain.entities.User;


class Server implements Serializable{
	
	private Integer id;
	private String name;
	private String url;
	private Set<User> users = new HashSet<User>(0);
	private Set<Project> projects = new HashSet<Project>(0);
	
	
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getUrl() {
		return url;
	}
	
	public void setUrl(String url) {
		this.url = url;
	}
	
	public Set<User> getUsers() {
		return users;
	}
	
	public void setUsers(Set<User> users) {
		this.users = users;
	}
	
	public Set<Project> getProjects() {
		return projects;
	}
	
	public void setProjects(Set<Project> projects) {
		this.projects = projects;
	}
	
	
}