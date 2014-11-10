package com.jastt.business.services.jira.impl;

import java.io.Closeable;
import java.io.IOException;

import com.atlassian.jira.rest.client.api.JiraRestClient;

class JiraConnection implements Closeable {

	protected JiraRestClient restClient = null;
	
	@Override
	public void close() throws IOException {
		

	}

}
