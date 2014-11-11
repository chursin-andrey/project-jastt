package com.jastt.dal.providers.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.jastt.business.domain.entities.User;
import com.jastt.dal.entities.UserEntity;
import com.jastt.dal.providers.UserDataProvider;
import com.jastt.utils.annotations.DefaultProfile;


@Repository
@DefaultProfile
public class UserDataProviderImpl extends BaseDataProviderImpl<UserEntity, User, Integer> implements UserDataProvider {

	private static final Logger LOG = LoggerFactory.getLogger(UserDataProviderImpl.class);

	@Transactional
	@Override
	public User getUserByEmail(String email) {
		Session session = sessionFactory.getCurrentSession();

		User user = null;

		try {
			Criteria criteria = session.createCriteria(UserEntity.class);
			criteria.add(Restrictions.eq("email", email));

			UserEntity dataEntity = (UserEntity) criteria.uniqueResult();
			if (dataEntity != null) {
				user = mappingService.map(dataEntity, User.class);
			}
		} catch (Exception ex) {
			LOG.error(String.format("Error loading user by email=%s", email), ex);
		}

		return user;
	}

	@Override
	public void addUser(User user) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteUser(User user) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public User editUser(User user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updatePassword(String newPassword) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public User getUserByLogin(String login) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<User> getAllUsers() {
		// TODO Auto-generated method stub
		return null;
	}

}
