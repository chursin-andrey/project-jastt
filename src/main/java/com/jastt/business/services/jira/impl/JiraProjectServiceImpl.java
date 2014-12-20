package com.jastt.business.services.jira.impl;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.atlassian.jira.rest.client.api.domain.BasicProject;
import com.jastt.business.domain.entities.Project;
import com.jastt.business.domain.entities.User;
import com.jastt.business.services.jira.JiraClientException;
import com.jastt.business.services.jira.JiraProjectService;
import com.jastt.business.services.jira.impl.client.JiraClient;

//@Service("jiraProjectService")
public class JiraProjectServiceImpl implements JiraProjectService {

	@Override
	public Set<Project> getAllProjects(User user) throws JiraClientException {    	
    	Set<Project> projectSet = new HashSet<Project>(); 
		
		JiraClient jc = new JiraClient(user.getServer().getUrl(), 
				user.getLogin(), user.getPassword());
		Set<BasicProject> jiraProjectSet = jc.getAllProjects();
		for (BasicProject jiraProject : jiraProjectSet) {
			Project project = convertJiraProjectToProjectEntity(jiraProject);			
			project.setServer(user.getServer());
			projectSet.add(project);
		}
		
		return projectSet;
	}
	
	static Project convertJiraProjectToProjectEntity(BasicProject jiraProject) {
		Project project = new Project();
		
		project.setKey(jiraProject.getKey());
		project.setName(StringUtils.defaultString(jiraProject.getName()));
		
		return project;
	}

}
