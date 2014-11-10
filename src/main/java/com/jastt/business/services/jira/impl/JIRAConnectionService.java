package com.jastt.business.services.jira.impl;

interface JIRAConnectionService {

	JIRAConnection getJiraConnection(String serverUrl, String username, String password);
}
