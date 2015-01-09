package com.jastt.business.services.jira.stub;

import static org.junit.Assert.*;

import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import com.jastt.business.domain.entities.Project;
import com.jastt.business.domain.entities.Server;
import com.jastt.business.domain.entities.User;
import com.jastt.business.services.jira.JiraClientException;
import com.jastt.business.services.jira.JiraProjectService;

public class JiraProjectServiceStubTest {

	private User user;
	private JiraProjectService jiraProjectService = new JiraProjectServiceStub();
	
	@Before
	public void setUp() throws Exception {
		Server server = new Server();
		server.setUrl(JiraStubConstants.SERVER_URL);
		
		user = new User();
		user.setServer(server);
		user.setLogin(JiraStubConstants.USERNAME);
		user.setPassword(JiraStubConstants.PASSWORD);
	}

	@Test
	public void testGetAllProjects_AllLoadedSuccessfully() throws JiraClientException {
		Set<Project> projectSet = jiraProjectService.getAllProjects(user);
		
		assertEquals(1, projectSet.size());
		Project project = projectSet.iterator().next();
		assertEquals(JiraStubConstants.PROJECT_KEY, project.getKey());
		assertEquals(JiraStubConstants.PROJECT_NAME, project.getName());
		assertSame(user.getServer(), project.getServer());
	}
	
	@Test(expected = RuntimeException.class)
	public void testGetAllProjects_InvalidServerUrl() throws JiraClientException {
		String oldUrl = user.getServer().getUrl();
		user.getServer().setUrl(oldUrl + " ");
		jiraProjectService.getAllProjects(user);
	}
	
	@Test
	public void testGetAllProjects_InvalidUser() throws JiraClientException {
		String oldLogin = user.getLogin();
		user.setLogin(oldLogin + " ");
		try {
			jiraProjectService.getAllProjects(user);
		} catch (JiraClientException ex) {
			assertEquals(Integer.valueOf(401), ex.getStatusCode());
			return;
		}
		fail();
	}
}
