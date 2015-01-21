package com.jastt.business.services.jira.impl;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.LinkedHashSet;
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

public class JiraClient {
	
	private static final Logger LOG = LoggerFactory.getLogger(JiraClient.class);
	
	private URI serverUrl;
	private String username;
	private String password;
	
	public JiraClient(URI serverUrl, String username, String password) {
		this.serverUrl = serverUrl;
		this.username = username;
		this.password = password;
	}
	
	public JiraClient(String serverUrl, String username, String password) throws JiraClientException {
		try {
			this.serverUrl = new URI(serverUrl);
		} catch (URISyntaxException e) {
			throw new JiraClientException(e);
		}
		this.username = username;
		this.password = password;
	}
	
	private JiraRestClient createJiraRestClient() {
    	try {
			JiraRestClientFactory factory = new AsynchronousJiraRestClientFactory();
			return factory
					.createWithBasicHttpAuthentication(serverUrl, username, password);
		} catch (Exception ex) {
			LOG.error("Error at creating JiraRestClient", ex);
			throw ex;
		}
    }
	
	ServerInfo getServerInfo() throws JiraClientException {
		try {
			try (JiraRestClient jrc = createJiraRestClient()) {
				MetadataRestClient metaClient = jrc.getMetadataClient();
				ServerInfo info = metaClient.getServerInfo().claim();
				return info;
			}
		} catch (RestClientException e) {
			throw new JiraClientException(e);
		} catch (IOException e) {
			throw new JiraClientException(e);
		}
	}
	
	public Set<BasicProject> getAllProjects() throws JiraClientException {    	
    	Set<BasicProject> projectSet = new LinkedHashSet<BasicProject>();
    	
    	try {
			try (JiraRestClient jrc = createJiraRestClient()) {
				ProjectRestClient projectClient = jrc.getProjectClient();
				Iterable<BasicProject> projects = projectClient.getAllProjects().claim();
				for (BasicProject project : projects) projectSet.add(project);
			}
		} catch (RestClientException e) {
			throw new JiraClientException(e);
		} catch (IOException e) {
			throw new JiraClientException(e);
		}
    	
    	return projectSet;
    }
	 
	public int getTotalNumberOfIssuesForQuery(final String jql) throws JiraClientException {
		try {
			try (JiraRestClient jrc = createJiraRestClient()) {
				SearchRestClient searchClient = jrc.getSearchClient();
				SearchResult results = searchClient.searchJql(jql, 0, null, null).claim();
				return results.getTotal();
			}
		} catch (RestClientException e) {
			throw new JiraClientException(e);
		} catch (IOException e) {
			throw new JiraClientException(e);
		}
	}
	
	Set<Issue> getAllIssuesByQuery(final String jql, final Integer pageSize) throws JiraClientException {
		String[] requiredFields = {"project", "summary", "issuetype", "status", "created", "updated", "priority", 
    			"timetracking", "versions", "assignee", "worklog"};
    	Set<String> fields = new HashSet<String>(Arrays.asList(requiredFields));
    	
    	Set<Issue> issueSet = new LinkedHashSet<Issue>();
    	
    	try {
			try (JiraRestClient jrc = createJiraRestClient()) {
				SearchRestClient searchClient = jrc.getSearchClient();
				LOG.trace("Retrieving issues...");
				int total = 0, startAt = 0;
	    		do {	    			
	    			SearchResult results = searchClient.searchJql(jql, pageSize, startAt, fields).claim();
		    		total = results.getTotal();
		    		LOG.trace(String.format("startAt = %d, total = %d", startAt, total));
		    		Iterable<Issue> issues = results.getIssues();
		    		for(Issue issue : issues) issueSet.add(issue);
		    		startAt = issueSet.size();
	    		} while (startAt < total);
	    		LOG.trace(String.format("Retrived %d issues", issueSet.size()));
			}
		} catch (RestClientException e) {
			throw new JiraClientException(e);
		} catch (IOException e) {
			throw new JiraClientException(e);
		}
		
		return issueSet;
	}
	
	public Set<Issue> getAllIssuesByQuery(final String jql) throws JiraClientException {
		return getAllIssuesByQuery(jql, -1);
	}
	
	public Set<Issue> getSomeIssuesByQuery(final String jql, final Integer maxResults) throws JiraClientException {
		String[] requiredFields = {"project", "summary", "issuetype", "status", "created", "updated", "priority", 
    			"timetracking", "versions", "assignee", "worklog"};
    	Set<String> fields = new HashSet<String>(Arrays.asList(requiredFields));
    	
    	Set<Issue> issueSet = new LinkedHashSet<Issue>();    	
    	
    	try {
			try (JiraRestClient jrc = createJiraRestClient()) {
				SearchRestClient searchClient = jrc.getSearchClient();
				SearchResult results = searchClient.searchJql(jql, maxResults, 0, fields).claim();
				Iterable<Issue> issues = results.getIssues();
	    		for(Issue issue : issues) issueSet.add(issue);
			}
		} catch (RestClientException e) {
			throw new JiraClientException(e);
		} catch (IOException e) {
			throw new JiraClientException(e);
		}
    	
    	return issueSet;
	}
}