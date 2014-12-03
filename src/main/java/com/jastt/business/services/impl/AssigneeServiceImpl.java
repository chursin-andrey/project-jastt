package com.jastt.business.services.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import com.jastt.business.services.AssigneeService;
import com.jastt.business.domain.entities.Assignee;
import com.jastt.business.domain.entities.Issue;
import com.jastt.business.domain.entities.Project;
import com.jastt.dal.entities.AssigneeEntity;
import com.jastt.dal.entities.IssueEntity;
import com.jastt.dal.providers.AssigneeDataProvider;

import java.util.List;

@Service
public class AssigneeServiceImpl implements AssigneeService {
	private static final Logger LOG = LoggerFactory.getLogger(AssigneeServiceImpl.class);
	
	@Autowired
	private AssigneeDataProvider assigneeDataProvider;
	
	@Override
	public List<Assignee> getAllAssignees() {
		List<Assignee>  assignees = assigneeDataProvider.findAll(AssigneeEntity.class, Assignee.class);	
		return  assignees;		
	}
	
	@Override
	public List<Assignee> getAssigneesByProject(Project project) {
		return assigneeDataProvider.getAssigneesByProject(project);
	}
	
}
