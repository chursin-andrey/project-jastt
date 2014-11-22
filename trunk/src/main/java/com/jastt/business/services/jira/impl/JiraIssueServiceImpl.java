package com.jastt.business.services.jira.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.joda.time.DateTime;
import org.springframework.stereotype.Service;

import com.jastt.business.domain.entities.Assignee;
import com.jastt.business.domain.entities.Issue;
import com.jastt.business.domain.entities.Project;
import com.jastt.business.domain.entities.User;
import com.jastt.business.services.jira.JiraClientException;
import com.jastt.business.services.jira.JiraIssueService;

@Service("jiraIssueService")
public class JiraIssueServiceImpl implements JiraIssueService {

	@Override
	public Set<Issue> getAllIssuesForProject(User user, Project project) throws JiraClientException {
		String jql = String.format("project = %s and timeSpent > 0 and assignee is not null", 
				project.getKey());
		Set<Issue> issueSet = new HashSet<Issue>();
		
		JiraClient jc = new JiraClient(user.getServer().getUrl(), 
				user.getLogin(), user.getPassword());
		Set<com.atlassian.jira.rest.client.api.domain.Issue> jiraIssueSet = 
				jc.getAllIssuesByQuery(jql, -1);
		for (com.atlassian.jira.rest.client.api.domain.Issue jiraIssue : jiraIssueSet) {
			Issue issue = new Issue();
			
			issue.setKey(jiraIssue.getKey());
			issue.setProject(project);
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
			
			issueSet.add(issue);
		}
		
		return null;//return issueSet;
	}

	@Override
	public Set<Issue> getAllIssuesForProject(User user, Project project,
			DateTime fromDate) throws JiraClientException {
		String jql = String.format("project = %s and timeSpent > 0 and assignee is not null "
				+ "and updated > %d", 
				project.getKey(), fromDate.getMillis());
		
		return null;
	}
}
