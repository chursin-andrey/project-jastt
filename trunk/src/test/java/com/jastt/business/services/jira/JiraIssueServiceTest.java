package com.jastt.business.services.jira;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.joda.time.DateTime;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jastt.business.domain.entities.Issue;
import com.jastt.business.domain.entities.Project;
import com.jastt.business.domain.entities.Server;
import com.jastt.business.domain.entities.User;
import com.jastt.business.services.jira.impl.JiraClient;
import com.jastt.business.services.jira.impl.JiraIssueServiceImpl;
import com.jastt.business.services.jira.impl.JiraProjectServiceImpl;

public class JiraIssueServiceTest {

	private static final Logger LOG = LoggerFactory.getLogger(JiraIssueServiceTest.class);
	
	private static User user;
	private static Project projectFound;
	private static Set <Issue> issueSetForProjectFound;
	private static JiraIssueService jiraIssueService = new JiraIssueServiceImpl();
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		user = createUser();
		findProject(user);
	}
	
	@Test
	public void getAllIssuesForProject_AllIssuesInTheSameProject() {
		for (Issue issue : issueSetForProjectFound) {
			assertSame(issue.getProject(), projectFound);
		}
	}
	
	@Test
	public void getAllIssuesForProject_IssueKeysAreUnique () {
		Set<String> issueKeys = new HashSet<String>();
		for (Issue issue : issueSetForProjectFound) {
			assertNotNull(issue.getKey());
			issueKeys.add(issue.getKey());
		}
		assertEquals(issueKeys.size(), issueSetForProjectFound.size());
	}
	
	@Test
	public void getAllIssuesForProject_JiraIssuesAndIssueEntitiesContainTheSameData() 
			throws JiraClientException {
		final String serverUrl = user.getServer().getUrl();
		final String username = user.getLogin();
		final String password = user.getPassword();
		
		JiraClient jiraClient = new JiraClient(serverUrl, username, password);		
		String jql = String.format("project = \"%s\" and timeSpent > 0", 
				projectFound.getKey());
		Set<com.atlassian.jira.rest.client.api.domain.Issue> jiraIssueSet =
				jiraClient.getAllIssuesByQuery(jql);
		
		assertEquals(jiraIssueSet.size(), issueSetForProjectFound.size());
		
		Map<String, com.atlassian.jira.rest.client.api.domain.Issue> jiraIssueMap = 
				new HashMap<String, com.atlassian.jira.rest.client.api.domain.Issue>();
		for (com.atlassian.jira.rest.client.api.domain.Issue jiraIssue : jiraIssueSet) {
			jiraIssueMap.put(jiraIssue.getKey(), jiraIssue);
		}
		
		for (Issue issue : issueSetForProjectFound) {
			compareIssues(jiraIssueMap.get(issue.getKey()), issue);
		}
	}
	
	@Test
	public void getAllIssuesForProjectFromDate() throws JiraClientException {
		List<Date> issueUpdateDateList = new ArrayList<Date>();
		for (Issue issue : issueSetForProjectFound) {
			issueUpdateDateList.add(issue.getUpdated());
		}		
		Collections.sort(issueUpdateDateList);

		Date fromDate = issueUpdateDateList.get(issueUpdateDateList.size()/2);
		
		Set<Issue> issueSet1 = new HashSet<Issue>();
		for (Issue issue : issueSetForProjectFound) {
			if (issue.getUpdated().after(fromDate)) issueSet1.add(issue);
		}
		
		Set<Issue> issueSet2 = jiraIssueService.getAllIssuesForProject(user, projectFound, new DateTime(fromDate));
		assertEquals(issueSet1, issueSet2);
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
	
	private static void findProject(User user) throws Exception {
		final int minNumberOfIssues = 10;
		
		JiraProjectService jiraProjectService = new JiraProjectServiceImpl();
		Set<Project> projectSet = jiraProjectService.getAllProjects(user);
		
		LOG.info("Search for a project having issues with timespent > 0");
		for (Project project : projectSet) {
			LOG.trace("Test project: " + project.getKey());
			Set<Issue> issueSet = jiraIssueService.getAllIssuesForProject(user, project);
			if (issueSet.size() > minNumberOfIssues) {
				projectFound = project;
				issueSetForProjectFound = issueSet;
				LOG.info(String.format("Project found: %s, Number of issues: %d", 
						projectFound.getKey(), issueSetForProjectFound.size()));
				return;
			}
		}
		throw new Exception("No project found");
	}
	
	private void compareIssues(com.atlassian.jira.rest.client.api.domain.Issue jiraIssue, Issue issue) {
		//Mandatory fields
		assertEquals(jiraIssue.getKey(), issue.getKey());
		assertEquals(jiraIssue.getProject().getKey(), issue.getProject().getKey());
		assertEquals(jiraIssue.getSummary(), issue.getSummary());
		assertEquals(jiraIssue.getIssueType().getName(), issue.getIssueType());
		assertEquals(jiraIssue.getStatus().getName(), issue.getStatus());
		assertEquals(jiraIssue.getCreationDate().toDate(), issue.getCreated());
		assertEquals(jiraIssue.getUpdateDate().toDate(), issue.getUpdated());
		//Optional fields
		if (jiraIssue.getAssignee() != null) {
			assertEquals(jiraIssue.getAssignee().getDisplayName(), issue.getAssignee().getName());
			assertEquals(jiraIssue.getAssignee().getEmailAddress(), issue.getAssignee().getEmail());
		} else {
//			LOG.info("Issue {} has no ASSIGNEE", issue.getKey());
			assertEquals("ABSENT", issue.getAssignee().getName());
			assertEquals("NoEmail", issue.getAssignee().getEmail());
		}
		
		assertEquals(jiraIssue.getTimeTracking().getTimeSpentMinutes().intValue(), issue.getTimeSpent());
		
		if (jiraIssue.getPriority() != null) {
			assertEquals(jiraIssue.getPriority().getName(), issue.getPriority());
		} else {
			assertNull(issue.getPriority());
		}
	}

}
