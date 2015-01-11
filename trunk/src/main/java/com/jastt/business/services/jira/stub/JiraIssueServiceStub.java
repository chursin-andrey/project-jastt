package com.jastt.business.services.jira.stub;

import java.io.Serializable;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.joda.time.DateTime;

import com.jastt.business.domain.entities.Assignee;
import com.jastt.business.domain.entities.Issue;
import com.jastt.business.domain.entities.Project;
import com.jastt.business.domain.entities.User;
import com.jastt.business.domain.entities.Worklog;
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
	
	private int worklogCounter = 0;
	
	private List<Assignee> assigneeList = new ArrayList<Assignee>();
	private Set<Issue> issueSet = new LinkedHashSet<Issue>();
	
	public JiraIssueServiceStub() {
//		generateIssues();
		generateIssuesWithWorklog();
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
	
	private void generateIssuesWithWorklog() {
		for (String assigneeName : assigneeNameList) {
			Assignee assignee = new Assignee(assigneeName, assigneeName + "@mail.com");
			assigneeList.add(assignee);
		}
		
		Calendar calendar = Calendar.getInstance();
		calendar.clear();
		calendar.set(2014, Calendar.JULY, 1);
		for (int i = 0; i < ISSUE_COUNT; i++) {
			Issue issue = new Issue();
			issue.setKey(String.format("%s-%d", PROJECT_KEY, i + 1));
			issue.setSummary(String.format("This is Issue%d in %s", i + 1, PROJECT_NAME));
			issue.setAssignee(assigneeList.get(i % assigneeList.size()));
			
			issue.setIssueType(issueTypeList.get(i % issueTypeList.size()));
			issue.setStatus(statusList.get(i % statusList.size()));
			issue.setPriority(priorityList.get(i % priorityList.size()));
			
			calendar.add(Calendar.HOUR_OF_DAY, 1);
			issue.setCreated(calendar.getTime());
			issue.setUpdated(issue.getCreated());
			issue.setTimeSpent(0);		
			
			generateWorklog(issue);
			calendar.setTime(issue.getUpdated());
			
			issueSet.add(issue);
		}
	}
	
	private void generateWorklog(Issue issue) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(issue.getCreated());
		
		int totalTimeSpent = 0;
		Set<Worklog> worklogSet = new LinkedHashSet<Worklog>();
		for (int i = 1; i <= WORKLOG_COUNT; i++) {
			Worklog worklog = new Worklog(); 
			worklog.setIssue(issue);
			worklog.setSelf(String.format("%s/rest/api/2/issue/%s/worklog/%d", SERVER_URL, issue.getKey(), i));
			worklog.setAuthor(issue.getAssignee().getName());
			
			worklog.setStarted(calendar.getTime());
			int timeSpent = timeIntervals[worklogCounter % timeIntervals.length];
			totalTimeSpent += timeSpent;
			worklog.setTimeSpent(timeSpent);
			
			calendar.add(Calendar.MINUTE, worklog.getTimeSpent());
			worklog.setCreated(calendar.getTime());
			worklog.setUpdated(calendar.getTime());
			
			worklogSet.add(worklog);
			worklogCounter++;
		}
		
		issue.setWorklogs(worklogSet);
		issue.setTimeSpent(totalTimeSpent);
		issue.setUpdated(calendar.getTime());
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
