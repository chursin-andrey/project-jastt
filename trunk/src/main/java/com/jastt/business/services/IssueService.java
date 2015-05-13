package com.jastt.business.services;

import com.jastt.business.domain.entities.Project;
import com.jastt.business.domain.entities.Issue;
import com.jastt.business.domain.entities.Assignee;
import com.jastt.business.domain.entities.User;
import com.jastt.business.enums.IssueStatusEnum;
import com.jastt.business.enums.IssueTypeEnum;
import com.jastt.business.enums.PredefinedDateEnum;
import com.jastt.business.services.jira.JiraClientException;

import java.util.*;

public interface IssueService {
	public Issue getIssueById(Integer id);
	public List<Issue> getIssuesByProject(Project project);
	public List<Issue> getAllIssues();
	public List<Issue> getIssues(Project project, Collection<IssueStatusEnum> status, List<Assignee> assignees, Collection<IssueTypeEnum> issueType, Date fromDate, Date toDate);
	public List<Issue> getIssues(Project project, Collection<IssueStatusEnum> status, List<Assignee> assignees, Collection<IssueTypeEnum> issueType, PredefinedDateEnum period);
	// FIXME probably out of contract
	public void update(User user) throws JiraClientException;
	
	
}
