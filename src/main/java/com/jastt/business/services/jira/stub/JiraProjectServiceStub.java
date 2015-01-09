package com.jastt.business.services.jira.stub;

import java.io.Serializable;
import java.net.UnknownHostException;
import java.util.HashSet;
import java.util.Set;

import com.jastt.business.domain.entities.Project;
import com.jastt.business.domain.entities.User;
import com.jastt.business.services.jira.JiraClientException;
import com.jastt.business.services.jira.JiraProjectService;

import static com.jastt.business.services.jira.stub.JiraStubConstants.*;

public class JiraProjectServiceStub implements JiraProjectService, Serializable {
	private static final long serialVersionUID = 1L;
	
	private Set<Project> projectSet = new HashSet<Project>();
	
	public JiraProjectServiceStub() {
		Project project = new Project(null, PROJECT_KEY, PROJECT_NAME);
		projectSet.add(project);
	}
	
	@Override
	public Set<Project> getAllProjects(User user) throws JiraClientException {
		
		String userServerUrl = user.getServer().getUrl();
		if (!userServerUrl.equals(SERVER_URL)) 
			throw new RuntimeException(new UnknownHostException(userServerUrl));		
		if (!user.getLogin().equals(USERNAME) || !user.getPassword().equals(PASSWORD)) 
			throw new JiraClientException("User is not authenticated", 401);
		
		for (Project project : projectSet) {
			project.setServer(user.getServer());
		}
		
		return projectSet;
	}

}
