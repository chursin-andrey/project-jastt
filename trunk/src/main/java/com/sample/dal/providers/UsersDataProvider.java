package com.sample.dal.providers;

import com.sample.business.domain.entities.User;
import com.sample.dal.providers.base.PageableDataProvider;

public interface UsersDataProvider extends PageableDataProvider<User> {

	User getUserByEmail(String email);
}
