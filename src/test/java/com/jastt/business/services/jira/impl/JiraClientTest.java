package com.jastt.business.services.jira.impl;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.atlassian.jira.rest.client.api.domain.BasicProject;
import com.atlassian.jira.rest.client.api.domain.Issue;
import com.atlassian.jira.rest.client.api.domain.ServerInfo;
import com.jastt.business.services.jira.JiraClientException;
import com.jastt.business.services.jira.impl.JiraClient;

public class JiraClientTest {

	private static final Logger LOG = LoggerFactory.getLogger(JiraClientTest.class);
	
	private String serverUrl;
	private String username;
	private String password;
	
	private JiraClient jc;
	
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
		
		serverUrl = prop.getProperty("jira.url");
		username = prop.getProperty("username");
		password = prop.getProperty("password");

		jc = new JiraClient(serverUrl, username, password);
	}

	@Test
	public void getConnectionToJiraServer() throws JiraClientException {
		ServerInfo info = jc.getServerInfo();
		assertEquals(serverUrl, info.getBaseUri().toString());
	}
	
	@Test
	public void getAllProjects() throws JiraClientException {
		Set<BasicProject> projectSet = jc.getAllProjects();
		
		LOG.info(String.format("Found %d projects on current JIRA server", projectSet.size()));
	}
	
	@Test
	public void getTotalNumberOfIssuesForQuery() throws JiraClientException {
		Set<BasicProject> projectSet = jc.getAllProjects();
		if (projectSet.isEmpty()) {
			LOG.info("No project found");
			fail();
		}
		
		BasicProject someProject = projectSet.iterator().next();
		
		String jql = String.format("project = %s", someProject.getKey());
		int total = jc.getTotalNumberOfIssuesForQuery(jql);
		LOG.info(String.format("Project: %s, Total number of issues: %d", someProject.getKey(), total));
	}
	
	@Test
	public void getAllIssuesByQuery_IssuesArePaginated_AllIssuesLoaded() throws JiraClientException {
		final int minNumberOfIssues = 1;
		
		Set<BasicProject> projectSet = jc.getAllProjects();
		
		BasicProject someProject = null; 
		int total = 0;
		
		LOG.info(String.format("Search for a project with total number of issues > %d", minNumberOfIssues));
		for (BasicProject project : projectSet) {
			LOG.trace("Test project: " + project.getKey());
			String jql = String.format("project = %s", project.getKey());
			total = jc.getTotalNumberOfIssuesForQuery(jql);
			if (total > minNumberOfIssues) { someProject = project; break; }
		}
		if (someProject == null) {
			LOG.info(String.format("No project found"));
			fail();
		}
		LOG.info(String.format("Project found: %s, Total number of issues: %d", someProject.getKey(), total));
		
		int pageSize = total/5 + 1;
		String jql = String.format("project = %s", someProject.getKey());
		Set<Issue> issueSet = jc.getAllIssuesByQuery(jql, pageSize);
		assertEquals(total, issueSet.size());
	}
	
}
