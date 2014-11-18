package com.jastt.business.services.jira;

import java.util.Set;

import org.joda.time.DateTime;

import com.jastt.business.domain.entities.Issue;
import com.jastt.business.domain.entities.Project;
import com.jastt.business.domain.entities.User;

public interface JiraIssueService {

	//getAllIssues methods return issues only with assignee and timespent not null  
	Issue getIssueByKey(User user, String issueKey) throws JiraClientException;
	Set<Issue> getAllIssuesForProject(User user, Project project) throws JiraClientException;
	Set<Issue> getAllIssuesForProject(User user, Project project, DateTime fromDate) throws JiraClientException;
}
