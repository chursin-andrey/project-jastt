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
	public Issue getIssueByKey(User user, String issueKey) throws JiraClientException {
		
		return null;
	}

	@Override
	public Set<Issue> getAllIssuesForProject(User user, Project project) throws JiraClientException {
		String jql = String.format("project = %s and timeSpent > 0 and assignee is not null", 
				project.getKey());
		Set<Issue> issueSet = new HashSet<Issue>();
		
		JiraClient jc = new JiraClient(user.getServer().getUrl(), 
				user.getLogin(), user.getPassword());
		List<com.atlassian.jira.rest.client.api.domain.Issue> issueList = 
				jc.getAllIssuesByQuery(jql);
		for (com.atlassian.jira.rest.client.api.domain.Issue issue : issueList) {
			Issue iss = new Issue();
			
			iss.setKey(issue.getKey());
			iss.setProject(project);
			iss.setSummary(issue.getSummary());
			iss.setIssueType(issue.getIssueType().getName());
			iss.setStatus(issue.getStatus().getName());
			iss.setCreated(issue.getCreationDate().toDate());
			iss.setUpdated(issue.getUpdateDate().toDate());
			if (issue.getPriority() != null) iss.setPriority(issue.getPriority().getName());
			if (issue.getTimeTracking() != null)
				if (issue.getTimeTracking().getTimeSpentMinutes() != null) {
					iss.setTimeSpent(issue.getTimeTracking().getTimeSpentMinutes());
				}
			if (issue.getAssignee() != null) {
				Assignee assignee = new Assignee();
				
				assignee.setName(issue.getAssignee().getDisplayName());
				assignee.setEmail(issue.getAssignee().getEmailAddress());
				
				iss.setAssignee(assignee);
			}
			
			issueSet.add(iss);
		}
		
		return null;//return issueSet;
	}

	@Override
	public Set<Issue> getAllIssuesForProject(User user, Project project,
			DateTime fromDate) throws JiraClientException {
		String jql = String.format("project = %s and timeSpent > 0 and assignee is not null "
				+ "and created > %d and updated > %d", 
				project.getKey(), fromDate.getMillis());
		
		return null;
	}
}
