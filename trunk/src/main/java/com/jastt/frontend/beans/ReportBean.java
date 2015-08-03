package com.jastt.frontend.beans;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.TreeSet;

import javax.annotation.PostConstruct;
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
import com.jastt.business.domain.entities.User;
import com.jastt.business.enums.IssueStatusEnum;
import com.jastt.business.enums.IssueTypeEnum;
import com.jastt.business.services.AssigneeService;
import com.jastt.business.services.IssueService;
import com.jastt.business.services.PermissionService;
import com.jastt.business.services.ProjectService;
import com.jastt.business.services.ReportingService;
import com.jastt.business.services.jira.JiraClientException;
import com.jastt.business.enums.PredefinedDateEnum;
import com.jastt.frontend.utils.Faces;

@Component
@Scope("session")
public class ReportBean implements Serializable{

	private static final long serialVersionUID = 7688215600325304973L;
	
	private List<Issue> issues;
	private List<Project> projects;
	private Set<Assignee> assignees;
	private Set<String> assignees_name;
	private List<Entry<String, List<Issue> >> entries;
	private Map<String, List<Issue>> mapAssigneesIssues = new HashMap<String, List<Issue>>();	
	private List<Permission> permissions;
	private Set<String> issueType;
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
	private String timespent = "allTime";
	private Set<String> status;
	private String predefinedDate;
	private int allTime;
	
	private List<Issue> reportIssues;
	private List<Issue> reportIssuesByUser;
	private List<IssueGrouping> byUserGrouping;
	private List<Issue> reportIssuesByType;
	private List<IssueGrouping> byTypeGrouping;
	private List<Issue> reportIssuesByComponent;
	private List<IssueGrouping> byComponentGrouping;
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
		Collection<IssueTypeEnum> type = null;
		Collection<IssueStatusEnum> issueStatus = null;
//		if(reportIssues != null) reportIssues.clear();
//		if(reportIssuesByUser != null) reportIssuesByUser.clear();
//		if(reportIssuesByType != null) reportIssuesByType.clear();
//		if(reportIssuesByComponent != null) reportIssuesByComponent.clear();

		if(issueType != null) type = IssueTypeEnum.getTypes(issueType);
		//TODO the enum is not needed. getIssues() can work with Strings
		if(status != null && !status.isEmpty())	issueStatus = getTypes();
//		reportIssues = new ArrayList<Issue>();
//		reportIssuesByUser = new ArrayList<Issue>();
//		reportIssuesByType = new ArrayList<Issue>();
//		reportIssuesByComponent = new ArrayList<Issue>();
		
		reportAssignees = new ArrayList<Assignee>();
		if (assignees_name != null)
			if (assignees_name.size() != 0) {
				for (String assign : assignees_name) {
					reportAssignees.add(assigneeService
							.getAssigneeByName(assign));
				}
			}
		if(timespent.equals("date"))
			reportIssues = issueService.getIssues(project, issueStatus, reportAssignees, type, dateFrom, dateTo);
		else
			reportIssues = issueService.getIssues(project, issueStatus, reportAssignees, type, PredefinedDateEnum.getType(predefinedDate));

						
		reportIssuesByUser = new ArrayList<Issue>(reportIssues);
		reportIssuesByType = new ArrayList<Issue>(reportIssues);
		reportIssuesByComponent = new ArrayList<Issue>(reportIssues);
		
		addAssigneesName();
		Collections.sort(reportIssues, Issue.getNameComparator());
		Collections.sort(reportIssuesByUser, Issue.getNameComparator());
		byUserGrouping = createByUserGrouping(reportIssuesByUser);
		Collections.sort(reportIssuesByType, Issue.getTypeComparator());
		byTypeGrouping = createByTypeGrouping(reportIssuesByType);
		Collections.sort(reportIssuesByComponent, Issue.getComponentComparator());
		byComponentGrouping = createByComponentGrouping(reportIssuesByComponent);
		showAllTime(reportIssues);
		addMapIssue(reportIssues);
		
	}

        private List<IssueGrouping> createByTypeGrouping(List<Issue> reportIssues) {
            HashMap<String, IssueGrouping> groupings = new HashMap<String, IssueGrouping>();
            for (Issue issue : reportIssues) {
        	String name = issue.getIssueType();
        	IssueGrouping issueGrouping = groupings.get(name);
        	if (issueGrouping==null) {
        	    issueGrouping = new IssueGrouping(name);
        	    groupings.put(name, issueGrouping);
        	}
        	issueGrouping.addIssue(issue);
	    }
            List<IssueGrouping> arrayList = new ArrayList<IssueGrouping>(groupings.values());
            Collections.sort(arrayList);
	    return arrayList;
        }

        private List<IssueGrouping> createByComponentGrouping(List<Issue> reportIssues) {
            HashMap<String, IssueGrouping> groupings = new HashMap<String, IssueGrouping>();
            for (Issue issue : reportIssues) {
        	String name = issue.getComponent();
        	IssueGrouping issueGrouping = groupings.get(name);
        	if (issueGrouping==null) {
        	    issueGrouping = new IssueGrouping(name);
        	    groupings.put(name, issueGrouping);
        	}
        	issueGrouping.addIssue(issue);
	    }
            List<IssueGrouping> arrayList = new ArrayList<IssueGrouping>(groupings.values());
            Collections.sort(arrayList);
	    return arrayList;
        }

        private List<IssueGrouping> createByUserGrouping(List<Issue> reportIssues) {
            HashMap<String, IssueGrouping> groupings = new HashMap<String, IssueGrouping>();
            for (Issue issue : reportIssues) {
        	String name = issue.getAssignee().getName();
        	IssueGrouping issueGrouping = groupings.get(name);
        	if (issueGrouping==null) {
        	    issueGrouping = new IssueGrouping(name);
        	    groupings.put(name, issueGrouping);
        	}
        	issueGrouping.addIssue(issue);
	    }
            List<IssueGrouping> arrayList = new ArrayList<IssueGrouping>(groupings.values());
            Collections.sort(arrayList);
	    return arrayList;
        }

	private Collection<IssueStatusEnum> getTypes() {
		Collection<IssueStatusEnum> issueStatus = new HashSet<IssueStatusEnum>();
		for (String s : status) {
			IssueStatusEnum e = IssueStatusEnum.getType(s);
			issueStatus.add(e); 
		}
		return issueStatus;
	}
	
	private void addAssigneesName() {
		for(Assignee as : assignees) {
			as.getName(); //???
		 }
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
		if (status != null) status.clear();
		disableMenu = true;
		
	}
	public void cancelAction(){
		reportIssues.clear();
		reportIssuesByUser.clear();
		reportIssuesByType.clear();
		reportIssuesByComponent.clear();
		setAllTime(0);
		mapAssigneesIssues.clear();
		entries.clear();
	}
	
	public void addMapIssue(List<Issue> list) {
       
        for (Issue issue : list) {
        	String assigneeName = issue.getAssignee().getName();
        	List<Issue> issuesOfAssignee = mapAssigneesIssues.get(assigneeName);
        	if (issuesOfAssignee == null){
        		issuesOfAssignee = new ArrayList<Issue>();
        		mapAssigneesIssues.put(assigneeName, issuesOfAssignee);
        	}
        	issuesOfAssignee.add(issue);
		}
			            
		entries = new ArrayList<Entry<String, List<Issue> >>(mapAssigneesIssues.entrySet());        
					
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

	public void exportByUserList(ActionEvent actionEvent) throws JRException, IOException {		
		Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		String reportFormat = params.get("reportFormat");
		if (!Arrays.asList("pdf", "xls", "xlsx").contains(reportFormat)) return;
		
		HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();		
		ServletOutputStream outputStream = response.getOutputStream();
		
		Map<String, Object> reportParams = fillReportParamsMap();
		
		switch (reportFormat) {
			case "pdf":
				response.addHeader("Content-disposition","attachment; filename=byUserReport.pdf");
				reportingService.exportToPdf("/pdfByUserReport.jasper", outputStream, reportParams, reportIssuesByUser);
				break;
			case "xls":
				response.addHeader("Content-disposition","attachment; filename=byUserReport.xls");
				reportingService.exportToXls("/xlsByUserReport.jasper", outputStream, reportParams, reportIssuesByUser);
				break;
			case "xlsx":
				response.addHeader("Content-disposition","attachment; filename=byUserReport.xlsx");
				reportingService.exportToXlsx("/xlsByUserReport.jasper", outputStream, reportParams, reportIssuesByUser);
			default: 
				break;
		}
		
		outputStream.flush();
		outputStream.close();
		FacesContext.getCurrentInstance().responseComplete();
	}
	
	public void exportByTypeList(ActionEvent actionEvent) throws JRException, IOException {		
		Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		String reportFormat = params.get("reportFormat");
		if (!Arrays.asList("pdf", "xls", "xlsx").contains(reportFormat)) return;
		
		HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();		
		ServletOutputStream outputStream = response.getOutputStream();
		
		Map<String, Object> reportParams = fillReportParamsMap();
//		Collections.sort(reportIssues, Issue.getTypeComparator());
		switch (reportFormat) {
			case "pdf":
				response.addHeader("Content-disposition","attachment; filename=byTypeReport.pdf");
				reportingService.exportToPdf("/pdfByTypeReport.jasper", outputStream, reportParams, reportIssuesByType);
				break;
			case "xls":
				response.addHeader("Content-disposition","attachment; filename=byTypeReport.xls");
				reportingService.exportToXls("/xlsByTypeReport.jasper", outputStream, reportParams, reportIssuesByType);
				break;
			case "xlsx":
				response.addHeader("Content-disposition","attachment; filename=byTypeReport.xlsx");
				reportingService.exportToXlsx("/xlsByTypeReport.jasper", outputStream, reportParams, reportIssuesByType);
			default: 
				break;
		}
		
		outputStream.flush();
		outputStream.close();
		FacesContext.getCurrentInstance().responseComplete();
	}
	
	public void exportByComponentList(ActionEvent actionEvent) throws JRException, IOException {		
		Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		String reportFormat = params.get("reportFormat");
		if (!Arrays.asList("pdf", "xls", "xlsx").contains(reportFormat)) return;
		
		HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();		
		ServletOutputStream outputStream = response.getOutputStream();
		
		Map<String, Object> reportParams = fillReportParamsMap();
		
		switch (reportFormat) {
			case "pdf":
				response.addHeader("Content-disposition","attachment; filename=byComponentReport.pdf");
				reportingService.exportToPdf("/pdfByComponentReport.jasper", outputStream, reportParams, reportIssuesByComponent);
				break;
			case "xls":
				response.addHeader("Content-disposition","attachment; filename=byComponentReport.xls");
				reportingService.exportToXls("/xlsByComponentReport.jasper", outputStream, reportParams, reportIssuesByComponent);
				break;
			case "xlsx":
				response.addHeader("Content-disposition","attachment; filename=byComponentReport.xlsx");
				reportingService.exportToXlsx("/xlsByComponentReport.jasper", outputStream, reportParams, reportIssuesByComponent);
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
		if (issueType != null && !issueType.isEmpty()) reportParams.put("issueType", issueType.toString());
		if (status != null && !status.isEmpty()) reportParams.put("status", status.toString());
		reportParams.put("totalTimeSpent", allTime);
		
		if (timespent != null)
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

	public Set<String> getStatus() {
		return status;
	}

	public void setStatus(Set<String> status) {
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
	
	public Collection<Assignee> getAssignees() {
		TreeSet<Assignee> treeSet = new TreeSet<Assignee>(
				Assignee.getAssigneeNameComparator());
		treeSet.addAll(assignees);
		return treeSet;
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

	public Set<String> getIssueType() {
		return issueType;
	}

	public void setIssueType(Set<String> issueType) {
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

	public List<Entry<String, List<Issue>>> getEntries() {
		return entries;
	}

	public void setEntries(List<Entry<String, List<Issue>>> entries) {
		this.entries = entries;
	}
	
	public Map<String, List<Issue>> getMapAssigneesIssues() {
		return mapAssigneesIssues;
	}

	public int getAllTime() {
		return allTime;
	}

	public void setAllTime(int allTime) {
		this.allTime = allTime;
	}

	private int assigneeTime;
	
	public int getAssigneeTime() {
		return assigneeTime;
	}

	public void setAssigneeTime(int assigneeTime) {
		this.assigneeTime = assigneeTime;
	}

	public List<Issue> getReportIssuesByUser() {
	    return reportIssuesByUser;
	}

	public List<Issue> getReportIssuesByType() {
	    return reportIssuesByType;
	}

	public List<Issue> getReportIssuesByComponent() {
	    return reportIssuesByComponent;
	}

	public List<IssueGrouping> getByUserGrouping() {
	    return byUserGrouping;
	}

	public List<IssueGrouping> getByTypeGrouping() {
	    return byTypeGrouping;
	}

	public List<IssueGrouping> getByComponentGrouping() {
	    return byComponentGrouping;
	}


}
