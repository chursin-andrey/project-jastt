package com.jastt.frontend.beans.users;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.jastt.business.domain.entities.Project;
import com.jastt.business.domain.entities.Server;
import com.jastt.business.domain.entities.User;
import com.jastt.business.enums.UserRoleEnum;
import com.jastt.business.services.CurrentUserService;
import com.jastt.business.services.IssueService;
import com.jastt.business.services.PermissionService;
import com.jastt.business.services.ServerService;
import com.jastt.business.services.UserService;
import com.jastt.business.services.jira.JiraClientException;
import com.jastt.business.services.jira.JiraProjectService;
import com.jastt.frontend.beans.LoginBean;
import com.jastt.frontend.utils.Dialogs;
import com.jastt.frontend.utils.Faces;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.crypto.hash.Sha512Hash;
import org.apache.shiro.subject.Subject;
import org.primefaces.model.LazyDataModel;

@Component(value="usersBean")
@Scope("session")
public class UsersBean implements Serializable {
	
	private static final long serialVersionUID = 2092800049856823809L;

	private static final Logger LOG = LoggerFactory.getLogger(UsersBean.class);
	
    @Autowired
    private UserService userService;
    
	@Autowired
	private ServerService serverService;
	
	@Autowired
	private PermissionService permissionService;
	
	@Autowired
	private IssueService issueService;
	
	@Autowired
	private JiraProjectService jiraProjectService;
	
	@Autowired
    private LazyDataModel<User> userDataModel;
	
	@Autowired
	private CurrentUserService currentUserService;
	
	@Autowired
	private LoginBean loginBean;
	
    private User user;
    private User currentUser;
    private List<User> users;
       
    private String login;
	private String url;
    private Server server;
	private String username;
	private String name;
	private boolean admin = false;

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
    
	public LazyDataModel<User> getUserDataModel() {
        return userDataModel;
    }

    public void setUserDataModel(LazyDataModel<User> userDataModel) {
        this.userDataModel = userDataModel;
    }
    
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
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
	
	@PostConstruct
    public void init() {
		Subject subject = SecurityUtils.getSubject();
		if (subject.isAuthenticated()) {
			currentUser = currentUserService.getCurrentUser();
		}
        users = userService.getAllUsers(); 
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
	
	public void verifyJiraLogin(User user) {
    	FacesContext fc = FacesContext.getCurrentInstance();
		try {
			Set<Project> availableProjects = jiraProjectService.getAllProjects(user);
			userService.addUser(user);
			User newUser = userService.getUserByLogin(user.getLogin());
	        issueService.update(newUser);	        
	        
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
		} finally {
			resetFields();
		}
		
    }
	
    public void addNewUser() {
        Faces.updateElements(Dialogs.NEW_USER_DIALOG_ID);
        Faces.showDialog(Dialogs.NEW_USER_DIALOG_WIDGET);
    }
	
    public void editUser(User user) {
    	Faces.updateElements(Dialogs.EDIT_USER_DIALOG_ID);
        Faces.showDialog(Dialogs.EDIT_USER_DIALOG_WIDGET);
    	resetFields();
    	this.user = user;
    	if (this.user.getUserRole().equalsIgnoreCase(UserRoleEnum.ADMIN.getMark())) {
    		setAdmin(true);
    	} else {
    		setAdmin(false);
    	}
    	if (this.user.getServer().getUrl() != null) {
    		url = this.user.getServer().getUrl();
    	}
    }
    
    public void confirmUserRemoval(User user) {
    	getUsername(user);
        Faces.showDialog(Dialogs.CONFIRM_USER_REMOVAL_DIALOG_WIDGET);
    }
    
	public void deleteUser() {
		permissionService.deletePermissionsByUser(user);
		if (currentUser.getLogin().equals(user.getLogin()) && currentUserService.currentUserIsAdmin(currentUser)) {
			//loginBean.doLogout();
			SecurityUtils.getSubject().logout();
			userService.deleteUser(user.getLogin());
			try {
				FacesContext fc = FacesContext.getCurrentInstance();
				fc.getExternalContext().redirect("/project-jastt/protected/login.xhtml");
			} catch(Exception e) {
				LOG.error("Exception happened during execution of deleteUser() method. ", e.getMessage());
			}
		} else {
			userService.deleteUser(user.getLogin());
			Faces.hideDialog(Dialogs.CONFIRM_USER_REMOVAL_DIALOG_WIDGET);
			Faces.info("growl", "User has been removed.",
					String.format("User %s successfully removed.", getUsername()));
			updateValues();
		}
		
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
       
    public void saveUser() {
    	if (isAdmin()) {
    		user.setUserRole(UserRoleEnum.ADMIN.getMark());
    		user.setPassword(new Sha512Hash(user.getPassword(), user.getLogin(), 1).toHex());
    		userService.addUser(user);
    		Faces.info("growl", "User successfully updated",
	        		String.format("User %s has been updated.", getUsername(user)));
    	} else {
    		user.setUserRole(UserRoleEnum.USER.getMark());
    		server = new Server(url);
            server = serverService.getServerByUrl(url);
    		if(server == null){
    			serverService.addServer(url);
    			server = serverService.getServerByUrl(url);
    		}
    		user.setServer(serverService.getServerByUrl(url));
    		if(verifyJiraCredentials(user)) {
    			Faces.info("growl", "User successfully updated",
    	        		String.format("User %s has been updated.", getUsername(user)));
            }  	
    		
    	}
    	resetFields();
    	updateValues();
    }
    
    @PreDestroy
    public void resetFields() {
    	name = null;
    	login = null;
    	url = null;
    	admin = false;
    }
    	

    public void updateValues() {
        users = userService.getAllUsers();
    }
    
    public void cancelHandle(ActionEvent action) {
    	resetFields();		
    	try {		
    		FacesContext.getCurrentInstance().getExternalContext().redirect("/project-jastt/protected/admin.xhtml");		
    	} catch(Exception e) {		
    		LOG.error(e.getMessage());		
    	}		
    	updateValues();
    }

}
