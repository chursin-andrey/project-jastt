package com.jastt.dal.providers.impl;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.jastt.business.domain.entities.Assignee;
import com.jastt.business.domain.entities.Project;
import com.jastt.business.domain.entities.User;
import com.jastt.dal.entities.AssigneeEntity;
import com.jastt.dal.entities.ProjectEntity;
import com.jastt.dal.exceptions.DaoException;
import com.jastt.dal.providers.ProjectDataProvider;
import com.jastt.utils.annotations.DefaultProfile;

@Repository
@DefaultProfile
public class ProjectDataProviderImpl extends BaseDataProviderImpl<ProjectEntity, Project, Integer> implements ProjectDataProvider {

	private static final Logger LOG = LoggerFactory.getLogger(IssueDataProviderImpl.class);

	@Transactional
	@Override
	public Project getProjectByName(String name) {
		Session session = sessionFactory.getCurrentSession();

		Project prj = null;

		try {
			Criteria criteria = session.createCriteria(ProjectEntity.class);
			criteria.add(Restrictions.eq("name", name));

			ProjectEntity dataEntity = (ProjectEntity) criteria.uniqueResult();
			if (dataEntity != null) {
				prj = mappingService.map(dataEntity, Project.class);
			}
		} catch (Exception ex) {
			LOG.error(String.format("Error loading Project by name=%s", name), ex);
		}

		return prj;
	}

	@Transactional
	@Override
	public Project getProjectByKey(String key) {
		Session session = sessionFactory.getCurrentSession();

		Project prj = null;

		try {
			Criteria criteria = session.createCriteria(ProjectEntity.class);
			criteria.add(Restrictions.eq("key", key));

			ProjectEntity dataEntity = (ProjectEntity) criteria.uniqueResult();
			if (dataEntity != null) {
				prj = mappingService.map(dataEntity, Project.class);
			}
		} catch (Exception ex) {
			LOG.error(String.format("Error loading Project by key=%s", key), ex);
		}

		return prj;
	}

	@Transactional
	@Override
	public List<Project> getAvailableProjectsForUser(User currentUser) {
		List<Project> resultList = new ArrayList<>();
		List<ProjectEntity> entityList = new ArrayList<>();
		try{
			Session session = sessionFactory.getCurrentSession();
			Criteria prjCriteria = session.createCriteria(Project.class).add(
					Restrictions.eq("userEntity.id", currentUser.getId()));
			entityList = prjCriteria.list();
			for (ProjectEntity pj : entityList) {
				Project prjt = mappingService.map(pj, Project.class);
				resultList.add(prjt);
			}
		} catch (HibernateException ex) {
        	LOG.error("Hibernate error occured while getting Project", ex.getMessage());
        	throw new DaoException(ex);
		} catch (Exception ex) {
			LOG.error("Hibernate error occured while getting Project", ex.getMessage());
			throw new DaoException(ex);
		}
		return resultList;
	}

}
