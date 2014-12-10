package com.jastt.frontend.beans.users;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.jastt.business.domain.entities.User;
import com.jastt.business.services.UserService;
import com.jastt.frontend.beans.LoginBean;


@Component
@Scope("request")
public class UserListBean implements Serializable{
	
	private static final long serialVersionUID = 2092800049856823809L;

	private static final Logger LOG = LoggerFactory.getLogger(UserListBean.class);
	
	private List<User> users;
	
	@Autowired
	private UserService userService;
	
	@PostConstruct
	public void init(){
		users = userService.getAllUsers();
		
	}
	
	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}

	
	
	
}
