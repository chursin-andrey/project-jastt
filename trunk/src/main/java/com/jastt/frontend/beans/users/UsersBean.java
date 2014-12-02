package com.jastt.frontend.beans.users;

import java.io.Serializable;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;


import com.jastt.business.services.UserService;
import com.jastt.frontend.beans.users.UsersBean;

@Component(value="usersBean")
@Scope("session")
public class UsersBean implements Serializable {
private static final long serialVersionUID = 2819227216048472445L;
	
	
	private static final Logger LOG = LoggerFactory.getLogger(UsersBean.class);
	
	@Autowired
	private UserService userService;
	
	public void deleteUser(String login){
		userService.deleteUser(login);
	}




}
