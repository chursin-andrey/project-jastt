package com.jastt.dal.providers;

import com.jastt.dal.entities.ServerEntity;
import com.jastt.dal.providers.base.PageableDataProvider;
import com.jastt.business.domain.entities.Server;
import com.jastt.business.domain.entities.*;



public interface ServerDataProvider extends PageableDataProvider<ServerEntity, Server, Integer> {
	public Server getServerByUrl(String url);
	public Server getServerByName(String name);
	public Server getServerByProject(Project project);
	
	
	
}
