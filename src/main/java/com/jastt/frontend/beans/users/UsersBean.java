package com.jastt.frontend.beans.users;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.primefaces.event.CloseEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.jastt.business.domain.entities.Server;
import com.jastt.business.domain.entities.User;
import com.jastt.business.enums.UserRoleEnum;
import com.jastt.business.services.ServerService;
import com.jastt.business.services.UserService;
import com.jastt.frontend.utils.Dialogs;
import com.jastt.frontend.utils.Faces;

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
    private LazyDataModel<User> userDataModel;
	
    private User user;
    private List<User> users;
       
    private String login;
	private String url;
    private String userRole;
    private String email;
    private Server server;
	private String password;
	private String username;
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
    
    public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
    
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
	
    public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}
	
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
    		url = this.user.getServer().getUrl();
    	}
    }
    
    public void confirmUserRemoval(User user) {
    	getUsername(user);
        Faces.showDialog(Dialogs.CONFIRM_USER_REMOVAL_DIALOG_WIDGET);
    }
    
	public void deleteUser() {
		userService.deleteUser(user.getLogin());		
        Faces.info("growl", "User has been removed.",
                String.format("User %s successfully removed.", username));
        updateValues();
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
       
    public void saveUser() {
        if (isAdmin()) {
        	user.setUserRole(UserRoleEnum.ADMIN.getMark());
        } else {
        	user.setUserRole(UserRoleEnum.USER.getMark());
    		server = new Server(url);
    		
            server = serverService.getServerByUrl(url);
    			
            if(server == null) {
            	serverService.addServer(url);
    			server = serverService.getServerByUrl(url);
    		}
    	        
    		user.setServer(serverService.getServerByUrl(url));
        }
        
        userService.updateUser(user);
        updateValues();
        resetFields();
    }
    
    @PreDestroy
    public void resetFields() {
    	name = null;
    	login = null;
    	email = null;
    	url = null;
    	password = null;
    	userRole = null;
    	admin = false;
    }
    	

    public void updateValues() {
        users = userService.getAllUsers();
    }

}
