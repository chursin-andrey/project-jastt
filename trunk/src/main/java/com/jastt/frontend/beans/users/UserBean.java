package com.jastt.frontend.beans.users;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.validator.ValidatorException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.jastt.business.domain.entities.Server;
import com.jastt.business.domain.entities.User;
import com.jastt.business.services.ServerService;
import com.jastt.business.services.UserService;
import com.jastt.dal.providers.ServerDataProvider;
import com.jastt.frontend.beans.LoginBean;
import com.jastt.utils.annotations.RequestScope;
import com.jastt.business.enums.UserRoleEnum;
import com.jastt.dal.exceptions.DaoException;


@Component(value="userBean")
@Scope("request")
public class UserBean implements Serializable {
	
	private static final long serialVersionUID = 2819227216048472445L;
	
	private static final Logger LOG = LoggerFactory.getLogger(UserBean.class);
	
	private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*" +
            "@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
	
	private static final String LOGIN_PATTERN = "^[a-z0-9_-]{4,16}$";
	
	@Autowired
	private UsersBean usersBean;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private ServerService serverService;
	
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
	private boolean admin = false;
	
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
        server = new Server();
    }
    
    public void validateLogin(FacesContext facesContext, UIComponent componentToValid, Object value)
    		throws ValidatorException{
        String login = value.toString();
        User userByLogin = userService.getUserByLogin(login);
        if(userByLogin != null) {
            String messageString = "User with login " + login + " already exists.";
            FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Couldn`t create user", messageString);
            throw new ValidatorException(facesMessage);
        }
        if(!login.matches(LOGIN_PATTERN)) {
            String messageString = "Login has a wrong format";
            FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Wrong login format", messageString);
            throw new ValidatorException(facesMessage);
        }
    }
    
    public void validateEmail(FacesContext facesContext, UIComponent componentToValid, Object value) throws ValidatorException{
        String email = value.toString();
        if(!email.matches(EMAIL_PATTERN)) {
            String messageString = "Email has a wrong format";
            FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Wrong email format", messageString);
            throw new ValidatorException(facesMessage);
        }
    }
    
    public void resetFields() {
    	name = null;
    	login = null;
    	email = null;
    	url = null;
    	password = null;
    	userRole = null;
    	admin = false;
    }
    
    public void verifyJiraLogin() {
    	Server server = new Server(getUrl());
    	
    }

    public void addUser() {
    	User user = new User();
    	user = userService.getUserByLogin(getLogin());
    	
    	if (user == null) {
        	user = new User();
        	user.setName(name);
        	user.setLogin(login);
        	user.setEmail(email);
    	}
    	
        if (isAdmin()) {
    		user.setUserRole("admin");
    		user.setPassword(password);
    		
    	} else {
    		user.setUserRole("user");
    		server = new Server(url);
    		try {
            	server = serverService.getServerByUrl(url);
    			if(server == null){
    				serverService.addServer(url);
    				server = serverService.getServerByUrl(url);
    			} else {
    	        	user.setServer(serverService.getServerByUrl(url));
    			}
    		} catch (Exception ex) {
    			
    		}
    		
    		user.setServer(serverService.getServerByUrl(url));
    	}
        userService.addUser(user);
        resetFields();
        usersBean.updateValues();
    }
}
