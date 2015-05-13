package com.jastt.frontend.beans;

import java.io.Serializable;
import java.util.Set;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.hash.Sha512Hash;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.jastt.business.domain.entities.Project;
import com.jastt.business.domain.entities.Server;
import com.jastt.business.domain.entities.User;
import com.jastt.business.enums.UserRoleEnum;
import com.jastt.business.services.IssueService;
import com.jastt.business.services.ServerService;
import com.jastt.business.services.UserService;
import com.jastt.business.services.jira.JiraClientException;
import com.jastt.business.services.jira.JiraProjectService;


@Component()
@Scope("session")

public class LoginBean implements Serializable {
	

	private static final long serialVersionUID = 3464721997249898518L;

	@Autowired
	private JiraProjectService jiraProjectService;
	@Autowired
	private UserService userService;
	@Autowired
	private ServerService serverService;	
	@Autowired
	private IssueService issueService;
	
	private static final Logger LOG = LoggerFactory.getLogger(LoginBean.class);
	private String login;
	private String password;
	private String url = "https://seu30.gdc-sbr01.t-systems.com/jira";
	private boolean adminSwitch;
	private boolean rememberMe;

	
	public void doLogin() {
		if(adminSwitch){
			loginAsAdmin();
		}else{
			loginAsUser();
		}	
	}
	
	public String doLogout(){
		SecurityUtils.getSubject().logout();
		return new String("/public/login.xhtml?faces-redirect=true");
	}
	
	public void checkState(){
		Subject subject = SecurityUtils.getSubject();
		if(subject.isAuthenticated()){
			FacesContext fc = FacesContext.getCurrentInstance();
			try{
				fc.getExternalContext().redirect("/project-jastt/protected/main.xhtml");
			}catch(Exception e){
				LOG.error("Exception happened during execution of checkState() method. ", e);
			}
			
		}
	}
	
	
	private void loginAsAdmin(){
		
		FacesContext fc = FacesContext.getCurrentInstance();	
		Subject subject = SecurityUtils.getSubject();	
		UsernamePasswordToken token = new UsernamePasswordToken(getLogin()  ,new Sha512Hash(getPassword(), getLogin(), 1).toHex(), isRememberMe());

		try {	
			User user = userService.getUserByLogin(getLogin());
			if(user == null){
				throw new AuthenticationException();
			}		
			if(user.getUserRole().equals("user")){
				throw new IncorrectCredentialsException();
			}			
			subject.login(token);	
			subject.getSession().setAttribute("login", getLogin());
			subject.getSession().setAttribute("password", getPassword());
			subject.getSession().setAttribute("user", user);
			
			fc.getExternalContext().redirect("/project-jastt/protected/admin.xhtml");
			
		} catch (IncorrectCredentialsException ice) {
			
			FacesMessage fm = new FacesMessage(FacesMessage.SEVERITY_ERROR, 
					"Authentication error!", "Wrong username or password!");
			fc.addMessage(null,fm);
			LOG.error("Incorrect Credentials Exception happened during execution of doLogin method. ", ice);
			
		} catch (AuthenticationException ae) {
			
			FacesMessage fm = new FacesMessage(FacesMessage.SEVERITY_ERROR, 
					"Authentication error!", "Wrong username or password!");
			fc.addMessage(null,fm);
			LOG.error("Authentication exception happened during execution of doLogin method. ",ae);
			
		}catch(Exception e){
			LOG.error("Unknown  exception happened during execution of doLogin method. ", e);
		
		} finally {
			token.clear();
		}
	}
	
	private void loginAsUser(){
		FacesContext fc = FacesContext.getCurrentInstance();
		Server server = new Server(getUrl());
		User user = new User(server, getLogin(), null, null, getPassword(), null);
		
		try{
			/*	 This operator below only need for authentication checking.
			 *	 If it throws an exception, it means that user is not valid or connection is not successful.
			 *	 It is absolutely does not matter which projects will be return.	*/	
			Set<Project> projects_set = jiraProjectService.getAllProjects(user);   
								
			server = serverService.getServerByUrl(getUrl());
			if(server == null){
				serverService.addServer(getUrl());
				server = serverService.getServerByUrl(getUrl());
			}
			
			user = userService.getUserByLogin(getLogin());
			if(user == null){
				user  = new User();
				user .setLogin(getLogin());
				user .setServer(serverService.getServerByUrl(getUrl()));
				user .setUserRole(UserRoleEnum.USER.getMark());	
				userService.addUser(user );
				user = userService.getUserByLogin(getLogin());
				user.setPassword(getPassword());
			}else{
				user.setPassword(getPassword());
			}
						
			Subject subject = SecurityUtils.getSubject();
			UsernamePasswordToken token = new UsernamePasswordToken(getLogin()  ,getPassword(), isRememberMe());
			
			subject.login(token);
			subject.getSession().setAttribute("login", getLogin());
			subject.getSession().setAttribute("password", getPassword());
			subject.getSession().setAttribute("url", getUrl());
			subject.getSession().setAttribute("user", user);
			
			issueService.update(user);
			fc.getExternalContext().redirect("/project-jastt/protected/main.xhtml");
			
			
		}catch(JiraClientException jce){
			if(jce.getStatusCode() == 401 | jce.getStatusCode() == 403){
				
				FacesMessage fm = new FacesMessage(FacesMessage.SEVERITY_ERROR, 
						"Authentication error!", "Wrong Jira username or password!");	
				fc.addMessage(null, fm);		
				
			}else{
				
				FacesMessage fm = new FacesMessage(FacesMessage.SEVERITY_ERROR, 
						"Communication error!", "Wrong  Jira URL or Jira server is not available!");
				fc.addMessage(null, fm);
			}		
		}catch (Exception e) {		
			
				FacesMessage fm = new FacesMessage(FacesMessage.SEVERITY_ERROR, 
						"Communication error!", "Wrong  Jira URL or Jira server is not available!");
				fc.addMessage(null, fm);			
		}
			
	}
	

	public void isAdminValueListener(){		
	}
	
	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
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
	
	
	public boolean isAdminSwitch() {
		return adminSwitch;
	}


	public void setAdminSwitch(boolean adminSwitch) {
		this.adminSwitch = adminSwitch;
	}


	public boolean isRememberMe() {
		return rememberMe;
	}


	public void setRememberMe(boolean rememberMe) {
		this.rememberMe = rememberMe;
	}
	

}
