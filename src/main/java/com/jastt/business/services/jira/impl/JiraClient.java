package com.jastt.business.services.jira.impl;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.atlassian.jira.rest.client.api.JiraRestClient;
import com.atlassian.jira.rest.client.api.JiraRestClientFactory;
import com.atlassian.jira.rest.client.api.MetadataRestClient;
import com.atlassian.jira.rest.client.api.ProjectRestClient;
import com.atlassian.jira.rest.client.api.RestClientException;
import com.atlassian.jira.rest.client.api.SearchRestClient;
import com.atlassian.jira.rest.client.api.domain.BasicProject;
import com.atlassian.jira.rest.client.api.domain.Issue;
import com.atlassian.jira.rest.client.api.domain.SearchResult;
import com.atlassian.jira.rest.client.api.domain.ServerInfo;
import com.atlassian.jira.rest.client.internal.async.AsynchronousJiraRestClientFactory;
import com.jastt.business.services.jira.JiraClientException;

class JiraClient {
	
	private static final Logger LOG = LoggerFactory.getLogger(JiraClient.class);
	
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
	//TODO: throws JiraClientException
	public ServerInfo getServerInfo() {
		JiraRestClient jrc = createJiraRestClient();
		
		ServerInfo info = null;
		try {
			MetadataRestClient metaClient = jrc.getMetadataClient();
			info = metaClient.getServerInfo().claim();
		} finally {
			closeJiraRestClient(jrc);
		}
		
		return info;
	}
	
	public Set<BasicProject> getAllProjects() /*throws JiraClientException*/ {
    	JiraRestClient jrc = createJiraRestClient();
    	
    	Set<BasicProject> projectSet = new HashSet<BasicProject>();
    	try {
			try {
				ProjectRestClient projectClient = jrc.getProjectClient();
				Iterable<BasicProject> projects = projectClient.getAllProjects().claim();
				for (BasicProject project : projects) projectSet.add(project);
			} finally {
				closeJiraRestClient(jrc);
			}
		} catch (RestClientException e) {
			//TODO: throw new JiraClientException(e.getMessage(), e.getStatusCode().orNull());
		}
    	
    	return projectSet;
    }
	//TODO: throws JiraClientException 
	public int getTotalNumberOfIssuesForQuery(final String jql) {
		JiraRestClient jrc = createJiraRestClient();
		
		int total = 0;
		try {
    		SearchRestClient searchClient = jrc.getSearchClient();
    		SearchResult results = searchClient.searchJql(jql, 0, null, null).claim();
    		total = results.getTotal();
    	} finally {
    		closeJiraRestClient(jrc);
    	}
		
		return total;
	}
	
	public Set<Issue> getAllIssuesByQuery(final String jql, final Integer maxResults) /*throws JiraClientException*/ {
		String[] requiredFields = {"project", "summary", "issuetype", "status", "created", "updated", "priority", 
    			"timetracking", "versions", "assignee"};
    	Set<String> fields = new HashSet<String>(Arrays.asList(requiredFields));
		
    	JiraRestClient jrc = createJiraRestClient();
    	
    	Set<Issue> issueSet = new HashSet<Issue>();
    	try {
			try {
				SearchRestClient searchClient = jrc.getSearchClient();
	    		int total = 0, startAt = 0;
	    		do {	    			
	    			SearchResult results = searchClient.searchJql(jql, maxResults, startAt, fields).claim();
		    		total = results.getTotal();
//		    		LOG.trace(String.format("startAt = %d, total = %d", startAt, total));
		    		Iterable<Issue> issues = results.getIssues();
		    		for(Issue issue : issues) issueSet.add(issue);
		    		startAt = issueSet.size();
	    		} while (startAt < total);
			} finally {
				closeJiraRestClient(jrc);
			}
		} catch (RestClientException e) {
			//TODO: throw new JiraClientException(e.getMessage(), e.getStatusCode().orNull());
		}
		
		return issueSet;
	}
}
