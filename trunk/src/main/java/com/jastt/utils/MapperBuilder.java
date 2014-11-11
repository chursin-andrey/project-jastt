package com.jastt.utils;

import org.dozer.loader.api.BeanMappingBuilder;

import com.jastt.business.domain.entities.Assignee;
import com.jastt.business.domain.entities.Issue;
import com.jastt.business.domain.entities.Permission;
import com.jastt.business.domain.entities.Project;
import com.jastt.business.domain.entities.Server;
import com.jastt.business.domain.entities.User;
import com.jastt.dal.entities.AssigneeEntity;
import com.jastt.dal.entities.IssueEntity;
import com.jastt.dal.entities.PermissionEntity;
import com.jastt.dal.entities.ProjectEntity;
import com.jastt.dal.entities.ServerEntity;
import com.jastt.dal.entities.UserEntity;


public class MapperBuilder extends BeanMappingBuilder {

	@Override
	protected void configure() {

		mapping(AssigneeEntity.class, Assignee.class);	
		mapping(IssueEntity.class, Issue.class).fields("assigneeEntity","assignee").fields("projectEntity","project");	
		mapping(PermissionEntity.class, Permission.class).fields("userEntity","user").fields("projectEntity","project");	
		mapping(ProjectEntity.class, Project.class).fields("serverEntity","server");	
		mapping(ServerEntity.class, Server.class);	
		mapping(UserEntity.class, User.class).fields("serverEntity", "server");		
		
	}
	
}

