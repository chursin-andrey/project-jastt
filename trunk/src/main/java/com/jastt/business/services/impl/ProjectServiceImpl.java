package com.jastt.business.services.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import com.jastt.business.services.ProjectService;
import com.jastt.dal.entities.ProjectEntity;
import com.jastt.dal.providers.ProjectDataProvider;
import com.jastt.business.domain.entities.Project;



import com.jastt.business.domain.entities.User;

import java.io.Serializable;
import java.util.List;


@Service
public class ProjectServiceImpl implements ProjectService, Serializable {

	private static final long serialVersionUID = 3954434930493958668L;

	private static final Logger LOG = LoggerFactory.getLogger(ProjectServiceImpl.class);
	
	@Autowired
	private ProjectDataProvider projectDataProvider;
	
	@Override
	public Project getProjectById(Integer id) {
		return projectDataProvider.findById(ProjectEntity.class, Project.class, id);
	}
	
	@Override
	public Project getProjectByName(String name) {
		return projectDataProvider.getProjectByName(name);
	}
	
	@Override
	public List<Project> getAllProjects() {
		List<Project> projects = projectDataProvider.findAll(ProjectEntity.class, Project.class);	
		return projects;		
	}
	
	@Override
	public List<Project> getAvailableProjectsForUser(User currentUser) {		
		return projectDataProvider.getAvailableProjectsForUser(currentUser);
	}

	@Override
	public void addProject(Project project) {
		projectDataProvider.save(project, ProjectEntity.class);
	}

}
