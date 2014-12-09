package com.jastt.business.services.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jastt.business.domain.entities.Assignee;
import com.jastt.business.domain.entities.Issue;
import com.jastt.business.domain.entities.Project;
import com.jastt.business.domain.entities.Server;
import com.jastt.business.domain.entities.User;
import com.jastt.business.enums.IssueStatusEnum;
import com.jastt.business.enums.IssueTypeEnum;
import com.jastt.business.enums.PredefinedDateEnum;
import com.jastt.business.services.IssueService;
import com.jastt.business.services.jira.JiraClientException;
import com.jastt.business.services.jira.JiraIssueService;
import com.jastt.business.services.jira.JiraProjectService;
import com.jastt.dal.entities.IssueEntity;
import com.jastt.dal.providers.IssueDataProvider;
import com.sun.xml.bind.v2.TODO;

@Service
public class IssueServiceImpl implements IssueService {
	
	private static final Logger logger = LoggerFactory.getLogger(IssueServiceImpl.class);
	
	
	@Autowired
	private IssueDataProvider issueDataProvider;
	@Autowired
	private JiraProjectService jiraProjectService;
	@Autowired
	private JiraIssueService jiraIssueService;
	
	@Override
	public List<Issue> getAllIssues() {
		List<Issue> issues = issueDataProvider.findAll(IssueEntity.class, Issue.class);	
		return issues;		
	}
	
	@Override
	public List<Issue> getIssues(Project project, IssueStatusEnum status, List<Assignee> assignees,
			IssueTypeEnum issueType, Date fromDate, Date toDate) {
		
		List<Issue> issues = new ArrayList<Issue>();
		
		try{	
			issues = issueDataProvider.getIssues(project, status, assignees, issueType, fromDate, toDate);
			return issues;	
		
		}catch(Exception unknownException){
			logger.error("Unknown exception happened during execution of getIssues method. ",unknownException.getMessage());
			return issues;	
		}	
	}

	
	@Override
	public List<Issue> getIssues(Project project, IssueStatusEnum status, List<Assignee> assignees,
			IssueTypeEnum issueType, PredefinedDateEnum period) {
		
		List<Issue> issues = new ArrayList<Issue>();
		
		Calendar calendar = Calendar.getInstance();
		Date fromDate = new Date();
		Date toDate = new Date();
		
		
		switch (period) {
			case ALL_TIME:{
				fromDate = null;
				toDate = null;
				break;
			}
			case TODAY:{
				//TODO
				break;
			}
			case YESTERDAY:{
				//TODO
				break;
			}
			case THIS_WEEK:{
				//TODO
				break;
			}
			case LAST_WEEK:{
				//TODO
				break;
			}
			case LAST_SEVEN_DAYS:{
				//TODO
				break;
			}
			case THIS_MONTH:{
				//TODO
				break;
			}
			case LAST_MONTH:{
				//TODO
				break;
			}
			case LAST_THIRTY_DAYS:{
				//TODO
				break;
			}
			case THIS_YEAR:{
				//TODO
				break;
			}
		}
			
		issues = getIssues(project, status, assignees, issueType, fromDate, toDate);
		
		return issues;
	}

	
	@Override
	public void update(User user){
		
		try{	
			Set<Project> projects_set = jiraProjectService.getAllProjects(user);
			List<Project> projects = new ArrayList<Project>(projects_set);
			
			
			if(projects.isEmpty()){
				logger.info("User "+user.getLogin()+" has none of projects.");
				return;	
			}
			
			List<Issue> issues = new ArrayList<Issue>();
			Issue latestIssue = issueDataProvider.getLatestIssue(projects);
			
			if(latestIssue == null){
				for(Project project : projects){		
					
					Set<Issue> newIssues = jiraIssueService.getAllIssuesForProject(user, project);	
					Iterator<Issue> iter = newIssues.iterator();
					while(iter.hasNext()){
						issues.add(iter.next());
					}
					
				}
			}else{
				for(Project project : projects){
					
					DateTime fromDate = new DateTime(latestIssue.getCreated());
					Set<Issue> newIssues = jiraIssueService.getAllIssuesForProject(user, project, fromDate);		
					Iterator<Issue> iter = newIssues.iterator();
					while(iter.hasNext()){
						issues.add(iter.next());
					}	
					
				}
			}
			
			if(!issues.isEmpty()){
				issueDataProvider.saveIssues(issues);
			}
			
		}catch(JiraClientException jiraClientException){
			logger.error("JiraClientException happened during execution of update method. ",jiraClientException.getStatusCode());
		}catch(Exception unknownException){
			logger.error("Unknown exception happened during execution of update method. ",unknownException.getMessage());
		}
				
	}
	
	
	/*@Override
	public void update(User user) throws JiraClientException{
			Set<Project> projects_set = jiraProjectService.getAllProjects(user);		
	}	*/
		
	
}
