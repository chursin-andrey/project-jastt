package com.jastt.business.services.impl;

import com.jastt.business.domain.entities.Issue;

public class IssueReportBean {
	private String assigneeName;
	private String projectName;
	private String issueType;
	private int issueTimeSpent;

	public IssueReportBean() {
		
	}
	
	public IssueReportBean(Issue issue) {
		this.assigneeName = issue.getAssignee().getName();
		this.projectName = issue.getProject().getName();
		this.issueType = issue.getIssueType();
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

	public String getIssueType() {
		return issueType;
	}

	public void setIssueType(String issueType) {
		this.issueType = issueType;
	}

	public int getIssueTimeSpent() {
		return issueTimeSpent;
	}

	public void setIssueTimeSpent(int issueTimeSpent) {
		this.issueTimeSpent = issueTimeSpent;
	}
}
