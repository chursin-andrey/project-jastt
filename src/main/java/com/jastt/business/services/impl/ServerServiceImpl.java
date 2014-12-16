package com.jastt.business.services.impl;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jastt.business.domain.entities.Server;
import com.jastt.business.services.ServerService;
import com.jastt.dal.entities.ServerEntity;
import com.jastt.dal.providers.ServerDataProvider;

@Service(value="serverService")
public class ServerServiceImpl implements ServerService, Serializable{
	
	
	private static final long serialVersionUID = 5243693760828134247L;
	
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
