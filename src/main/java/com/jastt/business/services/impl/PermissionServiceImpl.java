package com.jastt.business.services.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jastt.business.domain.entities.Permission;
import com.jastt.business.domain.entities.Project;
import com.jastt.business.domain.entities.User;
import com.jastt.business.services.PermissionService;
import com.jastt.dal.entities.PermissionEntity;
import com.jastt.dal.entities.UserEntity;
import com.jastt.dal.providers.PermissionDataProvider;
import com.jastt.dal.providers.UserDataProvider;

@Service(value="permissionService")
public class PermissionServiceImpl implements PermissionService, Serializable {
	
	@Autowired
	private PermissionDataProvider permissionDataProvider;
	@Autowired
	private UserDataProvider userDataProvider;
	
	
	@Override
	public Permission getPermission(User user, Project project) {
		return permissionDataProvider.getPermission(user, project);
	}

	
	public List<Permission> getPermissionsByUser(User user) {
		return permissionDataProvider.getPermissionByUser(user);
	}


	@Override
	public void addPermission(User user, Project project) {
		
		Permission permission = new Permission(user, project);
		permissionDataProvider.save(permission, PermissionEntity.class);
		
	}
	
	@Override
	public void deletePermissionsByUser(User user) {	
		List<Permission> perms = new ArrayList<Permission>();
		perms = permissionDataProvider.getPermissionByUser(user);
		for(Permission pr : perms) {
			permissionDataProvider.delete(pr, PermissionEntity.class);
		}
		
	}

}

