package com.jastt.dal.providers.impl;


import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.jastt.business.domain.entities.Assignee;
import com.jastt.business.domain.entities.Issue;
import com.jastt.business.domain.entities.Project;
import com.jastt.dal.entities.AssigneeEntity;
import com.jastt.dal.providers.AssigneeDataProvider;
import com.jastt.utils.annotations.DefaultProfile;


@Repository
@DefaultProfile
public class AssigneeDataProviderImpl extends BaseDataProviderImpl<AssigneeEntity, Assignee, Integer> implements AssigneeDataProvider {
	
	private static final Logger LOG = LoggerFactory.getLogger(IssueDataProviderImpl.class);

	@Transactional
	@Override
	public Assignee getAssigneeByName(String name) {
		Session session = sessionFactory.getCurrentSession();

		Assignee assig = null;

		try {
			Criteria criteria = session.createCriteria(AssigneeEntity.class);
			criteria.add(Restrictions.eq("name", name));

			AssigneeEntity dataEntity = (AssigneeEntity) criteria.uniqueResult();
			if (dataEntity != null) {
				assig = mappingService.map(dataEntity, Assignee.class);
			}
		} catch (Exception ex) {
			LOG.error(String.format("Error loading Assignee by name=%s", name), ex);
		} finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }

		return assig;
	}
	
	@Transactional
	@Override
	public Assignee getAssigneeByProject(Project project) {
		Session session = sessionFactory.getCurrentSession();

		Assignee assig = null;

		try {
			Criteria criteria = session.createCriteria(AssigneeEntity.class);
			criteria.add(Restrictions.eq("project", project));

			AssigneeEntity dataEntity = (AssigneeEntity) criteria.uniqueResult();
			if (dataEntity != null) {
				assig = mappingService.map(dataEntity, Assignee.class);
			}
		} catch (Exception ex) {
			LOG.error(String.format("Error loading Assignee by project=%s", project), ex);
		} finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }

		return assig;

	}
	
	@Transactional
	@Override
	public Assignee getAssigneeByIssues(Issue issue) {
		Session session = sessionFactory.getCurrentSession();

		Assignee assig = null;

		try {
			Criteria criteria = session.createCriteria(AssigneeEntity.class);
			criteria.add(Restrictions.eq("issue", issue));

			AssigneeEntity dataEntity = (AssigneeEntity) criteria.uniqueResult();
			if (dataEntity != null) {
				assig = mappingService.map(dataEntity, Assignee.class);
			}
		} catch (Exception ex) {
			LOG.error(String.format("Error loading Assignee by issue=%s", issue), ex);
		} finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }

		return assig;
	}

}
