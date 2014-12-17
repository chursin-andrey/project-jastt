package com.jastt.frontend.beans;

import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.event.ActionEvent;

import net.sf.jasperreports.engine.JRException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.jastt.business.domain.entities.Assignee;
import com.jastt.business.domain.entities.Issue;
import com.jastt.business.domain.entities.Project;
import com.jastt.business.services.AssigneeService;
import com.jastt.business.services.IssueService;
import com.jastt.business.services.ProjectService;
import com.jastt.business.services.ReportingService;

@Component
@Scope("session")
public class ReportBean implements Serializable{

	private static final long serialVersionUID = 7688215600325304973L;
	
	private List<Issue> issues;
	private List<Project> projects;
	private List<Project> projects_t;
	private List<Assignee> assignees;
	private Integer project_id;
	private String project_name;
	private String assignee_id;
	private String issue_id;
	private Project project;
	private Issue issue;
	private Assignee assignee;
	private Date dateFrom;
	private Date dateTo;
	private boolean disableMenu;
	private boolean disableSelectTime;
	private boolean disableSelectDate;
	private String timespent;
	private String status;


	@Autowired
	private IssueService issueService;
	@Autowired
	private ProjectService projectService;
	@Autowired
	private AssigneeService assigneeService;
	@Autowired
	private ReportingService reportingService;  
	
	@PostConstruct
	public void init(){
		disableMenu = true; disableSelectTime = true; disableSelectDate = true;
		projects = projectService.getAllProjects();
		//project = projectService.getProjectByName(project_name);
		//issues = issueService.getIssues(project, null, null, null, null, null);
		
		assignees = assigneeService.getAllAssignees();
	}
	
	public void changeProject() {
		project = projectService.getProjectByName(project_name);
		if(project != null) {
			disableMenu = false; 
			issues = issueService.getIssues(project, null, null, null, null, null);
			assignees = assigneeService.getAllAssignees();
		} else disableMenu = true;
		
		//assignees.add(assigneeService.getAssigneeById(1));
		//assignees = assigneeService.getAssigneeById(issues.);
	}


	public void changeIssue() {
		
		//assignees = assigneeService.getAllAssignees();
		assignees.clear();
		if(issue_id.length() != 0){
			issue = issueService.getIssueById(Integer.parseInt(issue_id));
			assignees.add(assigneeService.getAssigneeById(issue.getAssignee().getId()));
		} else {
			assignees = assigneeService.getAllAssignees();
		}
		
	}
	
	public void changeAssignee() {
		
			issues.clear();
			assignee = assigneeService.getAssigneeById(project_id);
			issues = issueService.getIssues(null, null, assignee, null, null, null);
			

	}
	
	public void changeTime() {
		if(timespent.equals("allTime")) { 
			disableSelectTime = false; 
			disableSelectDate = true;
		}
		else {
			disableSelectTime = true; 
			disableSelectDate = false;
		}
	}
	
	public void showReport() {
		//projects_t = projects;
		//assignees = assigneeService.getAllAssignees();
		//assignees.add(assigneeService.getAssigneeById(issue.getAssignee().getId()));
	}
	
	public void exportToPDF(ActionEvent actionEvent) throws JRException, IOException {
			reportingService.exportToPdf(issues);
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public boolean isDisableSelectTime() {
		return disableSelectTime;
	}

	public void setDisableSelectTime(boolean disableSelectTime) {
		this.disableSelectTime = disableSelectTime;
	}

	public boolean isDisableSelectDate() {
		return disableSelectDate;
	}

	public void setDisableSelectDate(boolean disableSelectDate) {
		this.disableSelectDate = disableSelectDate;
	}

	public String getTimespent() {
		return timespent;
	}

	public void setTimespent(String timespent) {
		this.timespent = timespent;
	}

	public boolean isDisableMenu() {
		return disableMenu;
	}

	public void setDisableMenu(boolean disableMenu) {
		this.disableMenu = disableMenu;
	}

	public Date getDateFrom() {
		return dateFrom;
	}

	public void setDateFrom(Date dateFrom) {
		this.dateFrom = dateFrom;
	}

	public Date getDateTo() {
		return dateTo;
	}

	public void setDateTo(Date dateTo) {
		this.dateTo = dateTo;
	}

	public String getIssue_id() {
		return issue_id;
	}

	public void setIssue_id(String issue_id) {
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

	public String getAssignee_id() {
		return assignee_id;
	}

	public void setAssignee_id(String assignee_id) {
		this.assignee_id = assignee_id;
	}

	public List<Project> getProjects_t() {
		return projects_t;
	}

	public void setProjects_t(List<Project> projects_t) {
		this.projects_t = projects_t;
	}



}
