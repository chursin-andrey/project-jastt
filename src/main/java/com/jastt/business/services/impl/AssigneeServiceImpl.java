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

import java.io.Serializable;
import java.util.List;

@Service
public class AssigneeServiceImpl implements AssigneeService, Serializable {

	private static final long serialVersionUID = -7926148960262045014L;
	private static final Logger LOG = LoggerFactory.getLogger(AssigneeServiceImpl.class);
	
	@Autowired
	private AssigneeDataProvider assigneeDataProvider;
	
	@Override
	public Assignee getAssigneeById(Integer id){
		return assigneeDataProvider.findById(AssigneeEntity.class, Assignee.class, id);
	}
	
	@Override
	public List<Assignee> getAllAssignees() {
		List<Assignee>  assignees = assigneeDataProvider.findAll(AssigneeEntity.class, Assignee.class);	
		return  assignees;		
	}
	
	@Override
	public List<Assignee> getAssigneesByProject(Project project) {
		return assigneeDataProvider.getAssigneesByProject(project);
	}

	@Override
	public Assignee getAssigneeByName(String name) {
		return assigneeDataProvider.getAssigneeByName(name);
	}

	@Override
	public void addAssignee(Assignee assignee) {
		assigneeDataProvider.save(assignee, AssigneeEntity.class);	
	}
	
}
