package com.jastt.frontend.beans.users;

import java.io.Serializable;








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
import com.jastt.frontend.beans.users.UsersBean;

@Component(value="usersBean")
@Scope("request")
public class UsersBean implements Serializable {
private static final long serialVersionUID = 2819227216048472445L;
	
	
	private static final Logger LOG = LoggerFactory.getLogger(UsersBean.class);
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private ServerDataProvider serverDataProvider;
	
	private String name;
	private String login;
	private String url;
	
	
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

	public void deleteUser(String login){
		userService.deleteUser(login);
		
		//---temporary block---!
		try{
			FacesContext.getCurrentInstance().getExternalContext().redirect("/project-jastt/protected/admin.xhtml");
		}catch(Exception e){
			LOG.error(e.getMessage());
		}
		//----------------------------
		
	}
	
	public String createUser() {
		Server server = serverDataProvider.getServerByUrl(url);
		User newUser = new User();
		newUser.setEmail("e-mail");
		newUser.setName(name);
		newUser.setLogin(login);
		newUser.setPassword("pswd");
		newUser.setServer(server);
		
		userService.addUser(newUser);
		return "protected/admin.xhtml";
	}
	
	public void click(ActionEvent e) {
		e.getComponent().getId();
	}

}
