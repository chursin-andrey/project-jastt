package com.jastt.business.services.jira.impl;

interface JiraConnectionService {

	JiraConnection getJiraConnection(String serverUrl, String username, String password);
}
