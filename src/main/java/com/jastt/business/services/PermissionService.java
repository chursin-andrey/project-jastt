package com.jastt.business.services;

import java.util.List;

import com.jastt.business.domain.entities.Permission;
import com.jastt.business.domain.entities.Project;
import com.jastt.business.domain.entities.User;

public interface PermissionService {
	
	public Permission getPermission(User user ,Project project);
	public List<Permission> getPermissionsByUser(User user);
	public void addPermission(User user, Project project);
	
}
