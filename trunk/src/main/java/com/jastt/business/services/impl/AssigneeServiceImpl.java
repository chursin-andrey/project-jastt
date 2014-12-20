package com.jastt.business.services.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import com.jastt.business.services.AssigneeService;
import com.jastt.business.services.IssueService;
import com.jastt.business.domain.entities.Assignee;
import com.jastt.business.domain.entities.Issue;
import com.jastt.business.domain.entities.Project;
import com.jastt.dal.entities.AssigneeEntity;
import com.jastt.dal.entities.IssueEntity;
import com.jastt.dal.providers.AssigneeDataProvider;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class AssigneeServiceImpl implements AssigneeService, Serializable {

	private static final long serialVersionUID = -7926148960262045014L;
	private static final Logger LOG = LoggerFactory.getLogger(AssigneeServiceImpl.class);
	
	@Autowired
	private AssigneeDataProvider assigneeDataProvider;
	@Autowired
	private IssueService issueService;
	
	@Override
	public Assignee getAssigneeById(Integer id){
		return assigneeDataProvider.findById(AssigneeEntity.class, Assignee.class, id);
	}
	
	@Override
	public Set<Assignee> getAllAssignees() {
		Set<Assignee> assignees = new HashSet<Assignee> (assigneeDataProvider.findAll(AssigneeEntity.class, Assignee.class));	
		return  assignees;		
	}
	
	@Override
	public Set<Assignee> getAssigneesByProject(Project project) { 
		Set<Assignee> assignees = new HashSet<Assignee>();
		List<Issue> issues = issueService.getIssuesByProject(project);
		if(project != null)
		for(Issue issList : issues) {
			assignees.add(issList.getAssignee());
		}
		return assignees;
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
