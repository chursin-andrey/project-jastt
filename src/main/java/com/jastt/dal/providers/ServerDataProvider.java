package com.jastt.dal.providers;

import java.io.Serializable;

import com.jastt.dal.entities.ServerEntity;
import com.jastt.dal.providers.base.PageableDataProvider;
import com.jastt.business.domain.entities.Server;



public interface ServerDataProvider extends PageableDataProvider<ServerEntity, Server, Integer>, Serializable {
	public Server getServerByUrl(String url);

	
	
	
	
}
