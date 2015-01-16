package com.jastt.frontend.beans;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

import net.sf.jasperreports.engine.JRException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.jastt.business.domain.entities.Assignee;
import com.jastt.business.domain.entities.Issue;
import com.jastt.business.domain.entities.Permission;
import com.jastt.business.domain.entities.Project;
import com.jastt.business.domain.entities.Server;
import com.jastt.business.domain.entities.User;
import com.jastt.business.enums.IssueStatusEnum;
import com.jastt.business.enums.IssueTypeEnum;
import com.jastt.business.services.AssigneeService;
import com.jastt.business.services.IssueService;
import com.jastt.business.services.PermissionService;
import com.jastt.business.services.ProjectService;
import com.jastt.business.services.ReportingService;
import com.jastt.business.services.impl.IssueServiceImpl;
import com.jastt.business.services.jira.JiraClientException;
import com.jastt.business.enums.PredefinedDateEnum;

@Component
@Scope("session")
public class ReportBean implements Serializable{

	private static final long serialVersionUID = 7688215600325304973L;
	
	private List<Issue> issues;
	private List<Project> projects;
	private Set<Assignee> assignees;
	private Set<String> assignees_name;
	private List<Permission> permissions;
	private String issueType;
	private String project_name;
	private Integer assignee_id;
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
	private String predefinedDate;
	private int hours;
	private int minuts;
	
	private List<Issue> reportIssues;
	private List<Project> reportProjects;
	private List<Assignee> reportAssignees;


	@Autowired
	private IssueService issueService;
	@Autowired
	private ProjectService projectService;
	@Autowired
	private AssigneeService assigneeService;
	@Autowired
	private ReportingService reportingService;  
	@Autowired
	private PermissionService permissionService;  
	
	@PostConstruct
	public void init() {
		Subject subject = SecurityUtils.getSubject();	
		User user = (User)subject.getSession().getAttribute("user");
		disableMenu = true;  disableSelectDate = true;
		projects = new ArrayList<Project>();
		
		timespent = "allTime";
		//Server serv = new Server("http://localhost:8083");
		//projects = projectService.getAllProjects();
		permissions = permissionService.getPermissionsByUser(user);
		for(Permission perm : permissions){
				project = projectService.getProjectById(perm.getProject().getId());
				projects.add(project);

		}

		assignees = assigneeService.getAllAssignees();
	}
	
	public void changeProject() {
		project = projectService.getProjectByName(project_name);
		if(project != null) {
			disableMenu = false; 
			//issues = issueService.getIssues(project, null, null, null, null, null);
			assignees = assigneeService.getAssigneesByProject(project);
		} else disableMenu = true;
		
	}


	public void changeIssue() {
		
	}
	
	public void changeAssignee() {

	}
	
	public void changeTime() {
		if(timespent.equals("allTime")) { 
			disableSelectTime = false; 
			disableSelectDate = true;
			dateFrom = null; dateTo = null;
		}
		else {
			disableSelectTime = true; 
			disableSelectDate = false;
		}
	}
	
	public void showReport() {
		reportAssignees = new ArrayList<Assignee>();
		IssueTypeEnum type = null;
		IssueStatusEnum issueStatus = null;
		if(reportIssues != null) reportIssues.clear();

		if(issueType != null) type = IssueTypeEnum.getType(issueType); 
		if(status != null) issueStatus = IssueStatusEnum.getType(status); 
		reportIssues = new ArrayList<Issue>();
		if(assignees_name.size() != 0) {
			for(String assign : assignees_name) {
				reportAssignees.add(assigneeService.getAssigneeByName(assign));	
			}
			if(timespent == "date")
				reportIssues = issueService.getIssues(project, issueStatus, reportAssignees, type, dateFrom, dateTo);
			else
				reportIssues = issueService.getIssues(project, issueStatus, reportAssignees, type, PredefinedDateEnum.getType(predefinedDate));
			addHours(reportIssues);
						
		} else {
			reportIssues = issueService.getIssues(project, issueStatus, null, type, dateFrom, dateTo);
			addHours(reportIssues);
		}		
	}
	
	
	public void addHours(List<Issue> reportIssues) {

		int result = 0;
		for(int i = 0; i < reportIssues.size(); i++) {
			
			 int prom = reportIssues.get(i).getTimeSpent();
			 result += prom;
			
		}
		setHours(result);
	}
	
	
	public void updateData() throws JiraClientException{
		Subject subject = SecurityUtils.getSubject();	
		User user = (User)subject.getSession().getAttribute("user");
		issueService.update(user);
	}
	
	public void clearReport(){
		if(projects != null) projects.clear();
		projects = projectService.getAllProjects();
		if(issues != null) issues.clear();
		if(assignees != null) assignees.clear();
		project_name = null;
		issueType = null;
		status = null;
		disableMenu = true;
		
	}
	public void cancelAction(){
		reportIssues.clear();
		setHours(0);
	}
	public void exportIssueList(ActionEvent actionEvent) throws JRException, IOException {		
		Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		String reportFormat = params.get("reportFormat");
		if (!Arrays.asList("pdf", "xls", "xlsx").contains(reportFormat)) return;
		
		HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();		
		ServletOutputStream outputStream = response.getOutputStream();
		
		switch (reportFormat) {
		case "pdf":
			response.addHeader("Content-disposition","attachment; filename=issueReport.pdf");
			reportingService.exportToPdf(reportIssues, outputStream);
			break;
		case "xls":
			response.addHeader("Content-disposition","attachment; filename=issueReport.xls");
			reportingService.exportToXls(reportIssues, outputStream);
			break;
		case "xlsx":
			response.addHeader("Content-disposition","attachment; filename=issueReport.xlsx");
			reportingService.exportToXlsx(reportIssues, outputStream);
		default: 
			break;
		}
		
		outputStream.flush();
		outputStream.close();
		FacesContext.getCurrentInstance().responseComplete();
	}

	public List<Permission> getPermissions() {
		return permissions;
	}

	public void setPermissions(List<Permission> permissions) {
		this.permissions = permissions;
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
	
	public Set<Assignee> getAssignees() {
		return assignees;
	}

	public void setAssignees(Set<Assignee> assignees) {
		this.assignees = assignees;
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

	public Integer getAssignee_id() {
		return assignee_id;
	}

	public void setAssignee_id(Integer assignee_id) {
		this.assignee_id = assignee_id;
	}

	public List<Issue> getReportIssues() {
		return reportIssues;
	}

	public void setReportIssues(List<Issue> reportIssues) {
		this.reportIssues = reportIssues;
	}

	public List<Project> getReportProjects() {
		return reportProjects;
	}

	public void setReportProjects(List<Project> reportProjects) {
		this.reportProjects = reportProjects;
	}

	public List<Assignee> getReportAssignees() {
		return reportAssignees;
	}

	public void setReportAssignees(List<Assignee> reportAssignees) {
		this.reportAssignees = reportAssignees;
	}

	public String getIssueType() {
		return issueType;
	}

	public void setIssueType(String issueType) {
		this.issueType = issueType;
	}

	public Set<String> getAssignees_name() {
		return assignees_name;
	}

	public void setAssignees_name(Set<String> assignees_name) {
		this.assignees_name = assignees_name;
	}
	
	
	public int getHours() {
		return hours;
	}

	public void setHours(int hours) {
		this.hours = hours;
	}

	public int getMinuts() {
		return minuts;
	}

	public void setMinuts(int minuts) {
		this.minuts = minuts;
	}

	public String getPredefinedDate() {
		return predefinedDate;
	}

	public void setPredefinedDate(String predefinedDate) {
		this.predefinedDate = predefinedDate;
	}



}