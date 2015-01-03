package com.jastt.frontend.beans.users;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.RequestScoped;
import javax.faces.view.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.validator.ValidatorException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.jastt.business.domain.entities.Permission;
import com.jastt.business.domain.entities.Project;
import com.jastt.business.domain.entities.Server;
import com.jastt.business.domain.entities.User;
import com.jastt.business.services.IssueService;
import com.jastt.business.services.PermissionService;
import com.jastt.business.services.ProjectService;
import com.jastt.business.services.ServerService;
import com.jastt.business.services.UserService;
import com.jastt.business.services.jira.JiraClientException;
import com.jastt.business.services.jira.JiraProjectService;
import com.jastt.dal.providers.PermissionDataProvider;
import com.jastt.dal.providers.ServerDataProvider;
import com.jastt.dal.providers.UserDataProvider;
import com.jastt.frontend.beans.LoginBean;
import com.jastt.utils.annotations.RequestScope;
import com.jastt.business.enums.UserRoleEnum;
import com.jastt.frontend.utils.Dialogs;
import com.jastt.frontend.utils.Faces;
import com.jastt.dal.exceptions.DaoException;


@Component(value="userBean")
@Scope("request")
public class UserBean {
	
	//private static final long serialVersionUID = 2819227216048472445L;
	
	private static final Logger LOG = LoggerFactory.getLogger(UserBean.class);
	
	private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*" +
            "@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
	
	private static final String LOGIN_PATTERN = "^[a-z0-9_-]";
	
	@Autowired
	private UsersBean usersBean;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private ServerService serverService;
	
	@Autowired
	private PermissionService permissionService;
	
	@Autowired
	private ProjectService projectService;
	
	@Autowired
	private IssueService issueService;
	
	@Autowired
	private JiraProjectService jiraProjectService;
	
	@Autowired
	private UserDataProvider userDataProvider;
	
	@Autowired
	private ServerDataProvider serverDataProvider;
	
	@Autowired
	private PermissionDataProvider permissionDataProvider;
	
	private User user;
	private Server server;
		
	private String name;
	private String login;
	private String email;
	private String url;
	private String password;
	private String userRole;
	private boolean admin = false;
	private String username;
    
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
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
	
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
	

    @PostConstruct
    public void init() {
    	resetFields();
        user = new User();
        server = new Server();
    }
    
    public void validateLogin(FacesContext facesContext, UIComponent componentToValid, Object value)
    		throws ValidatorException{
        String login = value.toString();
        User userByLogin = userService.getUserByLogin(login);
        if(userByLogin != null) {
            String messageDetails = "User with login " + login + " already exists.";
            FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Couldn`t create user.", messageDetails);
            throw new ValidatorException(facesMessage);
        }
        if(!login.matches(LOGIN_PATTERN)) {
            String messageSummary = "Login has a wrong format.";
            String messageDetails = "Length must be greater or equals than allowable minimum of '4' and less than allowable maximum of '16'.";
            FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, messageSummary, messageDetails);
            throw new ValidatorException(facesMessage);
        }
        resetFields();
    }
    
    public void validateEmail(FacesContext facesContext, UIComponent componentToValid, Object value) throws ValidatorException{
        String email = value.toString();
        if(!email.matches(EMAIL_PATTERN)) {
            String messageString = "Email has a wrong format.";
            FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Wrong email format.", messageString);
            throw new ValidatorException(facesMessage);
        }
        resetFields();
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
    
    public void verifyJiraLogin(User user) {
    	FacesContext fc = FacesContext.getCurrentInstance();
		try {
			Set<Project> availableProjects = jiraProjectService.getAllProjects(user);
			userService.addUser(user);
			User newUser = userService.getUserByLogin(user.getLogin());
	        issueService.update(newUser);	        
	        Faces.info("growl", "New user successfully added",
	        		String.format("User %s has been added.", getUsername(user)));
		} catch (JiraClientException jce) {
			if(jce.getStatusCode() == 401 | jce.getStatusCode() == 403){
				FacesMessage fm = new FacesMessage(FacesMessage.SEVERITY_ERROR, 
						"Authentication error!", "Wrong Jira username or password!");	
				fc.addMessage(null, fm);		
			} else {
				FacesMessage fm = new FacesMessage(FacesMessage.SEVERITY_ERROR, 
						"Communication error!", "Wrong  Jira URL or Jira server is not available!");
				fc.addMessage(null, fm);
			}		
		}
    }
    
    public void confirmUserRemoval(User user) {
    	getUsername(user);
        Faces.showDialog(Dialogs.CONFIRM_USER_REMOVAL_DIALOG_WIDGET);
    }
    
    
	public void deleteUser() {
		List<Permission> permissions = permissionService.getPermissionsByUser(user);
		for (Permission p : permissions) {
		}
		
		userService.deleteUser(user.getLogin());
		usersBean.updateValues();
		Faces.hideDialog(Dialogs.CONFIRM_USER_REMOVAL_DIALOG_WIDGET);
        Faces.info("growl", "User has been removed.",
        		String.format("User %s successfully removed.", getUsername(user)));
	}

    public void createUser() {    	
        if (isAdmin()) {
    		user.setUserRole(UserRoleEnum.ADMIN.getMark());
    		userService.addUser(user);
    	} else {
    		user.setUserRole(UserRoleEnum.USER.getMark());
    		server = new Server(url);
            server = serverService.getServerByUrl(url);
    		if(server == null){
    			server = serverService.getServerByUrl(url);
    	        serverService.addServer(url);
    		}
    		user.setServer(serverService.getServerByUrl(url));
    		verifyJiraLogin(user);
    	}
       
        Faces.hideDialog(Dialogs.NEW_USER_DIALOG_WIDGET);
        Faces.info("growl", "New user successfully added",
        		String.format("User %s successfully added.", getUsername(user)));
		resetFields();
        usersBean.updateValues();
        
    }
    
    public String getUsername(User user) {
    	name = user.getName();
    	login = user.getLogin();
    	if (!name.isEmpty() && name != null) {
    		username = name;
    	} else {
    		username = login;
    	}
    	return username;
    }
    
}
