package com.jastt.business.services.jira;

import java.util.Set;

import com.jastt.business.domain.entities.Project;
import com.jastt.business.domain.entities.User;

public interface JiraProjectService {

	Project getProjectByKey(User user, String projectKey) throws JiraClientException;
	Set<Project> getAllProjects(User user) throws JiraClientException;
}
