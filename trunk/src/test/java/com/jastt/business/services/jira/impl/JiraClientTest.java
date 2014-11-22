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
	public void testGettingConnectionToJiraServer() {
		ServerInfo info = jc.getServerInfo();
		assertNotNull("serverInfo must be not null", info);
		assertEquals(serverUrl, info.getBaseUri().toString());
	}
	
	@Test
	public void getAllProjects_MandatoryFieldsAreNotNull() {
		Set<BasicProject> projectSet = jc.getAllProjects();
		
		LOG.info(String.format("Found %d projects", projectSet.size()));
		
		for (BasicProject project : projectSet) {
			assertNotNull("projectKey must be not null", project.getKey());
		}
	}
	
	@Test
	public void getTotalNumberOfIssuesForQuery() {
		Set<BasicProject> projectSet = jc.getAllProjects();
		if (projectSet.size() == 0) {
			LOG.info("No projects found");
			fail();
		}
		
		BasicProject someProject = projectSet.iterator().next();
		
		String jql = String.format("project = %s", someProject.getKey());
		int total = jc.getTotalNumberOfIssuesForQuery(jql);
		LOG.info(String.format("Project: %s, Total number of issues: %d", someProject.getKey(), total));
	}
	
	@Test
	public void getAllIssuesByQuery_IssuesArePaginated_AllIssuesLoaded() {
		final int MAX_ISSUES_NUM = 1;
		
		Set<BasicProject> projectSet = jc.getAllProjects();
		
		BasicProject someProject = null; 
		int total = 0;
		//search for a project with total number of issues > MAX_ISSUES_NUM
		for (BasicProject project : projectSet) {
			LOG.trace("Test project: " + project.getKey());
			String jql = String.format("project = %s", project.getKey());
			total = jc.getTotalNumberOfIssuesForQuery(jql);
			if (total > MAX_ISSUES_NUM) {someProject = project; break;}
		}
		if (someProject == null) {
			LOG.info(String.format("No project with total number of issues > %d found", MAX_ISSUES_NUM));
			fail();
		}		
		LOG.info("Project found: " + someProject.getKey());
		
		int maxResults = total/5 + 1;
		String jql = String.format("project = %s", someProject.getKey());
		Set<Issue> issueSet = jc.getAllIssuesByQuery(jql, maxResults);
		assertEquals(total, issueSet.size());
	}
	
	@Test
	public void getAllIssuesByQuery_MandatoryFieldsAreNotNull() {
		Set<BasicProject> projectSet = jc.getAllProjects();
		
		BasicProject someProject = null; 
		//search for a project with at least one issue
		for (BasicProject project : projectSet) {
			LOG.trace("Test project: " + project.getKey());
			String jql = String.format("project = %s", project.getKey());
			int total = jc.getTotalNumberOfIssuesForQuery(jql);
			if (total > 0) {someProject = project; break;}
		}
		if (someProject == null) {
			LOG.info(String.format("No project with at least one issue found"));
			fail();
		}		
		LOG.info("Project found: " + someProject.getKey());
		
		String jql = String.format("project = %s", someProject.getKey());
		Set<Issue> issueSet = jc.getAllIssuesByQuery(jql, -1);
		for (Issue issue : issueSet) {
			assertNotNull("issueKey must be not null", issue.getKey());
			assertNotNull("issue must belong to a project", issue.getProject());
			assertNotNull("issueSummary must be not null", issue.getSummary());
			assertNotNull("issueType must be not null", issue.getIssueType().getName());
			assertNotNull("issueStatus must be not null", issue.getStatus().getName());
			assertNotNull("issueCreationDate must be not null", issue.getCreationDate());
			assertNotNull("issueUpdateDate must be not null", issue.getUpdateDate());
		}
	}
	

}
