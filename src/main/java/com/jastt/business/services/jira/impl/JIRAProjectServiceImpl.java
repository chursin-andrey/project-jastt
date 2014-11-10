package com.jastt.business.services.jira.impl;

import org.springframework.stereotype.Service;

import com.jastt.business.domain.entities.Project;
import com.jastt.business.domain.entities.User;
import com.jastt.business.services.jira.JiraProjectService;

@Service("jiraProjectService")
public class JiraProjectServiceImpl implements JiraProjectService {

	@Override
	public Project getProjectByKey(User user, String projectKey) {
		
		return null;
	}

	@Override
	public Iterable<Project> getAllProjects(User user) {
		
		return null;
	}

}
