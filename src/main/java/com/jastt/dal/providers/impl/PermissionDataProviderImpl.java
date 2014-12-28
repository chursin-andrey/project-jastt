package com.jastt.dal.providers.impl;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.jastt.business.domain.entities.Permission;
import com.jastt.business.domain.entities.Project;
import com.jastt.business.domain.entities.User;
import com.jastt.dal.entities.PermissionEntity;
import com.jastt.dal.exceptions.DaoException;
import com.jastt.dal.providers.PermissionDataProvider;
import com.jastt.utils.annotations.DefaultProfile;


@Repository
@DefaultProfile
public class PermissionDataProviderImpl extends BaseDataProviderImpl<PermissionEntity, Permission, Integer> implements PermissionDataProvider {
	
	private static final Logger LOG = LoggerFactory.getLogger(PermissionDataProviderImpl.class);

	@Transactional
	@Override
	public Permission getPermission(User user, Project project) {
		Session session = sessionFactory.getCurrentSession();
		
		Permission perm = null;
		
		try{
			Criteria criteria = session.createCriteria(PermissionEntity.class);
			criteria.add(Restrictions.eq("projectEntity.id", project.getId()))
			.add(Restrictions.eq("userEntity.id", user.getId()));
			
			PermissionEntity dataEntity = (PermissionEntity) criteria.uniqueResult();
			
			if (dataEntity != null) {
				perm = mappingService.map(dataEntity, Permission.class);
			}
			
		} catch (Exception ex) {
			LOG.error(String.format("Error getPermission by user=%s and project=%s  ", user , project ), ex);
		}
		
		return perm;
	}

	@Transactional
	@Override
	public List<Permission> getPermissionByUser(User user) {
		List<Permission> resultList = new ArrayList<>();
		List<PermissionEntity> entityList = new ArrayList<>();
		
		Session session = sessionFactory.getCurrentSession();
		try{
			Criteria criteria = session.createCriteria(PermissionEntity.class);
			if(user != null) {
				criteria.add(Restrictions.eq("userEntity.id", user.getId() ) );
			}
			
			entityList = criteria.list();
			for(PermissionEntity pe : entityList) {
				Permission perm = mappingService.map(pe, Permission.class);
				resultList.add(perm);
			}
			
			
		} catch (Exception ex) {
			LOG.error(String.format("Error getPermission by user=%s ", user), ex.getMessage());
			throw new DaoException(ex);
		}
		
		return resultList;
	}

	/*@Transactional
	@Override
	public Permission getPermissionByUser(User user) {
		Session session = sessionFactory.getCurrentSession();

		Permission perm = null;

		try {
			Criteria criteria = session.createCriteria(PermissionEntity.class);
			criteria.add(Restrictions.eq("user", user));

			PermissionEntity dataEntity = (PermissionEntity) criteria.uniqueResult();
			if (dataEntity != null) {
				perm = mappingService.map(dataEntity, Permission.class);
			}
		} catch (Exception ex) {
			LOG.error(String.format("Error loading Permission by user=%s", user), ex);
		}

		return perm;
	}

	@Transactional
	@Override
	public Permission getPermissionByProject(Project project) {
		Session session = sessionFactory.getCurrentSession();

		Permission perm = null;

		try {
			Criteria criteria = session.createCriteria(PermissionEntity.class);
			criteria.add(Restrictions.eq("project", project));

			PermissionEntity dataEntity = (PermissionEntity) criteria.uniqueResult();
			if (dataEntity != null) {
				perm = mappingService.map(dataEntity, Permission.class);
			}
		} catch (Exception ex) {
			LOG.error(String.format("Error loading Permission by project=%s", project), ex);
		}

		return perm;
	}
*/
}
