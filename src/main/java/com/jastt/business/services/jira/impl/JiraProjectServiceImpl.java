package com.jastt.business.services.jira.impl;

import java.util.Set;

import org.springframework.stereotype.Service;

import com.jastt.business.domain.entities.Project;
import com.jastt.business.domain.entities.User;
import com.jastt.business.services.jira.JiraClientException;
import com.jastt.business.services.jira.JiraProjectService;

@Service("jiraProjectService")
public class JiraProjectServiceImpl implements JiraProjectService {

	@Override
	public Project getProjectByKey(User user, String projectKey) throws JiraClientException {
		
		return null;
	}

	@Override
	public Set<Project> getAllProjects(User user) throws JiraClientException {
		
		return null;
	}

}
