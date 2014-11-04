package com.jastt.business.domain.entities;

import java.io.Serializable;

import com.jastt.business.domain.entities.PermissionId;
import com.jastt.business.domain.entities.Project;
import com.jastt.business.domain.entities.User;

class Permission implements Serializable{
	
	private PermissionId id;
	private User user;
	private Project project;
	
	
	public PermissionId getId() {
		return id;
	}
	
	public void setId(PermissionId id) {
		this.id = id;
	}
	
	public User getUser() {
		return user;
	}
	
	public void setUser(User user) {
		this.user = user;
	}
	
	public Project getProject() {
		return project;
	}
	
	public void setProject(Project project) {
		this.project = project;
	}	
	
}