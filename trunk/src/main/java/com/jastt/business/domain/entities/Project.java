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
	
	
}
