package com.jastt.dal.providers.worklog;

import java.util.Date;
import java.util.List;
import java.util.Set;

import com.jastt.business.domain.entities.Project;

public class WorklogSearchOptions {

	private Project project;
	private List<String> authors;
	private Set<String> issueType;
	private Set<String> issueStatus;
	private Date fromDate;
	private Date toDate;

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

	public Set<String> getIssueType() {
		return issueType;
	}

	public void setIssueType(Set<String> issueType) {
		this.issueType = issueType;
	}

	public Set<String> getIssueStatus() {
		return issueStatus;
	}

	public void setIssueStatus(Set<String> issueStatus) {
		this.issueStatus = issueStatus;
	}

	public Date getFromDate() {
		return fromDate;
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	public Date getToDate() {
		return toDate;
	}

	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}
}
