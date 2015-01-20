package com.jastt.business.services;

import com.jastt.business.domain.entities.Server;

public interface ServerService {
	public void addServer(String url);
	public Server getServerByUrl(String url);
	
}
