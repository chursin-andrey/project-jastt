package com.jastt.dal.providers.impl;


import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
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
import com.jastt.dal.entities.IssueEntity;
import com.jastt.dal.exceptions.DaoException;
import com.jastt.dal.providers.AssigneeDataProvider;
import com.jastt.utils.annotations.DefaultProfile;


@Repository
@DefaultProfile
public class AssigneeDataProviderImpl extends BaseDataProviderImpl<AssigneeEntity, Assignee, Integer> implements AssigneeDataProvider {
	
	private static final Logger LOG = LoggerFactory.getLogger(AssigneeDataProviderImpl.class);

	@Transactional
	@Override
	public Set<Assignee> getAssigneesByProject(Project project) {
		Session session = sessionFactory.getCurrentSession();
		Set<Assignee> resultList = new HashSet<>();
		List<AssigneeEntity> entityList = new ArrayList<>();
		try{
			
			Criteria asgnCriteria = session.createCriteria(AssigneeEntity.class).add(
					Restrictions.eq("projectEntity.id", project.getId()));
			entityList = asgnCriteria.list();
			for (AssigneeEntity aet : entityList) {
				Assignee asgn = mappingService.map(aet, Assignee.class);
				resultList.add(asgn);
			}
		} catch (HibernateException ex) {
        	LOG.error("Hibernate error occured while getting Assignee", ex);
        	throw new DaoException(ex);
		} catch (Exception ex) {
			LOG.error("Hibernate error occured while getting Assignee", ex);
			throw new DaoException(ex);
		}
		return resultList;
	}
	
	@Transactional
	@Override
	public Assignee getAssigneeByName(String name) {
		Session session = sessionFactory.getCurrentSession();

		Assignee assignee = null;

		try {
			Criteria criteria = session.createCriteria(AssigneeEntity.class);
			criteria.add(Restrictions.eq("name", name));

			AssigneeEntity dataEntity = (AssigneeEntity) criteria.uniqueResult();
			if (dataEntity != null) {
				assignee = mappingService.map(dataEntity, Assignee.class);
			}
		} catch (Exception ex) {
			LOG.error(String.format("Error loading Assignee by name=%s", name), ex);
		}

		return assignee;
	}

}
