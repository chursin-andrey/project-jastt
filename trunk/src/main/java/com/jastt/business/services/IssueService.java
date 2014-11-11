package com.jastt.business.services;

import com.jastt.business.domain.entities.Project;
import com.jastt.business.domain.entities.Issue;
import com.jastt.business.domain.entities.Assignee;

import java.util.List;

public interface IssueService {
	public List<Project> getAllProjects();
	public List<Issue> getAllIssues();
	public List<Assignee> getAllAssignees();
}
