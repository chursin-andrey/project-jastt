package com.jastt.business.services.jira;

import com.jastt.business.domain.entities.Project;
import com.jastt.business.domain.entities.User;

public interface JiraProjectService {

	Project getProjectByKey(User user, String projectKey);
	Iterable<Project> getAllProjects(User user);
}
