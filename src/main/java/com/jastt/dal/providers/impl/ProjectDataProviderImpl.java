package com.jastt.dal.providers.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import com.jastt.business.domain.entities.Project;
import com.jastt.business.domain.entities.User;
import com.jastt.dal.entities.ProjectEntity;
import com.jastt.dal.providers.ProjectDataProvider;

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
		} finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
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
		} finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }

		return prj;
	}

	@Override
	public List<Project> getAvailableProjectsForUser(User currentUser) {
		// TODO Auto-generated method stub
		return null;
	}

}
