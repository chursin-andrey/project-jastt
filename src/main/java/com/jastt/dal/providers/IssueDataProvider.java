package com.jastt.dal.providers;



import com.jastt.dal.entities.IssueEntity;
import com.jastt.dal.providers.base.PageableDataProvider;
import com.jastt.business.domain.entities.Issue;
import com.jastt.business.domain.entities.Project;



public interface IssueDataProvider extends PageableDataProvider<IssueEntity, Issue, Integer> {

	Issue getIssueByKey(String key);
	Issue getIssueByStatus(String status);
	Issue getIssueByProject(Project project);
}
