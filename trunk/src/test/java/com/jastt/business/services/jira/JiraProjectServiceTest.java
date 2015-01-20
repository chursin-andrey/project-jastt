package com.jastt.business.services.jira;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.atlassian.jira.rest.client.api.domain.BasicProject;
import com.jastt.business.domain.entities.Project;
import com.jastt.business.domain.entities.Server;
import com.jastt.business.domain.entities.User;
import com.jastt.business.services.jira.impl.JiraClient;
import com.jastt.business.services.jira.impl.JiraProjectServiceImpl;

public class JiraProjectServiceTest {

	private static final Logger LOG = LoggerFactory.getLogger(JiraProjectServiceTest.class);
	
	private static User user;
	private static Set<Project> projectSet;
	private static JiraProjectService jiraProjectService = new JiraProjectServiceImpl();
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		user = createUser();
		projectSet = jiraProjectService.getAllProjects(user);
		if (projectSet.isEmpty()) throw new Exception("No project found");
		LOG.info(String.format("Found %d projects on current JIRA server", projectSet.size()));
	}
	
	@Test
	public void getAllProjects_AllProjectsInTheSameServer() {
		for (Project project : projectSet) {
			assertSame(user.getServer(), project.getServer());
		}
	}
	
	@Test
	public void getAllProjects_ProjectKeysAreUnique() {
		Set<String> projectKeys = new HashSet<String>();
		for (Project project : projectSet) {
			assertNotNull(project.getKey());
			projectKeys.add(project.getKey());
		}
		assertEquals(projectKeys.size(), projectSet.size());
	}
	
	@Test
	public void getAllProjects_JiraProjectsAndProjectEntitiesContainTheSameData() throws JiraClientException {
		final String serverUrl = user.getServer().getUrl();
		final String username = user.getLogin();
		final String password = user.getPassword();
		
		JiraClient jiraClient = new JiraClient(serverUrl, username, password);		
		Set<BasicProject> jiraProjectSet = jiraClient.getAllProjects();
		
		assertEquals(jiraProjectSet.size(), projectSet.size());
		
		Map<String, BasicProject> jiraProjectMap = new HashMap<String, BasicProject>();
		for (BasicProject jiraProject : jiraProjectSet) {
			jiraProjectMap.put(jiraProject.getKey(), jiraProject);
		}
		
		for (Project project : projectSet) {
			compareProjects(jiraProjectMap.get(project.getKey()), project);
		}
	}
	
	private static User createUser() throws Exception {
		Properties prop = new Properties();
		
		InputStream in = JiraIssueServiceTest.class
				.getClassLoader().getResourceAsStream("jira_connection.properties");
		if (in == null) throw new FileNotFoundException("Property file not found");
		try {
			prop.load(in);
		} finally {
			in.close();
		}
		
		Server server = new Server();
		server.setUrl(prop.getProperty("jira.url"));
		
		User user = new User();
		user.setServer(server);
		user.setLogin(prop.getProperty("username"));
		user.setPassword(prop.getProperty("password"));
		return user;
	}
	
	private void compareProjects(BasicProject jiraProject, Project project) {
		assertEquals(jiraProject.getKey(), project.getKey());
		if (jiraProject.getName() != null) {
			assertEquals(jiraProject.getName(), project.getName());
		} else {
			assertTrue(project.getName().isEmpty());
		}
	}

}
