package com.jastt.dal.providers;

import java.io.Serializable;
import java.util.List;

import com.jastt.business.domain.entities.Issue;
import com.jastt.business.domain.entities.Project;
import com.jastt.business.domain.entities.Worklog;
import com.jastt.dal.entities.WorklogEntity;
import com.jastt.dal.providers.base.PageableDataProvider;
import com.jastt.dal.providers.worklog.WorklogSearchOptions;

public interface WorklogDataProvider extends
		PageableDataProvider<WorklogEntity, Worklog, Integer>, Serializable {

	public List<Worklog> getWorklogs(Issue issue);
	public List<Worklog> getWorklogs(Project project);
	public List<Worklog> getWorklogs(WorklogSearchOptions options);
	
	public List<String> getWorklogAuthors(Project project);
	
	public Worklog getWorklogBySelf(String self);
}
