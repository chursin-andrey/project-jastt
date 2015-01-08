package com.jastt.business.services;

import java.util.List;

import com.jastt.business.domain.entities.Issue;
import com.jastt.business.domain.entities.Worklog;

public interface WorklogService {

	public List<Worklog> getWorklogs(Issue issue);
}
