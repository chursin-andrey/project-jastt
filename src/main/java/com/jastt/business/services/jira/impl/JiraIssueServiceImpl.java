package com.jastt.business.services.jira.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.atlassian.jira.rest.client.api.domain.BasicComponent;
import com.atlassian.jira.rest.client.api.domain.IssueField;
import com.jastt.business.domain.entities.Assignee;
import com.jastt.business.domain.entities.Issue;
import com.jastt.business.domain.entities.Project;
import com.jastt.business.domain.entities.User;
import com.jastt.business.domain.entities.Worklog;
import com.jastt.business.services.jira.JiraClientException;
import com.jastt.business.services.jira.JiraIssueService;

//@Service("jiraIssueService")
public class JiraIssueServiceImpl implements JiraIssueService {
	private static final Logger LOG = LoggerFactory.getLogger(JiraIssueServiceImpl.class);
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
	
	@Override
	public Set<Issue> getAllIssuesForProject(User user, Project project) throws JiraClientException {
		String jql = String.format("project = \"%s\" and timeSpent > 0", 
				project.getKey());
		Set<Issue> issueSet = new LinkedHashSet<Issue>();
		
		JiraClient jc = new JiraClient(user.getServer().getUrl(), 
				user.getLogin(), user.getPassword());
		Set<com.atlassian.jira.rest.client.api.domain.Issue> jiraIssueSet = 
				jc.getAllIssuesByQuery(jql);
		for (com.atlassian.jira.rest.client.api.domain.Issue jiraIssue : jiraIssueSet) {
			Issue issue = convertJiraIssueToIssueEntity(jiraIssue);
			issue.setProject(project);
			issueSet.add(issue);
		}
		
		return issueSet;
	}

	@Override
	public Set<Issue> getAllIssuesForProject(User user, Project project,
			DateTime fromDate) throws JiraClientException {
		String jql = String.format("project = \"%s\" and timeSpent > 0 "
				+ "and updated > %d", 
				project.getKey(), fromDate.getMillis());
		Set<Issue> issueSet = new LinkedHashSet<Issue>();
		
		JiraClient jc = new JiraClient(user.getServer().getUrl(), 
				user.getLogin(), user.getPassword());
		Set<com.atlassian.jira.rest.client.api.domain.Issue> jiraIssueSet = 
				jc.getAllIssuesByQuery(jql);
		for (com.atlassian.jira.rest.client.api.domain.Issue jiraIssue : jiraIssueSet) {
			Issue issue = convertJiraIssueToIssueEntity(jiraIssue);
			issue.setProject(project);
			issueSet.add(issue);
		}
		
		return issueSet;
	}

	private static Issue convertJiraIssueToIssueEntity(com.atlassian.jira.rest.client.api.domain.Issue jiraIssue) {
		Issue issue = new Issue();
		
		issue.setKey(jiraIssue.getKey());
		issue.setSummary(jiraIssue.getSummary());
		issue.setIssueType(jiraIssue.getIssueType().getName());
		issue.setStatus(jiraIssue.getStatus().getName());
		issue.setCreated(jiraIssue.getCreationDate().toDate());
		issue.setUpdated(jiraIssue.getUpdateDate().toDate());
		DateTime dueDate = jiraIssue.getDueDate();
		issue.setDue(dueDate==null?null:dueDate.toDate());
		IssueField resolved = jiraIssue.getFieldByName("Resolved");
		if (resolved != null) {
			Object resolvedValue = resolved.getValue();
			if (resolvedValue instanceof String) {
				try {
					issue.setResolved(dateFormat.parse( (String) resolvedValue));
				} catch (ParseException e) {
					LOG.error(e.getMessage());
				}
			}
		}
		
		if (jiraIssue.getPriority() != null) issue.setPriority(jiraIssue.getPriority().getName());
		if (jiraIssue.getTimeTracking() != null)
			if (jiraIssue.getTimeTracking().getTimeSpentMinutes() != null) {
				issue.setTimeSpent(jiraIssue.getTimeTracking().getTimeSpentMinutes());
			}
		
		Assignee assignee = new Assignee();
		if (jiraIssue.getAssignee() != null) {
			assignee.setName(jiraIssue.getAssignee().getDisplayName());
			assignee.setEmail(jiraIssue.getAssignee().getEmailAddress());
		} else {
			assignee.setName("ABSENT");
			assignee.setEmail("NoEmail");
		}
		issue.setAssignee(assignee);
		// version field is null for the present
		
		Set<Worklog> worklogSet = new LinkedHashSet<Worklog>();
		if (jiraIssue.getWorklogs() != null) {
			for (com.atlassian.jira.rest.client.api.domain.Worklog jiraWorklog : jiraIssue.getWorklogs()) {
				Worklog worklog = new Worklog();
				
				worklog.setIssue(issue);
				worklog.setSelf(jiraWorklog.getSelf().toString());
				worklog.setAuthor(jiraWorklog.getAuthor().getDisplayName());
				worklog.setCreated(jiraWorklog.getCreationDate().toDate());
				worklog.setUpdated(jiraWorklog.getUpdateDate().toDate());
				worklog.setStarted(jiraWorklog.getStartDate().toDate());
				worklog.setTimeSpent(jiraWorklog.getMinutesSpent());
				
				worklogSet.add(worklog);
			}
			
			if (worklogSet.isEmpty()) LOG.warn("Worklog of Issue {} has no items (empty)", issue.getKey());
			if (worklogSet.size() >= 20) LOG.warn("Issue {} has too long worklog", issue.getKey()); 
		} else {
			LOG.warn("Issue {} has no worklog", issue.getKey());
		}
		issue.setWorklogs(worklogSet);
		
		Iterable<BasicComponent> components = jiraIssue.getComponents();
		ArrayList<String> componentsNames = new ArrayList<String>();
		for (BasicComponent basicComponent : components) {
		    String componentsName = basicComponent.getName();
		    componentsNames.add(componentsName);
		}
		Collections.sort(componentsNames);
		issue.setComponent(componentsNames.toString());
		
		return issue;
	}
}
