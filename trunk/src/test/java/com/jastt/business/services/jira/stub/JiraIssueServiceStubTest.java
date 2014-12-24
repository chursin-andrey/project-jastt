package com.jastt.business.services.jira.stub;

import static com.jastt.business.services.jira.stub.JiraStubConstants.*;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

import com.jastt.business.domain.entities.Issue;
import com.jastt.business.domain.entities.Project;
import com.jastt.business.domain.entities.Server;
import com.jastt.business.domain.entities.User;
import com.jastt.business.services.jira.JiraClientException;
import com.jastt.business.services.jira.JiraIssueService;

public class JiraIssueServiceStubTest {

	private User user;
	private Project project;
	private JiraIssueService jiraIssueService = new JiraIssueServiceStub();
	
	@Before
	public void setUp() throws Exception {
		Server server = new Server();
		server.setUrl(SERVER_URL);
		
		user = new User();
		user.setServer(server);
		user.setLogin(USERNAME);
		user.setPassword(PASSWORD);
		
		project = new Project(server, PROJECT_KEY, PROJECT_NAME);
	}

	@Test
	public void testGetAllIssuesForProject_AllLoadedSuccessfully() throws JiraClientException {
		Set<Issue> issueSet = jiraIssueService.getAllIssuesForProject(user, project);
		assertEquals(ISSUE_COUNT, issueSet.size());
		
		for (Issue issue : issueSet) {
			assertNotNull(issue.getKey());
			assertNotNull(issue.getSummary());
			assertNotNull(issue.getIssueType());
			assertNotNull(issue.getStatus());
			assertNotNull(issue.getCreated());
			assertNotNull(issue.getUpdated());
			assertTrue(issue.getUpdated().compareTo(issue.getCreated()) >= 0);
			assertNotNull(issue.getPriority());
			assertTrue(issue.getTimeSpent() > 0);
			assertNotNull(issue.getAssignee().getName());
			assertNotNull(issue.getAssignee().getEmail());
			assertSame(project, issue.getProject());
		}
	}
	
	@Test(expected = RuntimeException.class)
	public void testGetAllIssuesForProject_InvalidServerUrl() throws JiraClientException {
		String oldUrl = user.getServer().getUrl();
		user.getServer().setUrl(oldUrl + " ");
		jiraIssueService.getAllIssuesForProject(user, project);
	}
	
	@Test
	public void testGetAllIssuesForProject_InvalidUser() throws JiraClientException {
		String oldLogin = user.getLogin();
		user.setLogin(oldLogin + " ");
		try {
			jiraIssueService.getAllIssuesForProject(user, project);
		} catch (JiraClientException ex) {
			assertEquals(Integer.valueOf(401), ex.getStatusCode());
			return;
		}
		fail();
	}
	
	@Test
	public void testGetAllIssuesForProject_InvalidProject() throws JiraClientException {
		String oldKey = project.getKey();
		project.setKey(oldKey + " ");
		try {
			jiraIssueService.getAllIssuesForProject(user, project);
		} catch (JiraClientException ex) {
			assertEquals(Integer.valueOf(400), ex.getStatusCode());
			return;
		}
		fail();
	}
	
	@Test
	public void testGetAllIssuesForProjectFromDate() throws JiraClientException {
		Set<Issue> issueSet = jiraIssueService.getAllIssuesForProject(user, project);
		
		List<Date> issueUpdateDateList = new ArrayList<Date>();
		for (Issue issue : issueSet) {
			issueUpdateDateList.add(issue.getUpdated());
		}		
		Collections.sort(issueUpdateDateList);
		
		Date fromDate = issueUpdateDateList.get(issueUpdateDateList.size()/2);
		
		Set<Issue> issueSet1 = new HashSet<Issue>();
		for (Issue issue : issueSet) {
			if (issue.getUpdated().after(fromDate)) issueSet1.add(issue);
		}
		
		Project project1 = new Project(project.getServer(), PROJECT_KEY, PROJECT_NAME);
		Set<Issue> issueSet2 = jiraIssueService.getAllIssuesForProject(user, project1, new DateTime(fromDate));
		for (Issue issue : issueSet2) {
			assertSame(project1, issue.getProject());
		}
		assertEquals(issueSet1, issueSet2);
	}
}