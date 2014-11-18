package com.jastt.business.services.jira.impl;

import java.io.Closeable;
import java.io.IOException;

import com.atlassian.jira.rest.client.api.IssueRestClient;
import com.atlassian.jira.rest.client.api.JiraRestClient;
import com.atlassian.jira.rest.client.api.ProjectRestClient;

class JiraConnection implements Closeable {

	protected JiraRestClient restClient = null;
	
	public JiraConnection(JiraRestClient jrc) {
		this.restClient = jrc;
	}
	
	public ProjectRestClient getProjectClient() {
		if (restClient != null) return restClient.getProjectClient();
		return null;
	}
	
	public IssueRestClient getIssueClient() {
		if (restClient != null) return restClient.getIssueClient();
		return null;
	}
	
	@Override
	public void close() throws IOException {
		if (restClient != null) restClient.close();
	}

}
