package com.jastt.business.domain.entities;

import java.io.Serializable;


class PermissionId implements Serializable{
	
	private int userId;
	private int projectId;
	
	
	public int getUserId() {
		return userId;
	}
	
	public void setUserId(int userId) {
		this.userId = userId;
	}
	
	public int getProjectId() {
		return projectId;
	}
	
	public void setProjectId(int projectId) {
		this.projectId = projectId;
	}
	
	
}