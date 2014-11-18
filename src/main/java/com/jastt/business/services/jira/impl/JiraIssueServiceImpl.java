package com.jastt.business.services.jira.impl;

import java.util.Set;

import org.joda.time.DateTime;
import org.springframework.stereotype.Service;

import com.jastt.business.domain.entities.Issue;
import com.jastt.business.domain.entities.Project;
import com.jastt.business.domain.entities.User;
import com.jastt.business.services.jira.JiraClientException;
import com.jastt.business.services.jira.JiraIssueService;

@Service("jiraIssueService")
public class JiraIssueServiceImpl implements JiraIssueService {

	@Override
	public Issue getIssueByKey(User user, String issueKey) throws JiraClientException {
		
		return null;
	}

	@Override
	public Set<Issue> getAllIssuesForProject(User user, Project project) throws JiraClientException {
		
		return null;
	}

	@Override
	public Set<Issue> getAllIssuesForProject(User user, Project project,
			DateTime fromDate) throws JiraClientException {
		
		return null;
	}

}
