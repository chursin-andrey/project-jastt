package com.jastt.business.domain.entities;


import com.jastt.business.domain.entities.Server;

public class User extends PersistentEntity<Integer>{
	
	private static final long serialVersionUID = 468724781884407064L;
	private Server server;
	private String login;
	private String firstName;
	private String lastName;
	private String password;
	private String email;
	

	public User() {
		
	}
	
	public User(Server server, String login, String firstName, String lastName,
			String password, String email) {	
		super();
		this.server = server;
		this.login = login;
		this.firstName = firstName;
		this.lastName = lastName;
		this.password = password;
		this.email = email;

	}


	public Server getServer() {
		return server;
	}
	
	public void setServer(Server server) {
		this.server = server;
	}
	
	public String getLogin() {
		return login;
	}
	
	public void setLogin(String login) {
		this.login = login;
	}
	
	public String getFirstName() {
		return firstName;
	}
	
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	public String getLastName() {
		return lastName;
	}
	
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	

	
}