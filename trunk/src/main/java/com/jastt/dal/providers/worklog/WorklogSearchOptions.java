package com.jastt.dal.providers.worklog;

import java.util.List;

import com.jastt.business.domain.entities.Project;

public class WorklogSearchOptions {

	private Project project;
	private List<String> authors;
	private String issueType;
	private String issueStatus;

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}
	
	public List<String> getAuthors() {
		return authors;
	}

	public void setAuthors(List<String> authors) {
		this.authors = authors;
	}

	public String getIssueType() {
		return issueType;
	}

	public void setIssueType(String issueType) {
		this.issueType = issueType;
	}

	public String getIssueStatus() {
		return issueStatus;
	}

	public void setIssueStatus(String issueStatus) {
		this.issueStatus = issueStatus;
	}
}
