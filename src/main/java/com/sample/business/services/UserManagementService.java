package com.sample.business.services;

import java.util.List;

import com.jastt.dal.exceptions.DaoException;
import com.sample.business.domain.entities.User;
import com.sample.business.exceptions.IllegalCredentialsException;
import com.sample.business.exceptions.UserNotFoundException;

public interface UserManagementService {

	void updatePassword(String email, String oldPassword, String newPassword) throws UserNotFoundException,
			IllegalCredentialsException, DaoException;

	List<User> getAllUsers();
}
