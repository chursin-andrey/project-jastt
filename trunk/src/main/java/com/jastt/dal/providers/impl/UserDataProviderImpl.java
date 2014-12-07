package com.jastt.dal.providers.impl;

import java.util.List;
import java.util.ArrayList;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.jastt.business.domain.entities.User;
import com.jastt.dal.entities.UserEntity;
import com.jastt.dal.exceptions.DaoException;
import com.jastt.dal.providers.UserDataProvider;
import com.jastt.utils.annotations.DefaultProfile;


@Repository(value="userDataProvider")
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

	@Transactional	
	@Override
	public void updatePassword(User user, String newPassword) {
		Session session = sessionFactory.getCurrentSession();

		try {
			user.setPassword(newPassword);
		} catch (Exception ex) {
			LOG.error(String.format("Error update user pas by user=%s", user), ex);
		}
		
	}

	@Transactional
	@Override
	public User getUserByLogin(String login) {
		Session session = sessionFactory.getCurrentSession();
		
		User user = null;
		try {
			Criteria criteria = session.createCriteria(UserEntity.class);
			criteria.add(Restrictions.eq("login", login));

			UserEntity dataEntity = (UserEntity) criteria.uniqueResult();
			if (dataEntity != null) {
				user = mappingService.map(dataEntity, User.class);
			}
		} catch (Exception ex) {
			LOG.error(String.format("Error loading user by login=%s", login), ex);
		} 
		return user;
	}
	

	@Transactional
	@Override
	public List<User> getAllUsers()
	{
		Session session = sessionFactory.getCurrentSession();
		
		List<User> users = new ArrayList<User>();
		try {
			Criteria criteria = session.createCriteria(UserEntity.class);
			List<UserEntity> userEntities = criteria.list();
			if(userEntities != null){
				for(UserEntity userEntity : userEntities){
					users.add(mappingService.map(userEntity, User.class));
				}
			}
		} catch (Exception ex) {
			LOG.error(String.format("Error loading user"), ex);
		}
		return users;
		
	}
	
	
	//add others item
	@Transactional
	@Override
	public void addUser(User user)
	{
		Session session = sessionFactory.getCurrentSession();
		try {
			
			
		} catch (HibernateException ex) {
        	LOG.error("Hibernate error occured while add user", ex.getMessage());	
        	throw new DaoException(ex);
		} catch (Exception ex) {
			LOG.error("Unknown error occured while add user", ex.getMessage());
			throw new DaoException(ex);
		}
	}
	
	//add others item
	@Transactional
	@Override
	public void editUser(User user) {
	}
	
	@Transactional
	@Override
	public void deleteUserByLogin(String login) {
		Session session = sessionFactory.getCurrentSession();
		try {
			session.delete(login);
			
		} catch (HibernateException ex) {
        	LOG.error("Hibernate error occured while edit user", ex.getMessage());	
        	throw new DaoException(ex);
		} catch (Exception ex) {
			LOG.error("Unknown error occured while edit user", ex.getMessage());
			throw new DaoException(ex);
		}
		
		
	}

}
