package com.jastt.dal.providers;

import com.jastt.dal.entities.PermissionEntity;
import com.jastt.dal.providers.base.PageableDataProvider;
import com.jastt.business.domain.entities.Permission;

import com.jastt.business.domain.entities.*;



public interface PermissionDataProvider extends PageableDataProvider<PermissionEntity, Permission, Integer> {
	public Permission getPermissionByUser(User user);
	public Permission getPermissionByProject(Project project);
	
	
	
	
}
