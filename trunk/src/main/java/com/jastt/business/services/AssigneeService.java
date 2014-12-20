package com.jastt.business.services;

import com.jastt.business.domain.entities.Assignee;
import com.jastt.business.domain.entities.Project;
import com.jastt.business.enums.IssueTypeEnum;

import java.util.*;

public interface AssigneeService {
	public Assignee getAssigneeById(Integer id);
	public Set<Assignee> getAllAssignees();
	public Set<Assignee> getAssigneesByProject(Project project);
	public Assignee getAssigneeByName(String name);
	public void addAssignee(Assignee assignee);
}
