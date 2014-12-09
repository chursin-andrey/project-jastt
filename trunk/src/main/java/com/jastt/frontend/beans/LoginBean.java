package com.jastt.frontend.beans;

import java.util.Set;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import com.jastt.business.domain.entities.Project;
import com.jastt.business.domain.entities.Server;
import com.jastt.business.domain.entities.User;
import com.jastt.business.services.ServerService;
import com.jastt.business.services.UserService;
import com.jastt.business.services.jira.JiraClientException;
import com.jastt.business.services.jira.JiraProjectService;


@Component()
@Scope("request")
public class LoginBean {
	
	@Autowired
	private JiraProjectService jiraProjectService;
	//private IssueService issueService;
	@Autowired
	private UserService userService;
	@Autowired
	private ServerService serverService;	
	
	
	private static final Logger LOG = LoggerFactory.getLogger(LoginBean.class);
	private String login;
	private String password;
	private String url;
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
	
	private void loginAsAdmin(){
		FacesContext fc = FacesContext.getCurrentInstance();	
		Subject subject = SecurityUtils.getSubject();	
		UsernamePasswordToken token = new UsernamePasswordToken(getLogin()  ,getPassword(), isRememberMe());

		try {	
			subject.login(token);
			fc.getExternalContext().redirect("/project-jastt/protected/admin.xhtml");
			
		} catch (IncorrectCredentialsException ice) {
			
			FacesMessage fm = new FacesMessage(FacesMessage.SEVERITY_ERROR, 
					"Authentication error!", "Wrong username or password!");
			fc.addMessage(null,fm);
			LOG.error("Incorrect Credentials Exception happened during execution of doLogin method. ", ice.getMessage());
			
		} catch (AuthenticationException ae) {
			
			FacesMessage fm = new FacesMessage(FacesMessage.SEVERITY_ERROR, 
					"Authentication error!", "Wrong username or password!");
			fc.addMessage(null,fm);
			LOG.error("Authentication exception happened during execution of doLogin method. ",ae.getMessage());
			
		}catch(Exception e){
			LOG.error("Unknown  exception happened during execution of doLogin method. ", e.getMessage());
		
		} finally {
			token.clear();
		}
	}
	
	private void loginAsUser(){
		FacesContext fc = FacesContext.getCurrentInstance();
		Server server = new Server(url);
		User user = new User(server, login, null, null, password, null);
		
		try{
			//issueService.update(user);
			Set<Project> projects_set = jiraProjectService.getAllProjects(user);   //TODO	!!!
			
			server = serverService.getServerByUrl(getUrl());
			if(server == null){
				serverService.addServer(getUrl());
			}
			user = userService.getUserByLogin(getLogin());
			if(user == null){
				user  = new User();
				user .setLogin(getLogin());
				user .setServer(serverService.getServerByUrl(getUrl()));
				user .setUserRole("user");
				userService.addUser(user );
			}
					
			Subject subject = SecurityUtils.getSubject();
			UsernamePasswordToken token = new UsernamePasswordToken(getLogin()  ,getPassword(), isRememberMe());
			subject.login(token);
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
