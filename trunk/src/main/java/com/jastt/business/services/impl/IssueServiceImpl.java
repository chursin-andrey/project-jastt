package com.jastt.business.services.impl;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.jws.soap.SOAPBinding.Use;
import javax.persistence.criteria.From;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.jastt.business.domain.entities.Assignee;
import com.jastt.business.domain.entities.Issue;
import com.jastt.business.domain.entities.Permission;
import com.jastt.business.domain.entities.Project;
import com.jastt.business.domain.entities.Server;
import com.jastt.business.domain.entities.User;
import com.jastt.business.enums.IssueStatusEnum;
import com.jastt.business.enums.IssueTypeEnum;
import com.jastt.business.enums.PredefinedDateEnum;
import com.jastt.business.services.AssigneeService;
import com.jastt.business.services.IssueService;
import com.jastt.business.services.PermissionService;
import com.jastt.business.services.ProjectService;
import com.jastt.business.services.ServerService;
import com.jastt.business.services.UserService;
import com.jastt.business.services.WorklogService;
import com.jastt.business.services.jira.JiraClientException;
import com.jastt.business.services.jira.JiraIssueService;
import com.jastt.business.services.jira.JiraProjectService;
import com.jastt.dal.entities.IssueEntity;
import com.jastt.dal.entities.ProjectEntity;
import com.jastt.dal.providers.IssueDataProvider;
import com.jastt.dal.providers.ProjectDataProvider;
import com.jastt.dal.providers.ServerDataProvider;
import com.sun.xml.bind.v2.TODO;

@Service
public class IssueServiceImpl implements IssueService, Serializable {
	
	
	private static final long serialVersionUID = 2430454333364991862L;
	private static final Logger logger = LoggerFactory.getLogger(IssueServiceImpl.class);
		
	@Autowired
	private IssueDataProvider issueDataProvider;
	@Autowired
	private JiraProjectService jiraProjectService;
	@Autowired
	private JiraIssueService jiraIssueService;	
	@Autowired
	private ProjectService projectService;	
	@Autowired
	private ServerService serverService;
	@Autowired
	private AssigneeService assigneeService; 
	@Autowired
	private PermissionService permissionService;
	@Autowired
	private WorklogService worklogService;
	
	
	@Override
	public Issue getIssueById(Integer id){
		return issueDataProvider.findById(IssueEntity.class, Issue.class, id);
	}
	
	@Override
	public List<Issue> getAllIssues() {
		List<Issue> issues = issueDataProvider.findAll(IssueEntity.class, Issue.class);	
		return issues;		
	}
	
	@Override
	public List<Issue> getIssuesByProject(Project project) {
		List<Issue> issues = issueDataProvider.getIssuesByProject(project);	
		return issues;		
	}
	
	@Override
	public List<Issue> getIssues(Project project, IssueStatusEnum status, List<Assignee> assignees,
			IssueTypeEnum issueType, Date fromDate, Date toDate) {
		
		List<Issue> issues = new ArrayList<Issue>();
		if(project == null) return null;
		try{
			if(assignees != null){
				for(Assignee asgn : assignees)
					issues.addAll(issueDataProvider.getIssues(project, status, asgn, issueType, fromDate, toDate));
			} else
				issues = issueDataProvider.getIssues(project, status, null, issueType, fromDate, toDate);
			return issues;	
		
		}catch(Exception unknownException){
			logger.error("Unknown exception happened during execution of getIssues method. ",unknownException.getMessage());
			return issues;	
		}	
	}

	
	@Override
	public List<Issue> getIssues(Project project, IssueStatusEnum status, List<Assignee> assignees,
			IssueTypeEnum issueType, PredefinedDateEnum period) {
		
		Calendar calendar = Calendar.getInstance();	
		Date fromDate = calendar.getTime();
		Date toDate = calendar.getTime();
		
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		calendar.setFirstDayOfWeek(Calendar.MONDAY);
		
		switch (period) {
			case ALL_TIME:{
				fromDate = null;
				toDate = null;
				break;
			}
			case TODAY:{
				fromDate = calendar.getTime();
				break;
			}
			case YESTERDAY:{
				toDate = calendar.getTime();					
				calendar.add(Calendar.DATE, -1);
				fromDate = calendar.getTime();
				break;
			}
			case THIS_WEEK:{
				calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());
				fromDate = calendar.getTime();
				break;
			}
			case LAST_WEEK:{
				calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
				toDate = calendar.getTime();
				calendar.add(Calendar.DATE, -7);
				fromDate = calendar.getTime();	
				break;
			}
			case LAST_SEVEN_DAYS:{
				calendar.add(Calendar.DATE, -7);
				fromDate = calendar.getTime();
				break;
			}
			case THIS_MONTH:{
				calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
				fromDate = calendar.getTime();
				break;
			}
			case LAST_MONTH:{
				calendar.add(Calendar.MONTH, -1);
				calendar.set(Calendar.DATE, 1);
				fromDate = calendar.getTime();
				calendar.set(Calendar.DATE,  calendar.getActualMaximum(Calendar.DAY_OF_MONTH)+1);
				toDate = calendar.getTime();
				break;
			}
			case LAST_THIRTY_DAYS:{
				calendar.add(Calendar.DATE, -30);
				fromDate = calendar.getTime();
				break;
			}
			case THIS_YEAR:{
				calendar.set(Calendar.DAY_OF_YEAR, 1);
				fromDate = calendar.getTime();
				break;
			}
		}
		
		List<Issue> issues = new ArrayList<Issue>();
		issues = getIssues(project, status, assignees, issueType, fromDate, toDate);
		/* TODO Here is must be invocation of getIssues method with from and to date, for example:
		 * issues = getIssues(project, status, assignees, issueType, fromDate, toDate);
		 * or
		 * return getIssues(project, status, assignees, issueType, fromDate, toDate);
		 */
		
		return issues ;
	}

	@Override
	public void update(User user) throws JiraClientException {
		try{
			Set<Project> projects_set = jiraProjectService.getAllProjects(user);
			List<Project> projects = new ArrayList<Project>(projects_set);
						
			if(projects.isEmpty()){
				logger.info("User "+user.getLogin()+" has none of projects.");
				return;	
			}
			
					
			for(Project projectFromJira : projects){
		
				Project project = projectService.getProjectByName(projectFromJira.getName());		
				
				if(project == null){				
				/* if project  does not exist in a database	*/
											
					projectFromJira.setServer(user.getServer());
					projectService.addProject(projectFromJira);
					
					project = projectService.getProjectByName(projectFromJira.getName());
					Permission permission = permissionService.getPermission(user, project);
					if(permission == null){
						permissionService.addPermission(user, project);
					}
						
					Set<Issue> issuesSet = jiraIssueService.getAllIssuesForProject(user, project); 			
					saveIssues(issuesSet);
						
				}else{			
				/* if project already exist in a database */
					
					Permission permission = permissionService.getPermission(user, project);
					if(permission == null){
						permissionService.addPermission(user, project);
					}
								
					Issue latestIssue = issueDataProvider.getLatestIssue(project);
													
					if(latestIssue==null){
			
						Set<Issue> issuesSet = jiraIssueService.getAllIssuesForProject(user, projectFromJira);
						saveIssues(issuesSet);
												
					}else{	
									
						DateTime fromDate = new DateTime(latestIssue.getCreated());	
						Set<Issue> issuesSet = jiraIssueService.getAllIssuesForProject(user, projectFromJira, fromDate);
						saveIssues(issuesSet);									
					}				
				}
								
			
			}
			
			
		} catch(JiraClientException jiraClientException){
			logger.error("JiraClientException happened during execution of update method. CODE: "+jiraClientException.getStatusCode());
		}catch(Exception unknownException){
			logger.error("Unknown exception happened during execution of update method. ",unknownException.getMessage());
		}
		
	}
	

	
	private void saveIssues(Set<Issue> issuesSet){
		List<Issue> issues = new ArrayList<Issue>(issuesSet);
		
		for(Issue issueFromJira : issues){
			Issue issueFormDataBase = issueDataProvider.getIssueByKey(issueFromJira.getKey());
			
			if(issueFormDataBase == null){
				Issue issue = prepareToPersist(issueFromJira);		
				issueDataProvider.save(issue, IssueEntity.class);	
				worklogService.addOrUpdateWorklogs(issue.getWorklogs());
					
			}else{
				Date upTimeFromJira = issueFromJira.getUpdated();
				Date upTimeFromDataBase = issueFormDataBase.getUpdated();
							
				if(!upTimeFromJira .equals(upTimeFromDataBase )){					
					
					Issue issue = prepareToPersist(issueFromJira);	
					issue.setId(issueFormDataBase.getId());
					issueDataProvider.save(issue, IssueEntity.class);	
					worklogService.addOrUpdateWorklogs(issue.getWorklogs());		
				}			
				
			}
		
		}
			
	}
	
	
	private Issue prepareToPersist(Issue issue){
		String projectName = issue.getProject().getName();
		String assigneeName =  issue.getAssignee().getName();
		
		Assignee assignee = assigneeService.getAssigneeByName(assigneeName);	
		if(assignee == null){
			assigneeService.addAssignee(issue.getAssignee());
		}
		
		issue.setAssignee(assigneeService.getAssigneeByName(assigneeName));
		issue.setProject(projectService.getProjectByName(projectName));
		
		return issue;
	}

	
		
}
