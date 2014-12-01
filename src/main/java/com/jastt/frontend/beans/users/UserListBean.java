package com.jastt.frontend.beans.users;

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
@Scope(value="session")
public class UserListBean {
	private static final Logger LOG = LoggerFactory.getLogger(UserListBean.class);
	
	private List<User> users;
	
	//@Autowired
	//private UserService userService;
	
	@PostConstruct
	public void init(){
		//users = userService.getAllUsers();
		
		users = new ArrayList<User>();
		
		//temporary block, for test
		for(int i=0; i<=10; i++){
			User u = new User();
			u.setId(i);
			u.setLogin("TestUser"+i);
			u.setFirstName("TestUserName"+i);
			users.add(u );
		}
		//--------			
	}
	
	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}

	
	
	
}