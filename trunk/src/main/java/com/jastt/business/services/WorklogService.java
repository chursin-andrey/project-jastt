package com.jastt.business.services;

import java.util.List;
import java.util.Set;

import com.jastt.business.domain.entities.Issue;
import com.jastt.business.domain.entities.Worklog;

public interface WorklogService {

	public List<Worklog> getWorklogs(Issue issue);
	public void addOrUpdateWorklog(Worklog worklog);
	public void addOrUpdateWorklogs(Set<Worklog> worklogs);
}
