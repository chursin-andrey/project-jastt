package com.jastt.business.services;

import java.util.List;

import com.jastt.business.domain.entities.User;

public interface UserService {
	public User getUserByLogin(String login);
	public List<User> getAllUsers();
	public void addUser(User user);
	public void updateUser(User user);
	public void deleteUser(String login);
}



