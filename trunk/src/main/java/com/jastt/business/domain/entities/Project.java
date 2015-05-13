package com.jastt.business.domain.entities;

import com.jastt.business.domain.entities.Server;

public class Project extends PersistentEntity<Integer>{
	
	private static final long serialVersionUID = -1896715988263639452L;
	private Server server;
	private String key;
	private String name;

	public Project(){
		
	}
	
	public Project(Server server, String key, String name) {
		super();
		this.server = server;
		this.key = key;
		this.name = name;
	}

	public Server getServer() {
		return server;
	}
	
	public void setServer(Server server) {
		this.server = server;
	}
	
	public String getKey() {
		return key;
	}
	
	public void setKey(String key) {
		this.key = key;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((key == null) ? 0 : key.hashCode());
		result = prime * result + ((server == null) ? 0 : server.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Project other = (Project) obj;
		if (key == null) {
			if (other.getKey() != null)
				return false;
		} else if (!key.equals(other.getKey()))
			return false;
		if (server == null) {
			if (other.getServer() != null)
				return false;
		} else if (!server.equals(other.getServer()))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return key;
	}
}
