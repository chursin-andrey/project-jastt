package com.jastt.business.services.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jastt.business.domain.entities.Issue;
import com.jastt.business.domain.entities.Worklog;
import com.jastt.business.services.WorklogService;
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

}
