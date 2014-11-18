package com.jastt.business.services.jira.impl;

interface JIRAConnectionService {

	JiraConnection getJiraConnection(String serverUrl, String username, String password);
}
