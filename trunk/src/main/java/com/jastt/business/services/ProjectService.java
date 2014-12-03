package com.jastt.business.services;

import com.jastt.business.domain.entities.Project;
import com.jastt.business.domain.entities.User;

import java.util.*;

public interface ProjectService {
	public List<Project> getAllProjects();
	public List<Project> getAvailableProjectsForUser(User currentUser);
}
