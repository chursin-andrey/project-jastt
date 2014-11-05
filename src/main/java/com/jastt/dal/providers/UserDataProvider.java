package com.jastt.dal.providers;

import com.jastt.dal.entities.User;
import com.jastt.dal.providers.base.PageableDataProvider;



public interface UserDataProvider extends PageableDataProvider<UserEntity, User, Integer> {

	User getUserByEmail(String email);
}
