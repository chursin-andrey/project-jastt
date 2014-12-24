package com.jastt.frontend.beans.users;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.jastt.business.domain.entities.Server;
import com.jastt.business.domain.entities.User;
import com.jastt.business.enums.UserRoleEnum;
import com.jastt.business.services.UserService;
import com.jastt.frontend.beans.LoginBean;

@Component(value="usersBean")
@Scope("session")
public class UsersBean implements Serializable {
	
	private static final long serialVersionUID = 2092800049856823809L;

	private static final Logger LOG = LoggerFactory.getLogger(UsersBean.class);
	
    @Autowired
    private UserService userService;
    private User user;
    private List<User> users;
    
    private String url;
    private String userRole;
    private Server server;
	private String password;
	private boolean admin;
	
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
    	this.user = user;  
    }
       
    public void saveUser() {
        if (isAdmin()) {
        	this.user.setUserRole("admin");
        	setPassword(password);
        	setUserRole("admin");
        } else {
        	this.user.setUserRole("user");
        	setUserRole("user");
        }
        userService.updateUser(user);
        updateValues();
    }
    
	public void deleteUser(User user) {
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
