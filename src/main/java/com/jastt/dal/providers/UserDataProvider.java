package com.jastt.dal.providers;

import com.jastt.dal.entities.UserEntity;
import com.jastt.dal.providers.base.PageableDataProvider;
import com.jastt.business.domain.entities.User;



public interface UserDataProvider extends PageableDataProvider<UserEntity, User, Integer> {

	User getUserByEmail(String email);
}
