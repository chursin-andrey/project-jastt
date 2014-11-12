package com.jastt.business.services.jira.impl;

import java.net.URI;

import com.atlassian.jira.rest.client.api.JiraRestClient;
import com.atlassian.jira.rest.client.api.JiraRestClientFactory;
import com.atlassian.jira.rest.client.internal.async.AsynchronousJiraRestClientFactory;

class JIRAConnectionServiceImpl implements JIRAConnectionService {

	@Override
	public JIRAConnection getJiraConnection(String serverUrl, String username,
			String password) {
		
		//TODO: verify serverUrl
		URI jiraURL = URI.create(serverUrl);
		
		JiraRestClientFactory factory = new AsynchronousJiraRestClientFactory();
		JiraRestClient jrc = factory.createWithBasicHttpAuthentication(jiraURL, username, password);
		
		return new JIRAConnection(jrc);
	}

}