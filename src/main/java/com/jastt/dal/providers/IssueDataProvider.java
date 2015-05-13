package com.jastt.dal.providers;



import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import com.jastt.dal.entities.IssueEntity;
import com.jastt.dal.providers.base.PageableDataProvider;
import com.jastt.business.domain.entities.Assignee;
import com.jastt.business.domain.entities.Issue;
import com.jastt.business.domain.entities.Project;
import com.jastt.business.enums.*;



public interface IssueDataProvider extends PageableDataProvider<IssueEntity, Issue, Integer>, Serializable {

	public Issue getIssueByKey(String key);
	public Issue getIssueByStatus(String status);
	public List<Issue> getIssuesByProject(Project project);
	
	public Issue getLatestIssue();
	
	public void saveIssues(List<Issue> issues);
	
	public Issue getLatestIssue(Project  projects);
	
	public List<Issue> getIssues(Project project, Collection<IssueStatusEnum> status, Assignee assignee, Collection<IssueTypeEnum> issueType, Date fromDate, Date toDate);
}
