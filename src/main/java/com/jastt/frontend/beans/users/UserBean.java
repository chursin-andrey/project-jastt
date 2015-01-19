package com.jastt.frontend.beans.users;

import java.io.Serializable;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import org.apache.shiro.crypto.hash.Sha512Hash;
import org.primefaces.event.CloseEvent;

import javax.faces.validator.ValidatorException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

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
import com.jastt.business.enums.UserRoleEnum;
import com.jastt.frontend.utils.Dialogs;
import com.jastt.frontend.utils.Faces;

@Component(value="userBean")
@Scope("session")
public class UserBean implements Serializable {
	
	private static final long serialVersionUID = 2819227216048472445L;
	
	private static final Logger LOG = LoggerFactory.getLogger(UserBean.class);
	
	private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*" +
            "@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
	
	private static final String LOGIN_PATTERN = "^[A-Za-z0-9_-]{4,16}$";
	
	private static final String PASSWORD_PATTERN = "^[a-zA-z0-9]{8,16}$";
	
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
	private String url;
	private String username;
	private boolean admin;
	private boolean disableSaveButton;
	
	public boolean isDisableButton() {
		return disableSaveButton;
	}

	public void setDisableButton(boolean disableButton) {
		if (isAdmin()) {
			this.disableSaveButton = false; 
		}
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
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
    		throws ValidatorException {
    	
    	String login = value.toString();
        User userByLogin = userService.getUserByLogin(login);
    	            
        if(userByLogin != null) {
        	String messageSummary = "Couldn`t create user.";
        	String messageDetails = "User with login " + login + " already exists.";
            FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, messageSummary, messageDetails);
            throw new ValidatorException(facesMessage);
        }
        if(!login.matches(LOGIN_PATTERN)) {
        	String messageSummary = "Login has a wrong format.";
            String messageDetails = "Login must contain letters, digits, dash, underscore. Length must be greater or equals than allowable minimum of '4' and less than allowable maximum of '16'.";
            FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, messageSummary, messageDetails);
            throw new ValidatorException(facesMessage);
        }          
    }
    
    public void validateEmail(FacesContext facesContext, UIComponent componentToValid, Object value)
    		throws ValidatorException {
        String email = value.toString();
        if(!email.matches(EMAIL_PATTERN)) {
        	String messageSummary = "Wrong email format.";
            String messageString = "Email has a wrong format.";
            FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, messageSummary, messageString);
            throw new ValidatorException(facesMessage);
        }
    }
    
    public void validatePassword(FacesContext facesContext, UIComponent componentToValid, Object value)
    		throws ValidatorException {
    	if (isAdmin()) {
    		String password = value.toString();
            if(!password.matches(PASSWORD_PATTERN)) {
            	String messageSummary = "Password has invalid symbols.";
                String messageString = "The password must contain uppercase and lowercase Latin letters, numbers. Mininmum length is 8. Maximumum lenght is 16.";
                FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, messageSummary, messageString);
                throw new ValidatorException(facesMessage);
            }
    	}   
    }
    
    public void resetFields() {
    	user = new User();
    	server = new Server();
    	admin = false;
    	name = "";
    	login = "";
    	url = "";
    }
    
    public void verifyJiraCredentials() {
    	FacesContext fc = FacesContext.getCurrentInstance();    	
    	try {
    		if (!isAdmin()) {
        		user.setUserRole(UserRoleEnum.USER.getMark());
        		server = new Server(url);
                server = serverService.getServerByUrl(url);
                
                if(server == null){
                	serverService.addServer(url);
                	server = serverService.getServerByUrl(url);
        		} else {
        			user.setServer(serverService.getServerByUrl(url));
        		}
                
                user.setServer(serverService.getServerByUrl(url));
        	}
    		
			Set<Project> availableProjects = jiraProjectService.getAllProjects(user);
			userService.addUser(user);
			User newUser = userService.getUserByLogin(user.getLogin());
	        issueService.update(newUser);
	        disableSaveButton = false;
		} catch (JiraClientException jce) {
			FacesMessage facesMessage;
			if(jce.getStatusCode() == 401 | jce.getStatusCode() == 403) {
				String messageSummary = "Authentication error.";
				String messageDetails = "Wrong Jira username or password!";
	            facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, messageSummary, messageDetails);		
			} else {
				String messageSummary = "Communication error.";
				String messageDetails = "Wrong  Jira URL or Jira server is not available.";
				facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, messageSummary, messageDetails);
			}
			fc.addMessage(null, facesMessage);
			disableSaveButton = true;
		}
		
    }
    
    
    public boolean verifyJiraCredentials(User user) {
    	FacesContext fc = FacesContext.getCurrentInstance();
    	try {
			Set<Project> availableProjects = jiraProjectService.getAllProjects(user);
			userService.addUser(user);
			user.setPassword(new Sha512Hash(user.getPassword(), user.getLogin(), 1).toHex());
			User newUser = userService.getUserByLogin(user.getLogin());
	        issueService.update(newUser);
	        return true;
		} catch (JiraClientException jce) {
			FacesMessage facesMessage;
			if(jce.getStatusCode() == 401 | jce.getStatusCode() == 403) {
				String messageSummary = "Authentication error.";
				String messageDetails = "Wrong Jira username or password!";
	            facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, messageSummary, messageDetails);		
			} else {
				String messageSummary = "Communication error.";
				String messageDetails = "Wrong  Jira URL or Jira server is not available.";
				facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, messageSummary, messageDetails);
			}
			fc.addMessage(null, facesMessage);
			return false;
		}
		
    }
    	

    public void createUser() {    	
        if (isAdmin()) {
    		user.setUserRole(UserRoleEnum.ADMIN.getMark());
    		user.setPassword(new Sha512Hash(user.getPassword(), user.getLogin(), 1).toHex());
    		userService.addUser(user);
    		Faces.hideDialog(Dialogs.NEW_USER_DIALOG_WIDGET);
    		Faces.info("growl", "New user successfully added",
        		String.format("User %s successfully added.", getUsername(user)));
    	} else {
    		user.setUserRole(UserRoleEnum.USER.getMark());
    		server = new Server(url);
            server = serverService.getServerByUrl(url);
            
            if(server == null){
            	serverService.addServer(url);
            	server = serverService.getServerByUrl(url);
    		} else {
    			user.setServer(serverService.getServerByUrl(url));
    		}
            
            user.setServer(serverService.getServerByUrl(url));
            if(verifyJiraCredentials(user)) {
            	Faces.hideDialog(Dialogs.NEW_USER_DIALOG_WIDGET);
            	Faces.info("growl", "User successfully added.",
    	        		String.format("User %s has been added.", getUsername(user)));
            }  	
    	}
		resetFields();
		usersBean.updateValues();
    }
    
    public String getUsername(User user) {
    	if (user.getLogin() != null && !user.getLogin().isEmpty()) {
    		username = user.getLogin();
    	}
    	if (user.getName() != null && !user.getName().isEmpty()) {
    		username = user.getName();
    	}
    	return username;
    }
    
    public void cancelHandle(ActionEvent action) {
    	resetFields();		
    	try {		
    		FacesContext.getCurrentInstance().getExternalContext().redirect("/project-jastt/protected/admin.xhtml");		
    	} catch(Exception e) {		
    		LOG.error(e.getMessage());		
    	}		
    	usersBean.updateValues();
    }
    
}
