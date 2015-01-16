package com.jastt.frontend.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.event.ValueChangeEvent;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.jastt.business.domain.entities.Permission;
import com.jastt.business.domain.entities.Project;
import com.jastt.business.domain.entities.User;
import com.jastt.business.domain.entities.Worklog;
import com.jastt.business.services.PermissionService;
import com.jastt.business.services.ProjectService;
import com.jastt.business.services.WorklogService;
import com.jastt.dal.providers.worklog.WorklogSearchOptions;

@Component
@Scope("session")
public class WorklogBean implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Autowired
	private ProjectService projectService;
	@Autowired
	private WorklogService worklogService;
	@Autowired
	private PermissionService permissionService;

	private boolean disableMenu = true;
	//items of UI components
	private List<Project> projectList = new ArrayList<Project>();
	private List<String> authorList = new ArrayList<String>();
	private List<String> issueTypeList = new ArrayList<String>();
	private List<String> issueStatusList = new ArrayList<String>();
	private List<Worklog> worklogList = new ArrayList<Worklog>();
	//values of UI components
	private String currProjectName = "";
	private List<String> currAuthors = new ArrayList<String>();
	private String currIssueType = "";
	private String currIssueStatus = "";
	
	@PostConstruct
	public void init() {
		Subject subject = SecurityUtils.getSubject();	
		User user = (User) subject.getSession().getAttribute("user");
		if (user == null) return;
		
		List<Permission> userPermissions = permissionService.getPermissionsByUser(user);
		for (Permission prm : userPermissions) {
			Project tmp = prm.getProject();
			projectList.add(tmp);
		}
		
//		projectList = projectService.getAllProjects();
	}
	
	public void showButtonClick() {
		worklogList.clear();
		Project currProject = findProjectByName(currProjectName);
		if (currProject != null) {
			WorklogSearchOptions searchOptions = new WorklogSearchOptions();
			
			searchOptions.setProject(currProject);
			searchOptions.setAuthors(currAuthors);
			if (!currIssueType.isEmpty()) searchOptions.setIssueType(currIssueType);
			if (!currIssueStatus.isEmpty()) searchOptions.setIssueStatus(currIssueStatus);
		
			worklogList = worklogService.getWorklogs(searchOptions);
		}
	}
	
	public void projectChanged() {
		resetControls();
		Project currProject = findProjectByName(currProjectName);
		if (currProject != null) {
			authorList = worklogService.getWorklogAuthors(currProject);
			issueTypeList = worklogService.getWorklogIssueTypes(currProject);
			issueStatusList = worklogService.getWorklogIssueStatuses(currProject);
			disableMenu = false;
		}
	}
	
	private void resetControls() {
		currAuthors.clear();
		authorList.clear();
		
		currIssueType = "";
		issueTypeList.clear();
		
		currIssueStatus = "";
		issueStatusList.clear();
		
		disableMenu = true;
	}
	
	private Project findProjectByName(String projectName) {
		for (Project project : projectList) {
			if (project.getName().equals(projectName)) return project;
		}
		
		return null;
	}

	public List<Project> getProjectList() {
		return projectList;
	}

	public void setProjectList(List<Project> projects) {
		this.projectList = projects;
	}

	public String getCurrProjectName() {
		return currProjectName;
	}

	public void setCurrProjectName(String currProjectName) {
		this.currProjectName = currProjectName;
	}

	public List<Worklog> getWorklogList() {
		return worklogList;
	}

	public void setWorklogList(List<Worklog> worklogList) {
		this.worklogList = worklogList;
	}

	public List<String> getAuthorList() {
		return authorList;
	}

	public void setAuthorList(List<String> authorList) {
		this.authorList = authorList;
	}

	public List<String> getCurrAuthors() {
		return currAuthors;
	}

	public void setCurrAuthors(List<String> currAuthors) {
		this.currAuthors = currAuthors;
	}

	public boolean isDisableMenu() {
		return disableMenu;
	}

	public void setDisableMenu(boolean disableMenu) {
		this.disableMenu = disableMenu;
	}

	public List<String> getIssueTypeList() {
		return issueTypeList;
	}

	public void setIssueTypeList(List<String> issueTypeList) {
		this.issueTypeList = issueTypeList;
	}

	public List<String> getIssueStatusList() {
		return issueStatusList;
	}

	public void setIssueStatusList(List<String> issueStatusList) {
		this.issueStatusList = issueStatusList;
	}

	public String getCurrIssueType() {
		return currIssueType;
	}

	public void setCurrIssueType(String currIssueType) {
		this.currIssueType = currIssueType;
	}

	public String getCurrIssueStatus() {
		return currIssueStatus;
	}

	public void setCurrIssueStatus(String currIssueStatus) {
		this.currIssueStatus = currIssueStatus;
	}
}
