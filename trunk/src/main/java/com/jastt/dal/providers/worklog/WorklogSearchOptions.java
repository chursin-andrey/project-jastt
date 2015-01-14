package com.jastt.dal.providers.worklog;

import java.util.ArrayList;
import java.util.List;

import com.jastt.business.domain.entities.Project;

public class WorklogSearchOptions {

	private Project project;
	private List<String> authors;

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}
	
	public List<String> getAuthors() {
		return authors;
	}

	public void setAuthors(List<String> authors) {
		this.authors = authors;
	}

	public boolean isEmpty() {
		return (project == null) && (authors == null || authors.isEmpty());
	}
}
