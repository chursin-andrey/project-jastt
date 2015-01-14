package com.jastt.business.services;

import java.util.List;
import java.util.Set;

import com.jastt.business.domain.entities.Issue;
import com.jastt.business.domain.entities.Project;
import com.jastt.business.domain.entities.Worklog;
import com.jastt.dal.providers.worklog.WorklogSearchOptions;

public interface WorklogService {

	public List<Worklog> getWorklogs(Issue issue);
	public List<Worklog> getWorklogs(Project project);
	public List<Worklog> getWorklogs(WorklogSearchOptions options);
	
	public List<String> getWorklogAuthors(Project project);
	
	public void addOrUpdateWorklog(Worklog worklog);
	public void addOrUpdateWorklogs(Set<Worklog> worklogs);
}
