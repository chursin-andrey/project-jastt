package com.jastt.dal.providers;

import java.io.Serializable;
import java.util.List;

import com.jastt.business.domain.entities.Issue;
import com.jastt.business.domain.entities.Worklog;
import com.jastt.dal.entities.WorklogEntity;
import com.jastt.dal.providers.base.PageableDataProvider;

public interface WorklogDataProvider extends
		PageableDataProvider<WorklogEntity, Worklog, Integer>, Serializable {

	public List<Worklog> getWorklogs(Issue issue);
}
