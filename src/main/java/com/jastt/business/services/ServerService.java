package com.jastt.business.services;

import com.jastt.business.domain.entities.Server;

public interface ServerService {
	
	void addServer(String url);
	Server getServerByUrl(String url);
	
}
