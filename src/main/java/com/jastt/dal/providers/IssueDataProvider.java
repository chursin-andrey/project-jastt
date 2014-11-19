package com.jastt.dal.providers;



import java.util.Date;
import java.util.List;

import com.jastt.dal.entities.IssueEntity;
import com.jastt.dal.providers.base.PageableDataProvider;
import com.jastt.business.domain.entities.Assignee;
import com.jastt.business.domain.entities.Issue;
import com.jastt.business.domain.entities.Project;
import com.jastt.business.enums.*;



public interface IssueDataProvider extends PageableDataProvider<IssueEntity, Issue, Integer> {

	public Issue getIssueByKey(String key);
	public Issue getIssueByStatus(String status);
	public Issue getIssueByProject(Project project);
	
	public Issue getLatestIssue();
	
	public void saveIssues(List<Issue> issues);
	
	public Issue getLatestIssue(List<Project>  projects);
	
	public List<Issue> getIssues(Project project, IssueStatusEnum status, List<Assignee> assignees, IssueTypeEnum issueType, Date fromDate, Date toDate);
}
