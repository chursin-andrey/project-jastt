package com.jastt.dal.providers;

import java.util.List;

import com.jastt.dal.entities.PermissionEntity;
import com.jastt.dal.providers.base.PageableDataProvider;
import com.jastt.business.domain.entities.*;



public interface PermissionDataProvider extends PageableDataProvider<PermissionEntity, Permission, Integer> {
	public Permission getPermission(User user, Project project);
	public List<Permission> getPermissionByUser(Project project);
	
	
	
	
}
