package com.jastt.dal.providers;

import java.util.List;

import com.jastt.dal.entities.AssigneeEntity;
import com.jastt.dal.providers.base.PageableDataProvider;
import com.jastt.business.domain.entities.Assignee;
import com.jastt.business.domain.entities.Issue;
import com.jastt.business.domain.entities.Project;



public interface AssigneeDataProvider extends PageableDataProvider<AssigneeEntity, Assignee, Integer> {

	public List<Assignee> getAssigneesByProject(Project project);
	public Assignee getAssigneeByName(String name);
}
