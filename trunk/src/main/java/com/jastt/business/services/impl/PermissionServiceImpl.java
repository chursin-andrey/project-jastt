package com.jastt.business.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jastt.business.domain.entities.Permission;
import com.jastt.business.domain.entities.Project;
import com.jastt.business.domain.entities.User;
import com.jastt.business.services.PermissionService;
import com.jastt.dal.entities.PermissionEntity;
import com.jastt.dal.providers.PermissionDataProvider;

@Service(value="permissionService")
public class PermissionServiceImpl implements PermissionService {
	
	@Autowired
	private PermissionDataProvider permissionDataProvider;
	
	@Override
	public Permission getPermission(User user, Project project) {
		return permissionDataProvider.getPermission(user, project);
	}

	
	public List<Permission> getPermissionsByUser(User user) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public void addPermission(User user, Project project) {
		
		Permission permission = new Permission(user, project);
		permissionDataProvider.save(permission, PermissionEntity.class);
		
	}
	


}

