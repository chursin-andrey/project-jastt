package com.jastt.business.services.jira.impl;

import org.springframework.stereotype.Service;

import com.jastt.business.domain.entities.Project;
import com.jastt.business.domain.entities.User;
import com.jastt.business.services.jira.JIRAProjectService;

@Service("jiraProjectService")
public class JIRAProjectServiceImpl implements JIRAProjectService {

	@Override
	public Project getProjectByKey(User user, String projectKey) {
		
		return null;
	}

	@Override
	public Iterable<Project> getAllProjects(User user) {
		
		return null;
	}

}
