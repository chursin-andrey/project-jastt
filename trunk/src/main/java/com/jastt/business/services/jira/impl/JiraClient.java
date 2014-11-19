package com.jastt.business.services.jira.impl;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.atlassian.jira.rest.client.api.JiraRestClient;
import com.atlassian.jira.rest.client.api.JiraRestClientFactory;
import com.atlassian.jira.rest.client.api.ProjectRestClient;
import com.atlassian.jira.rest.client.api.RestClientException;
import com.atlassian.jira.rest.client.api.SearchRestClient;
import com.atlassian.jira.rest.client.api.domain.BasicProject;
import com.atlassian.jira.rest.client.api.domain.Issue;
import com.atlassian.jira.rest.client.api.domain.SearchResult;
import com.atlassian.jira.rest.client.internal.async.AsynchronousJiraRestClientFactory;
import com.jastt.business.services.jira.JiraClientException;

class JiraClient {
	private URI serverUrl;
	private String username;
	private String password;
	
	public JiraClient(URI serverUrl, String username, String password) {
		this.serverUrl = serverUrl;
		this.username = username;
		this.password = password;
	}
	
	public JiraClient(String serverUrl, String username, String password) /*throws JiraClientException*/ {
		try {
			this.serverUrl = new URI(serverUrl);
		} catch (URISyntaxException e) {
			//TODO: throw new JiraClientException(e.getMessage());
		}
		this.username = username;
		this.password = password;
	}
	
	private JiraRestClient createJiraRestClient() {
    	JiraRestClientFactory factory = new AsynchronousJiraRestClientFactory();
    	return factory
    			.createWithBasicHttpAuthentication(serverUrl, username, password);
    }
	
	private void closeJiraRestClient(JiraRestClient jrc) {
		try {
			jrc.close();
		} catch (IOException e) {
			//TODO: Send message in Logger
		}
	}
	
	//TODO
	public void getServerInfo() {
		
	}
	
	public List<BasicProject> getAllProjects() /*throws JiraClientException*/ {
    	JiraRestClient jrc = createJiraRestClient();
    	
    	List<BasicProject> projectList = new ArrayList<BasicProject>();
    	try {
			try {
				ProjectRestClient projectClient = jrc.getProjectClient();
				Iterable<BasicProject> projects = projectClient.getAllProjects().claim();
				for (BasicProject project : projects) projectList.add(project);
			} finally {
				closeJiraRestClient(jrc);
			}
		} catch (RestClientException e) {
			//TODO: throw new JiraClientException(e.getMessage(), e.getStatusCode().orNull());
		}
    	
    	return projectList;
    }
	
	public List<Issue> getAllIssuesByQuery(String jql) /*throws JiraClientException*/ {
		String[] requiredFields = {"project", "summary", "issuetype", "status", "created", "updated", "priority", 
    			"timetracking", "versions", "assignee"};
    	Set<String> fields = new HashSet<String>(Arrays.asList(requiredFields));
		
    	JiraRestClient jrc = createJiraRestClient();
    	
    	List<Issue> issueList = new ArrayList<Issue>();
    	try {
			try {
				SearchRestClient searchClient = jrc.getSearchClient();
	    		int total = 0, startAt = 0;
	    		do {
		    		SearchResult results = searchClient.searchJql(jql, -1, startAt, fields).claim();
		    		total = results.getTotal();
		    		Iterable<Issue> issues = results.getIssues();
		    		for(Issue issue : issues) issueList.add(issue);
		    		startAt += issueList.size();
	    		} while (issueList.size() < total);
			} finally {
				closeJiraRestClient(jrc);
			}
		} catch (RestClientException e) {
			//TODO: throw new JiraClientException(e.getMessage(), e.getStatusCode().orNull());
		}
		
		return issueList;
	}
}
