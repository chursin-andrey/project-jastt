package com.jastt.business.domain.entities;


public class Server extends PersistentEntity<Integer>{
		
	private static final long serialVersionUID = 2097826735449036033L;
	private String name;
	private String url;
	
	public Server() {
		
	}
		
	public Server(String name, String url) {
		super();
		this.name = name;
		this.url = url;
	}


	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getUrl() {
		return url;
	}
	
	public void setUrl(String url) {
		this.url = url;
	}
		
	
}