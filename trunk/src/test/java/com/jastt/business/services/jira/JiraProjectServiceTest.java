package com.jastt.business.services.jira;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.atlassian.jira.rest.client.api.domain.BasicProject;
import com.jastt.business.domain.entities.Project;
import com.jastt.business.domain.entities.Server;
import com.jastt.business.domain.entities.User;
import com.jastt.business.services.jira.impl.JiraProjectServiceImpl;
import com.jastt.business.services.jira.impl.client.JiraClient;

public class JiraProjectServiceTest {

	private static final Logger LOG = LoggerFactory.getLogger(JiraProjectServiceTest.class);
	
	private User user;
	private JiraProjectService jiraProjectService = new JiraProjectServiceImpl();
	
	@Before
	public void setUp() throws Exception {
		Properties prop = new Properties();
		
		InputStream in = getClass().getClassLoader().getResourceAsStream("jira_connection.properties");
		if (in == null) throw new FileNotFoundException("Property file not found");
		try {
			prop.load(in);
		} finally {
			in.close();
		}
		
		Server server = new Server();
		server.setUrl(prop.getProperty("jira.url"));
		
		user = new User();
		user.setServer(server);
		user.setLogin(prop.getProperty("username"));
		user.setPassword(prop.getProperty("password"));
	}
	
	@Test
	public void getAllProjects_AllProjectsLoaded() throws JiraClientException {
		final String serverUrl = user.getServer().getUrl();
		final String username = user.getLogin();
		final String password = user.getPassword();

		JiraClient jiraClient = new JiraClient(serverUrl, username, password);
		
		Set<BasicProject> jiraProjectSet = jiraClient.getAllProjects();
		Set<Project> projectSet = jiraProjectService.getAllProjects(user);
		
		for (Project project : projectSet) {
			assertSame(user.getServer(), project.getServer());
		}
		
		Set<String> jiraProjectKeys = new HashSet<String>();
		for (BasicProject jiraProject : jiraProjectSet) {
			jiraProjectKeys.add(jiraProject.getKey());
		}
		Set<String> projectKeys = new HashSet<String>();
		for (Project project : projectSet) {
			projectKeys.add(project.getKey());
		}
		
		assertEquals(jiraProjectKeys, projectKeys);
	}

}
