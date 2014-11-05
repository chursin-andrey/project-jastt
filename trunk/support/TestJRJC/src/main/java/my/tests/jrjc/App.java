package my.tests.jrjc;

import java.io.IOException;
import java.net.URI;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import com.atlassian.jira.rest.client.api.IssueRestClient;
import com.atlassian.jira.rest.client.api.JiraRestClient;
import com.atlassian.jira.rest.client.api.JiraRestClientFactory;
import com.atlassian.jira.rest.client.api.MetadataRestClient;
import com.atlassian.jira.rest.client.api.ProjectRestClient;
import com.atlassian.jira.rest.client.api.SearchRestClient;
import com.atlassian.jira.rest.client.api.domain.BasicProject;
import com.atlassian.jira.rest.client.api.domain.Issue;
import com.atlassian.jira.rest.client.api.domain.SearchResult;
import com.atlassian.jira.rest.client.api.domain.ServerInfo;
import com.atlassian.jira.rest.client.internal.async.AsynchronousJiraRestClientFactory;

public class App {
//	private static URI JIRA_URI = URI.create("https://jira.atlassian.com");
//    private static final String JIRA_USERNAME = "buslov84";
//    private static final String JIRA_PASSWORD = "NdQp9AQCpb";
    
	private static URI JIRA_URI = URI.create("https://seu30.gdc-sbr01.t-systems.com/jira/");
    private static final String JIRA_USERNAME = "vbichev";
    private static final String JIRA_PASSWORD = "wiw1!SIS";
    
    public static void main( String[] args ) {
    	String issueKey ="PSLDE-229";//https://jira.atlassian.com/browse/STASH-2524
    	
    	testConnection();
    	    	
    	getProjects();
    	
    	getIssues();    	
    	
    	getIssueInfo(issueKey);
    	
//    	getIssueInfoFromIssueList(issueKey);
    	
    	System.out.println("exit...");
    }
    
    public static void testConnection() {
    	JiraRestClient jrc = createJiraRestClient();
    	
    	MetadataRestClient metaClient = jrc.getMetadataClient();
    	ServerInfo info = metaClient.getServerInfo().claim();
    	System.out.println("JIRA Server: " + info.getBaseUri());
    	
    	closeJiraRestClient(jrc);
    	System.out.println("");
    }
    
    public static void getProjects() {
    	Iterable<BasicProject> projects = getAllProjects();
		for(BasicProject project : projects) {
			System.out.println("Project Name: " + project.getName());
		}
		System.out.println("");
    }
    
    public static void getIssueInfo(String issueKey) {
    	Issue i1 = getIssueByKey(issueKey);
    	printIssue(i1);
    	System.out.println("");
    }
    
    public static void getIssues() {
//    	String jql = "project = ANERDS AND assignee = nmuldoon"; 
//    	String jql = "project = ANERDS AND summary !~ solahart AND assignee is not empty";
//    	String jql = "timespent > 0 AND assignee is not EMPTY";
    	String jql = "project=PSLDE";
    	
    	Iterable<Issue> issues = getIssuesByQuery(jql);
    	int i = 0;
    	for(Issue issue : issues) {
    		System.out.println(++i + ": " + issue.getSummary());
    	}
    	System.out.println("");
    }
    
    public static void getIssueInfoFromIssueList(String issueKey) {
    	String jql = "timespent > 0 AND assignee is not EMPTY and project != TST";
    	
    	getIssueInfo(issueKey);
    	
    	Iterable<Issue> issues = getIssuesByQuery(jql);
    	for(Issue issue : issues) {
    		if (issue.getKey().equals(issueKey)) {
    			System.out.println("");
    			printIssue(issue); 
    		}
    	}
    	System.out.println("");
    }
    
    public static void printIssue(Issue issue) {
    	StringBuilder output = new StringBuilder();
    	
    	output.append("Project: "); output.append(issue.getProject().getKey()); 
    	output.append("\r\nDesr: "); output.append(issue.getSummary());
    	output.append("\r\nType: "); output.append(issue.getIssueType().getName());
    	output.append("\r\nStatus: "); output.append(issue.getStatus().getName());
    	output.append("\r\nCreated: "); output.append(issue.getCreationDate());
    	output.append("\r\nUpdated: "); output.append(issue.getUpdateDate());
    	output.append("\r\nPriority: "); output.append(issue.getPriority() == null ? null : issue.getPriority().getName());
    	output.append("\r\nTimespent(min): "); output.append(issue.getTimeTracking() == null ? null : issue.getTimeTracking().getTimeSpentMinutes());
    	output.append("\r\nVersion: ");
    	output.append("\r\nAssignee: "); output.append(issue.getAssignee() == null ? null : issue.getAssignee().getDisplayName());
    	
    	System.out.println(output.toString());
    }

    public static Issue getIssueByKey(String issueKey) {
    	JiraRestClient jrc = createJiraRestClient();
    	
    	IssueRestClient issueClient = jrc.getIssueClient();
    	Issue issue = issueClient.getIssue(issueKey).claim();
    	
    	closeJiraRestClient(jrc);
    	return issue;
    }
    
    public static Iterable<Issue> getIssuesByQuery(String jql) {
    	String[] requiredFields = {"project", "summary", "issuetype", "status", "created", "updated", "priority", 
    			"timetracking", "versions", "assignee"};
     	Set<String> fields = new HashSet<String>(Arrays.asList(requiredFields));
     	
    	JiraRestClient jrc = createJiraRestClient();
    	
    	SearchRestClient searchClient = jrc.getSearchClient();
//    	SearchResult results = searchClient.searchJql(jql, null, null, fields).claim();
    	SearchResult results = searchClient.searchJql(jql, -1, null, fields).claim();
    	int total = results.getTotal();
    	System.out.println("total number of issues: " + total);
    	closeJiraRestClient(jrc);
    	return results.getIssues();
    }
    
    public static Iterable<BasicProject> getAllProjects() {
    	JiraRestClient jrc = createJiraRestClient();
    	
    	ProjectRestClient projectClient = jrc.getProjectClient();
    	Iterable<BasicProject> projects = projectClient.getAllProjects().claim();
    	
    	closeJiraRestClient(jrc);
    	return projects;
    }
        
    private static JiraRestClient createJiraRestClient() {
    	JiraRestClientFactory factory = new AsynchronousJiraRestClientFactory();
    	return factory
    			.createWithBasicHttpAuthentication(JIRA_URI, JIRA_USERNAME, JIRA_PASSWORD);
    }
    
    private static void closeJiraRestClient(JiraRestClient jrc) {
    	try {
			jrc.close();
		} catch (IOException e) {
//				e.printStackTrace();
			System.out.println("IOException in client.close()");
		}
    }
}
