package com.jastt.business.services.jira.impl;

import java.util.Date;

import org.springframework.stereotype.Service;

import com.jastt.business.domain.entities.Issue;
import com.jastt.business.domain.entities.Project;
import com.jastt.business.domain.entities.User;
import com.jastt.business.services.jira.JIRAIssueService;

@Service("jiraIssueService")
public class JIRAIssueServiceImpl implements JIRAIssueService {

	@Override
	public Issue getIssueByKey(User user, String issueKey) {
		
		return null;
	}

	@Override
	public Iterable<Issue> getAllIssuesForProject(User user, Project project) {
		
		return null;
	}

	@Override
	public Iterable<Issue> getAllIssuesForProject(User user, Project project,
			Date fromDate) {
		
		return null;
	}

}
