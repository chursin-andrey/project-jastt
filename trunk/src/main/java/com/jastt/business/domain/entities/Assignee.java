package com.jastt.business.domain.entities;


public class Assignee extends PersistentEntity<Integer>{
	
	private static final long serialVersionUID = -6761832289105928380L;
	private String name;
	private String email;
	
	
	public Assignee(){	
	
	}
	
	public Assignee(String name, String email) {
		super();
		this.name = name;
		this.email = email;
	}

	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
			
}
