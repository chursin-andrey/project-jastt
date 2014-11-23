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

import com.jastt.business.domain.entities.Assignee;
import com.jastt.business.domain.entities.Project;
import com.jastt.dal.entities.AssigneeEntity;
import com.jastt.dal.exceptions.DaoException;
import com.jastt.dal.providers.AssigneeDataProvider;
import com.jastt.utils.annotations.DefaultProfile;


@Repository
@DefaultProfile
public class AssigneeDataProviderImpl extends BaseDataProviderImpl<AssigneeEntity, Assignee, Integer> implements AssigneeDataProvider {
	
	private static final Logger LOG = LoggerFactory.getLogger(AssigneeDataProviderImpl.class);

	@Transactional
	@Override
	public List<Assignee> getAssigneesByProject(Project project) {
		List<Assignee> resultList = new ArrayList<>();
		List<AssigneeEntity> entityList = new ArrayList<>();
		try{
			Session session = sessionFactory.getCurrentSession();
			Criteria asgnCriteria = session.createCriteria(Assignee.class).add(
					Restrictions.eq("projectEntity.id", project.getId()));
			entityList = asgnCriteria.list();
			for (AssigneeEntity aet : entityList) {
				Assignee asgn = mappingService.map(aet, Assignee.class);
				resultList.add(asgn);
			}
		} catch (HibernateException ex) {
        	LOG.error("Hibernate error occured while getting Assignee", ex.getMessage());
        	throw new DaoException(ex);
		} catch (Exception ex) {
			LOG.error("Hibernate error occured while getting Assignee", ex.getMessage());
			throw new DaoException(ex);
		}
		return resultList;
	}

}
