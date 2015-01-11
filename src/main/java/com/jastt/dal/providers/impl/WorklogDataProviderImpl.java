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

import com.jastt.business.domain.entities.Issue;
import com.jastt.business.domain.entities.Worklog;
import com.jastt.dal.entities.WorklogEntity;
import com.jastt.dal.exceptions.DaoException;
import com.jastt.dal.providers.WorklogDataProvider;
import com.jastt.utils.annotations.DefaultProfile;

@Repository
@DefaultProfile
public class WorklogDataProviderImpl extends
		BaseDataProviderImpl<WorklogEntity, Worklog, Integer> implements
		WorklogDataProvider {
	
	private static final long serialVersionUID = 1L;

	private static final Logger LOG = LoggerFactory.getLogger(WorklogDataProviderImpl.class);
	
	@Transactional
	@Override
	public List<Worklog> getWorklogs(Issue issue) {
		Session session = sessionFactory.getCurrentSession();
		
		List<Worklog> worklogList = new ArrayList<Worklog>();
		try {
			Criteria cr = session.createCriteria(WorklogEntity.class).
					add(Restrictions.eq("issueEntity.id", issue.getId()));
			List<WorklogEntity> worklogEntityList = cr.list();
			
			for (WorklogEntity worklogEntity : worklogEntityList) {
				Worklog worklog = mappingService.map(worklogEntity, Worklog.class);
				worklogList.add(worklog);
			}
			
		} catch (HibernateException ex) {
			LOG.error("Hibernate error while loading worklog for issue", ex.getMessage());
        	throw new DaoException(ex);
		} catch (Exception ex) {
			LOG.error("Unknown error occured while loading worklog for issue", ex.getMessage());
        	throw new DaoException(ex);
		}
		return worklogList;
	}

	@Transactional
	@Override
	public Worklog getWorklogBySelf(String self) {
		Session session = sessionFactory.getCurrentSession();
		
		Worklog worklog = null;
		try {
			Criteria cr = session.createCriteria(WorklogEntity.class).
					add(Restrictions.eq("self", self));
			WorklogEntity dataEntity = (WorklogEntity) cr.uniqueResult();			
			
			if (dataEntity != null) {
				worklog = mappingService.map(dataEntity, Worklog.class);
			}
			
		} catch (HibernateException ex) {
			LOG.error("Hibernate error while loading worklog by self", ex.getMessage());
        	throw new DaoException(ex);
		} catch (Exception ex) {
			LOG.error("Unknown error occured while loading worklog by self", ex.getMessage());
        	throw new DaoException(ex);
		}
		return worklog;
	}	

}
