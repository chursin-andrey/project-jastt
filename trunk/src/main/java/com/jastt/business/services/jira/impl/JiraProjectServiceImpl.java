package com.jastt.business.services.jira.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.atlassian.jira.rest.client.api.domain.BasicProject;
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
    	Set<Project> projectSet = new HashSet<Project>(); 
		
		JiraClient jc = new JiraClient(user.getServer().getUrl(), 
				user.getLogin(), user.getPassword());
		List<BasicProject> projectList = jc.getAllProjects();
		for (BasicProject project : projectList) {
			Project pr = new Project();
			
			pr.setServer(user.getServer());
			pr.setKey(project.getKey());
			if (project.getName() == null) pr.setName("") ; else pr.setName(project.getName()); 
			
			projectSet.add(pr);
		}
		
		return null;//return projectSet;
	}

}
