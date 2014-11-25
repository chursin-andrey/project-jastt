package com.jastt.business.services.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jastt.business.domain.entities.User;
import com.jastt.business.services.UserService;
import com.jastt.dal.providers.UserDataProvider;

//@Service
public class UserServiceImpl implements UserService{
	private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);	
	
	@Autowired
	private UserDataProvider userDataProvider;
	
	@Override
	public User getUserByLogin(String login) {
		return userDataProvider.getUserByLogin(login);
	}

	@Override
	public List<User> getAllUsers() {
		return userDataProvider.getAllUsers();
	}

	@Override
	public void addUser(User user) {
		userDataProvider.addUser(user);
	}

	@Override
	public void updateUser(User user) {
		userDataProvider.editUser(user);		
	}

	@Override
	public void deleteUser(String login) {
		userDataProvider.deleteUserByLogin(login);	
	}
	
}
	
	


