package com.jastt.frontend.beans;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.jastt.business.domain.entities.Assignee;
import com.jastt.business.domain.entities.Issue;
import com.jastt.business.domain.entities.Project;
import com.jastt.business.services.AssigneeService;
import com.jastt.business.services.IssueService;
import com.jastt.business.services.ProjectService;

@Component
@Scope("request")
public class ReportBean implements Serializable{

	private static final long serialVersionUID = 7688215600325304973L;
	
	private List<Issue> issues;
	private List<Project> projects;
	private List<Assignee> assignees;
	private String project;
	private String issue;
	private String assignee;


	@Autowired
	private IssueService issueService;
	@Autowired
	private ProjectService projectService;
	@Autowired
	private AssigneeService assigneeService;
	
	@PostConstruct
	public void init(){
		issues = issueService.getAllIssues();
		projects = projectService.getAllProjects();
		assignees = assigneeService.getAllAssignees();
	}
	
	public List<Issue> getIssues() {
		return issues;
	}

	public void setIssues(List<Issue> issues) {
		this.issues = issues;
	}
	
	public List<Project> getProjects() {
		return projects;
	}

	public void setProjects(List<Project> projects) {
		this.projects = projects;
	}
	
	public List<Assignee> getAssignees() {
		return assignees;
	}

	public void setAssignees(List<Assignee> assignees) {
		this.assignees = assignees;
	}

	public String getProject() {
		return project;
	}

	public void setProject(String project) {
		this.project = project;
	}

	public String getIssue() {
		return issue;
	}

	public void setIssue(String issue) {
		this.issue = issue;
	}
	

	public String getAssignee() {
		return assignee;
	}

	public void setAssignee(String assignee) {
		this.assignee = assignee;
	}

}
