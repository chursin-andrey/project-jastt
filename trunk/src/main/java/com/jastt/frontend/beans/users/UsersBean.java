package com.jastt.frontend.beans.users;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.jastt.business.domain.entities.Server;
import com.jastt.business.domain.entities.User;
import com.jastt.business.services.UserService;
import com.jastt.dal.providers.ServerDataProvider;
import com.jastt.frontend.beans.LoginBean;
import com.jastt.business.enums.UserRoleEnum;
import com.jastt.dal.exceptions.DaoException;


@Component(value="usersBean")
@Scope("request")
public class UsersBean implements Serializable {
private static final long serialVersionUID = 2819227216048472445L;
	
	
	private static final Logger LOG = LoggerFactory.getLogger(UsersBean.class);
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private ServerDataProvider serverDataProvider;
	
	@Autowired
	private LoginBean loginBean;
	
	private String name;
	private String login;
	private String email;
	private String url;
	private String oldPassword;
	private String newPassword;
	private String userRole;
	private boolean admin = false;
	
	public boolean isAdmin() {
		return admin;
	}

	public void setAdmin(boolean admin) {
		this.admin = admin;
	}

	private List<User> users;
	private User user;
	private Server server;

	/*
	@PostConstruct
	public void init(){
		users = userService.getAllUsers();
	}
	
	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}

	*/
	
	
	public String getUserRole() {
		return userRole;
	}

	public void setUserRole(String userRole) {
		this.userRole = userRole;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
	public String getOldPassword() {
		return oldPassword;
	}

	public void setOldPassword(String password) {
		this.oldPassword = password;
	}
	
	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	public void deleteUser(String login){
		userService.deleteUser(login);
		try{
			FacesContext.getCurrentInstance().getExternalContext().redirect("/project-jastt/protected/admin.xhtml");
		}catch(Exception e){
			LOG.error(e.getMessage());
		}
	}
	
	public void loadFields(String login) {
		user = userService.getUserByLogin(login);
		name = user.getName();
		this.login = user.getLogin();
		email = user.getEmail();
		if (user.getUserRole().equalsIgnoreCase("admin")) {
			userRole = UserRoleEnum.ADMIN.toString();
			admin = true;
		}
		else {
			userRole = UserRoleEnum.USER.toString();
			admin = false;
			url = user.getServer().getUrl();
		}
	}
	
	public void resetFields() {
		name = null;
		login = null;
		email = null;
		url = null;
		oldPassword = null;
	}	
	
	public void saveUser() {
		user = new User();
		user.setLogin(login);
		user.setName(name);
		user.setEmail(email);
		if (isAdmin()) {
			user.setUserRole(UserRoleEnum.ADMIN.toString());
			user.setPassword(newPassword);
		} else {
			user.setUserRole(UserRoleEnum.USER.toString());
			user.setServer(server);
		}		
		userService.addUser(user);
	}

}
