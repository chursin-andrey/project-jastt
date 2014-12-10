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
	private Integer project_id;
	private String project_name;
	private Integer assignee_name;
	private Integer issue_id;
	private Project project;
	private Issue issue;
	private Assignee assignee;


	@Autowired
	private IssueService issueService;
	@Autowired
	private ProjectService projectService;
	@Autowired
	private AssigneeService assigneeService;
	
	@PostConstruct
	public void init(){
		projects = projectService.getAllProjects();
		//project = projectService.getProjectByName(project_name);
		//issues = issueService.getIssues(project, null, null, null, null, null);
		
		assignees = assigneeService.getAllAssignees();
	}
	
	public void changeProject() {
		project = projectService.getProjectByName(project_name);
		issues = issueService.getIssues(project, null, null, null, null, null);
		assignees = assigneeService.getAllAssignees();
		//assignees.add(assigneeService.getAssigneeById(1));
		//assignees = assigneeService.getAssigneeById(issues.);
	}
	
	public void changeIssue() {
		issue = issueService.getIssueById(issue_id);
		//assignees = assigneeService.getAllAssignees();
		//assignees.add(assigneeService.getAssigneeById(issue.getAssignee().getId()));
	}
	
	public Integer getIssue_id() {
		return issue_id;
	}

	public void setIssue_id(Integer issue_id) {
		this.issue_id = issue_id;
	}

	public String getProject_name() {
		return project_name;
	}

	public void setProject_name(String project_name) {
		this.project_name = project_name;
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


	public Integer getProject_id() {
		return project_id;
	}

	public void setProject_id(Integer project_id) {
		this.project_id = project_id;
	}

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}
	

	public Issue getIssue() {
		return issue;
	}

	public void setIssue(Issue issue) {
		this.issue = issue;
	}

	public Assignee getAssignee() {
		return assignee;
	}

	public void setAssignee(Assignee assignee) {
		this.assignee = assignee;
	}



}
