package com.jastt.business.services.jira;

import java.util.Date;

import com.jastt.business.domain.entities.Issue;
import com.jastt.business.domain.entities.Project;
import com.jastt.business.domain.entities.User;

public interface JiraIssueService {

	//getAllIssues methods return issues only with assignee and timespent not null  
	Issue getIssueByKey(User user, String issueKey);
	Iterable<Issue> getAllIssuesForProject(User user, Project project);
	Iterable<Issue> getAllIssuesForProject(User user, Project project, Date fromDate);
}
