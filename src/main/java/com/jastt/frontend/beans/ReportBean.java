package com.jastt.frontend.beans;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

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
import com.jastt.utils.AlphanumComparator;

@Component
@Scope("session")
public class ReportBean implements Serializable{

	private static final long serialVersionUID = 7688215600325304973L;
	
	private Comparator alphanumComparator = new AlphanumComparator();
	private List<Issue> issues;
	private List<Project> projects;
	private Set<Assignee> assignees;
	private Set<String> assignees_name;
	private List<Entry<String, List<Issue> >> entries;
	private List<Integer> as_time;
	private ArrayList<Assignee> assignees_list;
	private Map<String, List<Issue>> mapAssigneesIssues = new HashMap<String, List<Issue>>();	
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
	private int allTime;
	
	private List<Issue> reportIssues;
	private List<Issue> reportIssues2;
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
		assignees = new HashSet<Assignee>();
		
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
		if(project == null) return;
		IssueTypeEnum type = null;
		IssueStatusEnum issueStatus = null;
		if(reportIssues != null) reportIssues.clear();

		if(issueType != null) type = IssueTypeEnum.getType(issueType); 
		if(status != null) issueStatus = IssueStatusEnum.getType(status); 
		reportIssues = new ArrayList<Issue>();
		
		if(assignees_name != null)
		if(assignees_name.size() != 0) {
			for(String assign : assignees_name) {
				reportAssignees.add(assigneeService.getAssigneeByName(assign));	
			}
		} 
		if(timespent == "date")
			reportIssues = issueService.getIssues(project, issueStatus, reportAssignees, type, dateFrom, dateTo);
		else
			reportIssues = issueService.getIssues(project, issueStatus, reportAssignees, type, PredefinedDateEnum.getType(predefinedDate));

						
		
		reportIssues2 = reportIssues;
		addAssigneesName();
		sortingIssues(reportIssues);
		sortingIssues(reportIssues2);
		showAllTime(reportIssues);
		showAllTime(reportIssues2);
		assignees_list = new ArrayList<Assignee>(assignees);
		addMapIssue(reportIssues2);
		showAsssigneeTime();
		
	}
	
	private void addAssigneesName() {
		for(Assignee as : assignees) {
			as.getName();
		 }
	}
	
	private void sortingIssues(List<Issue> report) {
		
		Collections.sort(report, new Comparator<Issue>() {

			@Override
			public int compare(Issue i1, Issue i2) {	
				return i1.getAssignee().getName().compareTo(i2.getAssignee().getName());
			}
		});
	}
	
	private void sortingAssignees(List<Assignee> list) {
		
		Collections.sort(list, new Comparator<Assignee>() {

			@Override
			public int compare(Assignee a1, Assignee a2) {
				return a1.getName().compareTo(a2.getName());
			}
		});
	}
	
	
	public int showAllTime(List<Issue> list) {

		int result = 0;
		for(int i = 0; i < list.size(); i++) {
			
			 int increment = list.get(i).getTimeSpent();
			 result += increment;
		}

		setAllTime(result);
		
		return result;
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
		reportIssues2.clear();
		setAllTime(0);
		as_time.clear();
		mapAssigneesIssues.clear();
		entries.clear();
	}
	
	public void addMapIssue(List<Issue> list) {
		int startPosition= 0;
		if (assignees_list.size() == 0) return;
		String tmp = assignees_list.get(0).getName();
        int nach = 0;
        int endp = 0;
        sortingIssues(list);
        sortingAssignees(assignees_list);
       
		for(Assignee as : assignees_list) {

			for(int j = 0; j < list.size(); j++) {
				
				if (tmp.equals( list.get(j).getAssignee().getName() )) {
					endp = j;
					mapAssigneesIssues.put(tmp, new ArrayList<Issue>(list.subList(nach, ++endp)) );
		        } else {
					if(startPosition < assignees_list.size() -1 ) {
		            	nach = j;
		            	tmp= assignees_list.get(++startPosition).getName();
		            }
				}				 
			}
		}
			            
		entries = new ArrayList<Entry<String, List<Issue> >>(mapAssigneesIssues.entrySet());        
					
	}
	
	public void showAsssigneeTime() {
		
		int result = 0;
		assignees_list = new ArrayList<Assignee>(assignees);
		as_time = new ArrayList<Integer>();
		List<Issue> list = new ArrayList<Issue>();
	
		for (int j = 0; j < assignees_list.size() ; j++ ) {
			list = mapAssigneesIssues.get(assignees_list.get(j).getName());
			
			if (list != null)
			for(int i = 0; i < list.size(); i++) {
				int increment = list.get(i).getTimeSpent();
				 result += increment;
			}
			
			as_time.add(result);
			
		}
	}
	
	public int sortInNaturalOrder(Object key1, Object key2) {
		return alphanumComparator.compare(key1, key2);
	}
	
	public void exportIssueList(ActionEvent actionEvent) throws JRException, IOException {		
		Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		String reportFormat = params.get("reportFormat");
		if (!Arrays.asList("pdf", "xls", "xlsx").contains(reportFormat)) return;
		
		HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();		
		ServletOutputStream outputStream = response.getOutputStream();
		
		Map<String, Object> reportParams = fillReportParamsMap();
		
		switch (reportFormat) {
			case "pdf":
				response.addHeader("Content-disposition","attachment; filename=issueReport.pdf");
				reportingService.exportToPdf("/pdfIssueReport.jasper", outputStream, reportParams, reportIssues);
				break;
			case "xls":
				response.addHeader("Content-disposition","attachment; filename=issueReport.xls");
				reportingService.exportToXls("/xlsIssueReport.jasper", outputStream, reportParams, reportIssues);
				break;
			case "xlsx":
				response.addHeader("Content-disposition","attachment; filename=issueReport.xlsx");
				reportingService.exportToXlsx("/xlsIssueReport.jasper", outputStream, reportParams, reportIssues);
			default: 
				break;
		}
		
		outputStream.flush();
		outputStream.close();
		FacesContext.getCurrentInstance().responseComplete();
	}

	private Map<String, Object> fillReportParamsMap() {
		Map<String, Object> reportParams= new HashMap<String, Object>();
		if (project_name != null && !project_name.isEmpty()) reportParams.put("project", project_name);
		if (assignees_name != null && !assignees_name.isEmpty()) {
			reportParams.put("assignees", assignees_name.toString());
		}
		if (issueType != null && !issueType.isEmpty()) reportParams.put("issueType", issueType);
		if (status != null && !status.isEmpty()) reportParams.put("status", status);
		reportParams.put("totalTimeSpent", allTime);
		
		switch (timespent) {
			case "allTime":
				reportParams.put("predefinedTimePeriod", predefinedDate);
				break;
			case "date":
				if (dateFrom != null || dateTo != null) {
					reportParams.put("fromDate", dateFrom);
					reportParams.put("toDate", dateTo);
				} else {
					reportParams.put("predefinedTimePeriod", PredefinedDateEnum.ALL_TIME.getDescription());
				}
				break;
			default: 
				break;
		}
		
		return reportParams;
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
	

	public String getPredefinedDate() {
		return predefinedDate;
	}

	public void setPredefinedDate(String predefinedDate) {
		this.predefinedDate = predefinedDate;
	}
	public List<Integer> getAs_time() {
		return as_time;
	}

	public void setAs_time(List<Integer> as_time) {
		this.as_time = as_time;
	}

	public List<Entry<String, List<Issue>>> getEntries() {
		return entries;
	}

	public void setEntries(List<Entry<String, List<Issue>>> entries) {
		this.entries = entries;
	}
	
	public Map<String, List<Issue>> getMapAssigneesIssues() {
		return mapAssigneesIssues;
	}

	public ArrayList<Assignee> getAssignees_list() {
		return assignees_list;
	}

	public void setAssignees_list(ArrayList<Assignee> assignees_list) {
		this.assignees_list = assignees_list;
	}
	
	public int getAllTime() {
		return allTime;
	}

	public void setAllTime(int allTime) {
		this.allTime = allTime;
	}
	
	public List<Issue> getReportIssues2() {
		return reportIssues2;
	}

	public void setReportIssues2(List<Issue> reportIssues2) {
		this.reportIssues2 = reportIssues2;
	}

	private int assigneeTime;
	
	public int getAssigneeTime() {
		return assigneeTime;
	}

	public void setAssigneeTime(int assigneeTime) {
		this.assigneeTime = assigneeTime;
	}


}
