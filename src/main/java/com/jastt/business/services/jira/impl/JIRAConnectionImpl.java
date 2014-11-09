package com.jastt.business.services.jira.impl;

import java.io.IOException;

import com.atlassian.jira.rest.client.api.JiraRestClient;

class JIRAConnectionImpl implements JIRAConnection {

	protected JiraRestClient jrc = null;
	
	@Override
	public void close() throws IOException {
		

	}

	@Override
	public void open(String serverURL, String user, String password) {
		

	}

}
