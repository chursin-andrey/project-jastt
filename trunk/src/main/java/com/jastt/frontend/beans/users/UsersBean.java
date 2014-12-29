package com.jastt.frontend.beans.users;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import org.primefaces.event.CloseEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import sun.print.resources.serviceui;

import com.jastt.business.domain.entities.Server;
import com.jastt.business.domain.entities.User;
import com.jastt.business.enums.UserRoleEnum;
import com.jastt.business.services.UserService;
import com.jastt.frontend.beans.LoginBean;
import com.jastt.utils.annotations.SessionScope;

@Component(value="usersBean")
@SessionScope
public class UsersBean implements Serializable {
	
	private static final long serialVersionUID = 2092800049856823809L;

	private static final Logger LOG = LoggerFactory.getLogger(UsersBean.class);
	
    @Autowired
    private UserService userService;
    private User user;
    private List<User> users;
    
    private String username;
    
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	private String name;
    
    public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	private String login;
	
    public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	private String url;
    private String userRole;
    private Server server;
	private String password;
	private boolean admin = false;
	
	@PostConstruct
    public void init() {
        users = userService.getAllUsers();
        
    }
    
    public String getUserRole() {
		return userRole;
	}

	public void setUserRole(String userRole) {
		this.userRole = userRole;
	}
	
    public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

    public boolean isAdmin() {
		return admin;
	}

	public void setAdmin(boolean admin) {
		this.admin = admin;
	}
	
    public void editUser(User user) {
    	if (user.getUserRole().equalsIgnoreCase("admin")) {
    		setAdmin(true);;
    	} else {
    		setAdmin(false);;
    	}
    	this.user = user;
    }
    
    public String getUsername(User user) {
    	name = user.getName();
    	login = user.getLogin();
    	if (!name.isEmpty()) {
    		username = name;
    	} else {
    		username = login;
    	}
    	return username;
    }
       
    public void saveUser() {
        if (isAdmin()) {
        	user.setUserRole("admin");
        } else {
        	user.setUserRole("user");
        }
        userService.updateUser(user);
        updateValues();
    }
    
	public void deleteUser() {
		userService.deleteUser(user.getLogin());
		try {
			FacesContext.getCurrentInstance().getExternalContext().redirect("/project-jastt/protected/admin.xhtml");
		} catch(Exception e) {
			LOG.error(e.getMessage());
		}
		updateValues();
	}
	

    public void updateValues() {
        users = userService.getAllUsers();
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

}
