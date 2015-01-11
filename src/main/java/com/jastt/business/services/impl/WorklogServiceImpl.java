package com.jastt.business.services.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jastt.business.domain.entities.Issue;
import com.jastt.business.domain.entities.Worklog;
import com.jastt.business.services.WorklogService;
import com.jastt.dal.entities.WorklogEntity;
import com.jastt.dal.providers.WorklogDataProvider;

@Service
public class WorklogServiceImpl implements WorklogService, Serializable {
	private static final long serialVersionUID = 1L;
	
	@Autowired
	private WorklogDataProvider worklogDataProvider;

	@Override
	public List<Worklog> getWorklogs(Issue issue) {
		List<Worklog> worklogList = new ArrayList<Worklog>();
		
		if (issue != null && issue.getId() != null)
			worklogList = worklogDataProvider.getWorklogs(issue);
		
		return worklogList;
	}

	@Override
	public void addOrUpdateWorklog(Worklog worklog) {
		if (worklog == null || worklog.getSelf() == null || worklog.getSelf().isEmpty()) return;
		
		Worklog oldWorklog = worklogDataProvider.getWorklogBySelf(worklog.getSelf());
		if (oldWorklog != null) worklog.setId(oldWorklog.getId());
		worklogDataProvider.save(worklog, WorklogEntity.class);
	}

	@Override
	public void addOrUpdateWorklogs(Set<Worklog> worklogs) {
		if (worklogs == null) return;
		for (Worklog worklog : worklogs) {
			addOrUpdateWorklog(worklog);
		}
	}

}
