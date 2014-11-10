package com.jastt.business.services.jira.impl;

interface JIRAConnectionService {

	JIRAConnection getJIRAConnection(String serverURL, String user, String password);
}
