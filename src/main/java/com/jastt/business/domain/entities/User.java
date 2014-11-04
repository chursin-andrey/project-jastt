package com.jastt.business.domain.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.jastt.business.domain.entities.Permission;
import com.jastt.business.domain.entities.Server;

class User implements Serializable{
	
	private Integer id;
	private Server server;
	private String login;
	private String firstName;
	private String lastName;
	private String password;
	private String email;
	private Date birthday;
	private Set<Permission> permissions = new HashSet<Permission>(0);
	
	
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
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
	
	public Date getBirthday() {
		return birthday;
	}
	
	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}
	
	public Set<Permission> getPermissions() {
		return permissions;
	}
	
	public void setPermissions(Set<Permission> permissions) {
		this.permissions = permissions;
	}
	
	
}