package com.sample.dal.providers.impl;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.sample.business.domain.entities.User;
import com.sample.dal.entities.UserEntity;
import com.sample.dal.providers.UsersDataProvider;
import com.sample.dal.providers.base.PageableDataProviderBase;
import com.sample.utils.annotations.DefaultProfile;

@Repository
@DefaultProfile
public class UsersDataProviderImpl extends PageableDataProviderBase<UserEntity, User> implements UsersDataProvider {

	private static final Logger LOG = LoggerFactory.getLogger(UsersDataProviderImpl.class);

	@Transactional
	@Override
	public User getUserByEmail(String email) {
		Session session = getSession();

		User user = null;

		try {
			Criteria criteria = session.createCriteria(UserEntity.class);
			criteria.add(Restrictions.eq("email", email));

			UserEntity dataEntity = (UserEntity) criteria.uniqueResult();
			if (dataEntity != null) {
				user = mapToDomainObject(dataEntity);
			}
		} catch (Exception ex) {
			LOG.error(String.format("Error loading user by email=%s", email), ex);
		}

		return user;
	}

	@Override
	public Class<UserEntity> getDataObjectClass() {
		return UserEntity.class;
	}

	@Override
	public Class<User> getDomainObjectClass() {
		return User.class;
	}
}
