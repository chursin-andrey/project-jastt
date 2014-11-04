package com.jastt.business.domain.entities;

import java.io.Serializable;
import java.util.Date;

import com.jastt.business.domain.entities.Assignee;
import com.jastt.business.domain.entities.Project;

class Issue implements Serializable {
	
	private Integer id;
	private String version;
	private Assignee assignee;
	private Project project;
	private String key;
	private String issueType;
	private String status;
	private Date created;
	private Date updated;
	private String priority;
	private double timeSpent;
	
	
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public String getVersion() {
		return version;
	}
	
	public void setVersion(String version) {
		this.version = version;
	}
	
	public Assignee getAssignee() {
		return assignee;
	}
	
	public void setAssignee(Assignee assignee) {
		this.assignee = assignee;
	}
	
	public Project getProject() {
		return project;
	}
	
	public void setProject(Project project) {
		this.project = project;
	}
	
	public String getKey() {
		return key;
	}
	
	public void setKey(String key) {
		this.key = key;
	}
	
	public String getIssueType() {
		return issueType;
	}
	
	public void setIssueType(String issueType) {
		this.issueType = issueType;
	}
	
	public String getStatus() {
		return status;
	}
	
	public void setStatus(String status) {
		this.status = status;
	}
	
	public Date getCreated() {
		return created;
	}
	
	public void setCreated(Date created) {
		this.created = created;
	}
	
	public Date getUpdated() {
		return updated;
	}
	
	public void setUpdated(Date updated) {
		this.updated = updated;
	}
	
	public String getPriority() {
		return priority;
	}
	
	public void setPriority(String priority) {
		this.priority = priority;
	}
	
	public double getTimeSpent() {
		return timeSpent;
	}
	
	public void setTimeSpent(double timeSpent) {
		this.timeSpent = timeSpent;
	}
	
	
}