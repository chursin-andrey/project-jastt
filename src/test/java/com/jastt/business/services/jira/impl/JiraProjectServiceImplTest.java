package com.jastt.business.services.jira.impl;

import static org.junit.Assert.*;

import java.net.URI;
import java.net.URISyntaxException;

import org.junit.Test;

import com.atlassian.jira.rest.client.api.domain.BasicProject;
import com.jastt.business.domain.entities.Project;

public class JiraProjectServiceImplTest {

	@Test
	public void convertJiraProjectToProjectEntity() 
			throws URISyntaxException {
		BasicProject jiraProject = new BasicProject(new URI("tmp"), "key", null, "name");
		assertNotNull(jiraProject.getKey());
		assertNotNull(jiraProject.getName());
		
		Project project = JiraProjectServiceImpl.convertJiraProjectToProjectEntity(jiraProject);
		assertEquals(jiraProject.getKey(), project.getKey());
		assertEquals(jiraProject.getName(), project.getName());
	}
	
	@Test
	public void convertJiraProjectToProjectEntity_JiraProjectNameIsNull_ProjectEntityNameIsEmpty() 
			throws URISyntaxException {
		BasicProject jiraProject = new BasicProject(new URI("tmp"), "key", null, null);
		assertNotNull(jiraProject.getKey());
		assertNull(jiraProject.getName());
		
		Project project = JiraProjectServiceImpl.convertJiraProjectToProjectEntity(jiraProject);
		assertEquals(jiraProject.getKey(), project.getKey());
		assertTrue(project.getName().isEmpty());
	}

}
