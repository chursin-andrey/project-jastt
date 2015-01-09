package com.jastt.business.services.jira.stub;

import java.io.Serializable;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.joda.time.DateTime;

import com.jastt.business.domain.entities.Assignee;
import com.jastt.business.domain.entities.Issue;
import com.jastt.business.domain.entities.Project;
import com.jastt.business.domain.entities.User;
import com.jastt.business.services.jira.JiraClientException;
import com.jastt.business.services.jira.JiraIssueService;

import static com.jastt.business.services.jira.stub.JiraStubConstants.*;

public class JiraIssueServiceStub implements JiraIssueService, Serializable {
	private static final long serialVersionUID = 1L;
	
	private List<String> issueTypeList = Arrays.asList("Bug", "Improvement", "New feature", "Task");
	private List<String> statusList = Arrays.asList("Open", "In progress", "Resolved", "Reopened", "Closed");
	private List<String> priorityList = Arrays.asList("Major", "Critical", "Minor");
	private List<String> assigneeNameList = Arrays.asList("Alice", "Bob", "Carol");
	private int[] timeIntervals = {20, 30, 45, 25, 15, 35, 40};//in minutes
	
	private List<Assignee> assigneeList = new ArrayList<Assignee>();
	private Set<Issue> issueSet = new HashSet<Issue>();
	
	public JiraIssueServiceStub() {
		generateIssues();
	}
	
	private void generateIssues() {
		for (String assigneeName : assigneeNameList) {
			Assignee assignee = new Assignee(assigneeName, assigneeName + "@mail.com");
			assigneeList.add(assignee);
		}
		
		Calendar calendar = Calendar.getInstance();
		calendar.clear();
		calendar.set(2014, Calendar.JULY, 1);
		for (int i = 1; i <= ISSUE_COUNT; i++) {
			Issue issue = new Issue();
			issue.setKey(PROJECT_KEY + "-" + i);
			issue.setSummary("This is Issue" + i + " in " + PROJECT_NAME);
			
			issue.setIssueType(issueTypeList.get(i % issueTypeList.size()));
			issue.setStatus(statusList.get(i % statusList.size()));
			issue.setPriority(priorityList.get(i % priorityList.size()));
			
			calendar.add(Calendar.HOUR_OF_DAY, 1);
			issue.setCreated(calendar.getTime());
			
			int timeSpent = timeIntervals[i % timeIntervals.length];
			issue.setTimeSpent(timeSpent);
			
			calendar.add(Calendar.MINUTE, timeSpent);
			issue.setUpdated(calendar.getTime());
			
			issue.setAssignee(assigneeList.get(i % assigneeList.size()));
			
			issueSet.add(issue);
		}
	}
	
	@Override
	public Set<Issue> getAllIssuesForProject(User user, Project project)
			throws JiraClientException {
		
		String userServerUrl = user.getServer().getUrl();
		if (!userServerUrl.equals(SERVER_URL)) 
			throw new RuntimeException(new UnknownHostException(userServerUrl));		
		if (!user.getLogin().equals(USERNAME) || !user.getPassword().equals(PASSWORD)) 
			throw new JiraClientException("User is not authenticated", 401);
		if (!project.getKey().equals(PROJECT_KEY))
			throw new JiraClientException("The value " + PROJECT_KEY + " does not exist for the project field", 400);
		
		for (Issue issue : issueSet) {
			issue.setProject(project);
		}
		return issueSet;
	}

	@Override
	public Set<Issue> getAllIssuesForProject(User user, Project project, DateTime fromDate) 
			throws JiraClientException {
		
		String userServerUrl = user.getServer().getUrl();
		if (!userServerUrl.equals(SERVER_URL)) 
			throw new RuntimeException(new UnknownHostException(userServerUrl));		
		if (!user.getLogin().equals(USERNAME) || !user.getPassword().equals(PASSWORD)) 
			throw new JiraClientException("User is not authenticated", 401);
		if (!project.getKey().equals(PROJECT_KEY))
			throw new JiraClientException("The value " + PROJECT_KEY + " does not exist for the project field", 400);
		
		Set<Issue> tmpIssueSet = new HashSet<Issue>();
		
		for (Issue issue : issueSet) {
			if (issue.getUpdated().after(fromDate.toDate())) {
				issue.setProject(project);
				tmpIssueSet.add(issue);
			}
		}
		
		return tmpIssueSet;
	}

}
