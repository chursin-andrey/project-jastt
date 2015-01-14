package com.jastt.business.services.jira.impl;

import java.util.LinkedHashSet;
import java.util.Set;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.jastt.business.domain.entities.Assignee;
import com.jastt.business.domain.entities.Issue;
import com.jastt.business.domain.entities.Project;
import com.jastt.business.domain.entities.User;
import com.jastt.business.domain.entities.Worklog;
import com.jastt.business.services.jira.JiraClientException;
import com.jastt.business.services.jira.JiraIssueService;
import com.jastt.business.services.jira.impl.client.JiraClient;

//@Service("jiraIssueService")
public class JiraIssueServiceImpl implements JiraIssueService {
	private static final Logger LOG = LoggerFactory.getLogger(JiraIssueServiceImpl.class);
	
	@Override
	public Set<Issue> getAllIssuesForProject(User user, Project project) throws JiraClientException {
		String jql = String.format("project = \"%s\" and timeSpent > 0 and assignee is not null", 
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
		String jql = String.format("project = \"%s\" and timeSpent > 0 and assignee is not null "
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

	static Issue convertJiraIssueToIssueEntity(com.atlassian.jira.rest.client.api.domain.Issue jiraIssue) {
		Issue issue = new Issue();
		
		issue.setKey(jiraIssue.getKey());
		issue.setSummary(jiraIssue.getSummary());
		issue.setIssueType(jiraIssue.getIssueType().getName());
		issue.setStatus(jiraIssue.getStatus().getName());
		issue.setCreated(jiraIssue.getCreationDate().toDate());
		issue.setUpdated(jiraIssue.getUpdateDate().toDate());
		
		if (jiraIssue.getPriority() != null) issue.setPriority(jiraIssue.getPriority().getName());
		if (jiraIssue.getTimeTracking() != null)
			if (jiraIssue.getTimeTracking().getTimeSpentMinutes() != null) {
				issue.setTimeSpent(jiraIssue.getTimeTracking().getTimeSpentMinutes());
			}
		if (jiraIssue.getAssignee() != null) {
			Assignee assignee = new Assignee();
			
			assignee.setName(jiraIssue.getAssignee().getDisplayName());
			assignee.setEmail(jiraIssue.getAssignee().getEmailAddress());
			
			issue.setAssignee(assignee);
		}
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
		
		return issue;
	}
}
