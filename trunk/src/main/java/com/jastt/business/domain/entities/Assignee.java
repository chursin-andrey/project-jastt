package com.jastt.business.domain.entities;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import com.jastt.business.domain.entities.Issue;


class Assignee implements Serializable{
	
	private Integer id;
	private String name;
	private String email;
	private Set<Issue> issues = new HashSet<Issue>(0);
	
	
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
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public Set<Issue> getIssues() {
		return issues;
	}
	
	public void setIssues(Set<Issue> issues) {
		this.issues = issues;
	}	
		
}
