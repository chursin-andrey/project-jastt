package com.jastt.business.domain.entities;


import com.jastt.business.domain.entities.Project;
import com.jastt.business.domain.entities.User;

public class Permission extends PersistentEntity<Integer>{
	
	private static final long serialVersionUID = -3308659067131109492L;
	private User user;
	private Project project;
	
	public Permission(){
		
	}
	
	public Permission(User user, Project project) {
		super();
		this.user = user;
		this.project = project;
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