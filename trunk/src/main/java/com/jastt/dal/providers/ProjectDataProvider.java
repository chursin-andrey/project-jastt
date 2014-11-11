package com.jastt.dal.providers;

import java.util.List;

import com.jastt.dal.entities.ProjectEntity;
import com.jastt.dal.providers.base.PageableDataProvider;
import com.jastt.business.domain.entities.Project;



public interface ProjectDataProvider extends PageableDataProvider<ProjectEntity, Project, Integer> {

	Project getProjectByName(String name);
	Project getProjectByKey(String key);
	List<Project> getAllProjects();
}
