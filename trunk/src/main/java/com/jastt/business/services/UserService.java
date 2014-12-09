package com.jastt.business.services;

import java.util.List;

import com.jastt.business.domain.entities.User;

public interface UserService {
	
	User getUserByLogin(String login);
	
	List<User> getAllUsers();
	void addUser(User user);
	void updateUser(User user);
	void deleteUser(String login);
//	void chackUSer(User user);
}



