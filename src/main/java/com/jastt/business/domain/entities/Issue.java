package com.jastt.business.domain.entities;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.jastt.business.domain.entities.Assignee;
import com.jastt.business.domain.entities.Project;

public class Issue extends PersistentEntity<Integer>{
	
	private static final long serialVersionUID = -3505282155291252607L;
	
	private String version;
	private Assignee assignee;
	private Project project;
	private String key;
	private String issueType;
	private String status;
	private Date created;
	private Date updated;
	private String priority;
	private String summary;
	private int timeSpent;
	private Set<Worklog> worklogs = new HashSet<Worklog>(0);
	
	public Issue(){
		
	}
	
	
	public Issue(String version, Assignee assignee, Project project,
			String key, String issueType, String status, Date created,
			Date updated, String priority, int timeSpent) {
		
		super();
		this.version = version;
		this.assignee = assignee;
		this.project = project;
		this.key = key;
		this.issueType = issueType;
		this.status = status;
		this.created = created;
		this.updated = updated;
		this.priority = priority;
		this.timeSpent = timeSpent;
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
	
	public int getTimeSpent() {
		return timeSpent;
	}
	
	public void setTimeSpent(int timeSpent) {
		this.timeSpent = timeSpent;
	}


	public String getSummary() {
		return summary;
	}


	public void setSummary(String summary) {
		this.summary = summary;
	}

	public Set<Worklog> getWorklogs() {
		return worklogs;
	}


	public void setWorklogs(Set<Worklog> worklogs) {
		this.worklogs = worklogs;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((key == null) ? 0 : key.hashCode());
		result = prime * result + ((project == null) ? 0 : project.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Issue other = (Issue) obj;
		if (key == null) {
			if (other.getKey() != null)
				return false;
		} else if (!key.equals(other.getKey()))
			return false;
		if (project == null) {
			if (other.getProject() != null)
				return false;
		} else if (!project.equals(other.getProject()))
			return false;
		return true;
	}
	
}