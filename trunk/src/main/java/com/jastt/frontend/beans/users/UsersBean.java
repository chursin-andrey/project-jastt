package com.jastt.frontend.beans.users;

import java.io.Serializable;

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
import com.jastt.frontend.beans.users.UsersBean;
import com.jastt.business.enums.UserRole;
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
	private String url;
	private String password;
	private String userRole;

	private boolean admin = false;
	private boolean currentUserIsAdmin = false;
	
	public boolean isCurrentUserIsAdmin() {
		return currentUserIsAdmin;
	}

	public void setCurrentUserIsAdmin(boolean currentUserIsAdmin) {
		this.currentUserIsAdmin = currentUserIsAdmin;
	}

	public String getUserRole() {
		return userRole;
	}

	public void setUserRole(String userRole) {
		this.userRole = userRole;
	}
	
	public boolean isAdmin() {
		return admin;
	}
	
	public void setAdmin(boolean admin) {
		this.admin = admin;
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

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void deleteUser(String login){
		userService.deleteUser(login);
		
		//---temporary block---!
		try{
			FacesContext.getCurrentInstance().getExternalContext().redirect("/project-jastt/protected/admin.xhtml");
		}catch(Exception e){
			LOG.error(e.getMessage());
		}
		//----------------------------
		
	}
		
	public void loadFields(String login) {
		currentUserIsAdmin = true;
		User user = userService.getUserByLogin(login);
		if (user != null) {
			this.name = user.getName();
			this.login = user.getLogin();
			if (user.getUserRole().equalsIgnoreCase("admin")) {
				this.userRole = UserRole.ADMIN.toString();
				this.admin = true;
			}
			else {
				this.userRole = UserRole.USER.toString();
				this.url = user.getServer().getUrl();
			}
		}
	}
	
	public void changeUserInfo() {
		User userEntity = null;
		Server serverEntity = null;
		try {
			userEntity = userService.getUserByLogin(login);
			if (userEntity == null) {
				serverEntity = serverDataProvider.getServerByUrl(url);
				
				userEntity = new User();
				userEntity.setName(name);
				userEntity.setLogin(login);
				
				if (isAdmin()) {
					userEntity.setUserRole(UserRole.ADMIN.toString());
					userEntity.setPassword(password);
					serverEntity = serverDataProvider.getServerByUrl("https://jira.atlassian.com");
				}
				else {
					userEntity.setUserRole(UserRole.USER.toString());
				}
				
				userEntity.setServer(serverEntity);
				userService.addUser(userEntity);
			}
			else {
				/*
				userEntity.setName(name);
				userEntity.setLogin(login);
				*/
				
				if (isAdmin()) {
					userEntity.setUserRole(UserRole.ADMIN.toString());
					userEntity.setPassword(password);
					serverEntity = serverDataProvider.getServerByUrl("https://jira.atlassian.com");
				}
				else {
					userEntity.setUserRole(UserRole.USER.toString());
				}
				
				userService.updateUser(userEntity);;
			}
		} catch (Exception ex) {
			
		}
		
	}

}
