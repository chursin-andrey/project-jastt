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
	public List<Issue> getAllIssues();
	public List<Issue> getIssues(Project project, IssueStatusEnum status, List<Assignee> assignees, IssueTypeEnum issueType, Date fromDate, Date toDate);
	public List<Issue> getIssues(Project project, IssueStatusEnum status, List<Assignee> assignees, IssueTypeEnum issueType, PredefinedDateEnum period);
	public void update(User user) throws JiraClientException;
	
	
}
