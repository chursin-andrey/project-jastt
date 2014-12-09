package com.jastt.business.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jastt.business.domain.entities.Server;
import com.jastt.business.services.ServerService;
import com.jastt.dal.entities.ServerEntity;
import com.jastt.dal.providers.ServerDataProvider;

@Service(value="serverService")
public class ServerServiceImpl implements ServerService{
	
	@Autowired
	ServerDataProvider serverDataProvider;
	
	@Override
	public void addServer(String url) {
		Server server = new Server(url);
		serverDataProvider.save(server, ServerEntity.class);	
	}

	@Override
	public Server getServerByUrl(String url) {
		return serverDataProvider.getServerByUrl(url);
	}

}
