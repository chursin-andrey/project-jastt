package com.sample.business.services.fakes;

import java.io.Serializable;

import org.springframework.stereotype.Repository;

import com.jastt.dal.exceptions.DaoException;
import com.jastt.dal.providers.UserDataProvider;
import com.jastt.dal.providers.pagination.LoadOptions;
import com.jastt.dal.providers.pagination.PagedList;
import com.jastt.utils.annotations.UnitTestProfile;
import com.sample.business.domain.entities.User;
import com.sample.utils.TestConstants;

@Repository
@UnitTestProfile
public class UsersDataProviderFake implements UserDataProvider {

	private User user;

	public UsersDataProviderFake() {
		user = new User();
		user.setEmail(TestConstants.FAKE_USER_EMAIL);
		user.setPassword(TestConstants.FAKE_USER_PASSWORD);
	}

	@Override
	public User getUserByEmail(String email) {
		if (user.getEmail().equals(email))
			return user;

		return null;
	}

	@Override
	public User getObject(Serializable id) throws DaoException {
		throw new UnsupportedOperationException();
	}

	@Override
	public PagedList<User> getObjects(LoadOptions loadOptions) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void create(User object) throws DaoException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void update(User object) throws DaoException {
		throw new UnsupportedOperationException();
	}
}
