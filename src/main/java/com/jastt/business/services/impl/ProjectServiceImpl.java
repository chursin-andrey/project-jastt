package com.jastt.business.services.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import com.jastt.business.services.ProjectService;
import com.jastt.dal.providers.ProjectDataProvider;
import com.jastt.business.domain.entities.Project;
import com.jastt.business.domain.entities.User;

import java.util.List;


@Service
public class ProjectServiceImpl implements ProjectService {
	private static final Logger LOG = LoggerFactory.getLogger(ProjectServiceImpl.class);
	
	@Autowired
	private ProjectDataProvider projectDataProvider;
	
	@Override
	public List<Project> getAvailableProjectsForUser(User currentUser) {		
		return projectDataProvider.getAvailableProjectsForUser(currentUser);
	}

}
