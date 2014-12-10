package com.jastt.business.services;

import com.jastt.business.domain.entities.Project;
import com.jastt.business.domain.entities.User;
import com.jastt.dal.entities.ProjectEntity;

import java.util.*;

public interface ProjectService {
	public Project getProjectById(Integer id);
	public Project getProjectByName(String name);
	public List<Project> getAllProjects();
	public List<Project> getAvailableProjectsForUser(User currentUser);
}
