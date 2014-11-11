package com.jastt.dal.providers;

import com.jastt.dal.entities.UserEntity;
import com.jastt.dal.providers.base.PageableDataProvider;
import com.jastt.business.domain.entities.User;

import java.util.List;



public interface UserDataProvider extends PageableDataProvider<UserEntity, User, Integer> {


	public void addUser(User user);
	public void deleteUser (User user);
	public User editUser(User user);
	public void updatePassword(String newPassword);
	public User getUserByLogin(String login);
	public List<User> getAllUsers();	
	public User getUserByEmail(String email);
	
	
	
	
}