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
	
	
	private List<User> users;
	private User user;
	
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
	}
	
	public void loadFields(String login) {
		currentUserIsAdmin = true;
		user = userService.getUserByLogin(login);
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
				this.admin = false;
			}
		}
		
	}
	
	public void resetFields() {
		name = null;
		login = null;
		url = null;
		password = null;
		currentUserIsAdmin = false;
		admin = false;
	}	
	
	public void changeUserInfo() {
		user = new User();
		Server serverEntity = null;
		try {
			user = userService.getUserByLogin(login);
			if (user == null) {
				serverEntity = serverDataProvider.getServerByUrl(url);
				
				user = new User();
				user.setName(name);
				user.setLogin(login);
				
				if (isAdmin()) {
					user.setUserRole(UserRole.ADMIN.toString());
					user.setPassword(password);
				}
				else {
					user.setUserRole(UserRole.USER.toString());
				}
				
				user.setServer(serverEntity);
				userService.addUser(user);
			}
			else {
				user.setName(name);				
				if (isAdmin()) {
					user.setUserRole(UserRole.ADMIN.toString());
					user.setPassword(password);
				}
				else {
					user.setUserRole(UserRole.USER.toString());
				}
				userService.updateUser(user);
			}
		} catch (Exception ex) {
			
		}
		
	}

}
