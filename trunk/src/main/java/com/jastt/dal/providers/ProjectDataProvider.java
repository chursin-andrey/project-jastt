package com.jastt.dal.providers;

import java.io.Serializable;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.jastt.dal.entities.ProjectEntity;
import com.jastt.dal.providers.base.PageableDataProvider;
import com.jastt.business.domain.entities.Project;
import com.jastt.business.domain.entities.User;


@Repository
public interface ProjectDataProvider extends PageableDataProvider<ProjectEntity, Project, Integer>, Serializable {
	public Project getProjectByName(String name);
	public Project getProjectByKey(String key);
	public List<Project> getAvailableProjectsForUser(User currentUser);
}
