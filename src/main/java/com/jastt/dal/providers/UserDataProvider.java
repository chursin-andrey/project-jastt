package com.jastt.dal.providers;

import java.io.Serializable;
import java.util.List;

import com.jastt.dal.entities.UserEntity;
import com.jastt.dal.providers.base.PageableDataProvider;
import com.jastt.business.domain.entities.User;



public interface UserDataProvider extends PageableDataProvider<UserEntity , User, Integer>, Serializable {


	public void updatePassword(User user, String newPassword);
	public User getUserByLogin(String login);
	public User getUserByEmail(String email);
	public List<User> getAllUsers();
	public void deleteUserByLogin(String login);
	
}