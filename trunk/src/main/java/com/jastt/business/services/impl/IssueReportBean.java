package com.jastt.business.services.impl;

import com.jastt.business.domain.entities.Issue;

public class IssueReportBean {
	private String assigneeName;
	private String projectName;
	private String issueKey;
	private int issueTimeSpent;

	public IssueReportBean() {
		
	}
	
	public IssueReportBean(Issue issue) {
		this.assigneeName = issue.getAssignee().getName();
		this.projectName = issue.getProject().getName();
		this.issueKey = issue.getKey();
		this.issueTimeSpent = issue.getTimeSpent();
	}

	public String getAssigneeName() {
		return assigneeName;
	}

	public void setAssigneeName(String assigneeName) {
		this.assigneeName = assigneeName;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getIssueKey() {
		return issueKey;
	}

	public void setIssueKey(String issueKey) {
		this.issueKey = issueKey;
	}

	public int getIssueTimeSpent() {
		return issueTimeSpent;
	}

	public void setIssueTimeSpent(int issueTimeSpent) {
		this.issueTimeSpent = issueTimeSpent;
	}
}
