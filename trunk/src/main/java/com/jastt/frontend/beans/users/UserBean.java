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


@Component(value="userBean")
@Scope("request")
public class UserBean implements Serializable {
	
	private static final long serialVersionUID = 2819227216048472445L;
	
	private static final Logger LOG = LoggerFactory.getLogger(UserBean.class);
	
	@Autowired
	private UsersBean usersBean;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private ServerDataProvider serverDataProvider;
	
	private User user;
	private Server server;
		
	private String name;
	private String login;
	private String email;
	private String url;
	private String oldPassword;
	private String newPassword;
	private String password;	
	private String userRole;
	private boolean admin;
	
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isAdmin() {
		return admin;
	}

	public void setAdmin(boolean admin) {
		this.admin = admin;
	}
	
	
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
	
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
	

    @PostConstruct
    public void init() {
        user = new User();
        user.setServer(null);
//        server = new Server();
//        user.setServer(server);
//        user.setUserRole("user");
    }

    public void addUser() {
        if (userRole.equalsIgnoreCase("admin")) {
    		user.setUserRole("admin");
    	} else {
    		user.setUserRole("user");
//    		server = new Server();
//    		server.setUrl(url);
    	}
        userService.addUser(user);
        usersBean.updateValues();
    }
}
