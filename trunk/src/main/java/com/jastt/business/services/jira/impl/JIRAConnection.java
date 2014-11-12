package com.jastt.business.services.jira.impl;

import java.io.Closeable;
import java.io.IOException;

import com.atlassian.jira.rest.client.api.JiraRestClient;
import com.jastt.business.services.jira.JiraClientException;

class JIRAConnection implements Closeable {

	protected JiraRestClient restClient = null;
	
	public JIRAConnection(JiraRestClient jrc) {
		this.restClient = jrc;
	}
	
	@Override
	public void close() {
		try {
			if (restClient != null) restClient.close();
		} catch (IOException e) {
			throw new JiraClientException("Can't close JIRA server connection: " + e.getMessage());
		}
	}

}
