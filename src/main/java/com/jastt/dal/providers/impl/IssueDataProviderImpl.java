package com.jastt.dal.providers.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Projections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.jastt.business.domain.entities.Assignee;
import com.jastt.business.domain.entities.Issue;
import com.jastt.business.domain.entities.Project;
import com.jastt.business.enums.IssueStatusEnum;
import com.jastt.business.enums.IssueTypeEnum;
import com.jastt.dal.entities.IssueEntity;
import com.jastt.dal.exceptions.DaoException;
import com.jastt.dal.providers.IssueDataProvider;
import com.jastt.utils.annotations.DefaultProfile;

@Repository
@DefaultProfile
public class IssueDataProviderImpl extends BaseDataProviderImpl<IssueEntity, Issue, Integer> implements IssueDataProvider {
	
	private static final Logger LOG = LoggerFactory.getLogger(IssueDataProviderImpl.class);
	
	@Transactional
	@Override
	public Issue getIssueByKey(String key) {
		Session session = sessionFactory.getCurrentSession();

		Issue iss = null;

		try {
			Criteria criteria = session.createCriteria(IssueEntity.class);
			criteria.add(Restrictions.eq("key", key));

			IssueEntity dataEntity = (IssueEntity) criteria.uniqueResult();
			if (dataEntity != null) {
				iss = mappingService.map(dataEntity, Issue.class);
			}
		} catch (Exception ex) {
			LOG.error(String.format("Error loading Issue by key=%s", key), ex);
		}

		return iss;
	}

	@Transactional
	@Override
	public Issue getIssueByStatus(String status) {
		Session session = sessionFactory.getCurrentSession();

		Issue iss = null;

		try {
			Criteria criteria = session.createCriteria(IssueEntity.class);
			criteria.add(Restrictions.eq("status", status));

			IssueEntity dataEntity = (IssueEntity) criteria.uniqueResult();
			if (dataEntity != null) {
				iss = mappingService.map(dataEntity, Issue.class);
			}
		} catch (Exception ex) {
			LOG.error(String.format("Error loading Issue by status=%s", status), ex);
		}

		return iss;
	}
	
	@Transactional
	@Override
	public Issue getIssueByProject(Project project) {
		Session session = sessionFactory.getCurrentSession();

		Issue iss = null;

		try {
			Criteria criteria = session.createCriteria(IssueEntity.class);
			criteria.add(Restrictions.eq("project", project));

			IssueEntity dataEntity = (IssueEntity) criteria.uniqueResult();
			if (dataEntity != null) {
				iss = mappingService.map(dataEntity, Issue.class);
			}
		} catch (Exception ex) {
			LOG.error(String.format("Error loading Issue by project=%s", project), ex);
		}

		return iss;
	}
	

	
	//!!!
	@Transactional
	@Override
	public Issue getLatestIssue(Project project) {
		Session session = sessionFactory.getCurrentSession();
		List<Issue> issList = new ArrayList<>();
		Issue issue = null;
		List<IssueEntity> entityList = new ArrayList<>();
		
		try{
			Criteria cr = session.createCriteria(IssueEntity.class);
			cr.add(Restrictions.eq("projectEntity.id", project.getId()))
			.addOrder(Order.asc("created"));						
			entityList = cr.list();			
			for (IssueEntity ie : entityList) {
				Issue is = mappingService.map(ie, Issue.class);
				issList.add(is);
			}
			issue = issList.get(issList.size()-1);
			
		} catch (HibernateException ex) {
        	LOG.error("Hibernate error occured while getting latest Issue", ex.getMessage());	
        	throw new DaoException(ex);
		} catch (Exception ex) {
			LOG.error("Exception error occured while getting latest Issue", ex.getMessage());
			throw new DaoException(ex);
		}
		return issue;
	}
	

	//remove
	@Transactional
	@Override
	public Issue getLatestIssue() {
		Issue issue = null;
		try{
			Session session = sessionFactory.getCurrentSession();
			Criteria cr = session
				    .createCriteria(Issue.class)
				    .setProjection(Projections.max("id"));
			cr.setMaxResults(1);
			int LatestIssue = (int)cr.uniqueResult();
			cr = session.createCriteria(IssueEntity.class).add(Restrictions.eq("id", LatestIssue));
			IssueEntity is = (IssueEntity)cr.uniqueResult();
			issue = mappingService.map(is, Issue.class);
		} catch (HibernateException ex) {
        	LOG.error("Hibernate error occured while getting latest Issue", ex.getMessage());	
        	throw new DaoException(ex);
		} catch (Exception ex) {
			LOG.error("Hibernate error occured while getting latest Issue", ex.getMessage());
			throw new DaoException(ex);
		}
		return issue;
	}
	
	
	
	
	
	@Transactional
	@Override
	public void saveIssues(List<Issue> issues) {
		Session session = sessionFactory.getCurrentSession();
		try {
			
			session.save(issues);
			
		} catch (HibernateException ex) {
        	LOG.error("Hibernate error occured while save Issue", ex.getMessage());	
        	throw new DaoException(ex);
		} catch (Exception ex) {
			LOG.error("Hibernate error occured while save Issue", ex.getMessage());
			throw new DaoException(ex);
		}
		
	}

	@Transactional
	@Override
	public List<Issue> getIssues(Project project, IssueStatusEnum status,
			Assignee assignee, IssueTypeEnum issueType, Date fromDate,
			Date toDate) {
				
		
		List<Issue> resultList = new ArrayList<>();
		List<IssueEntity> entityList = new ArrayList<>();
		try{
			Session session = sessionFactory.getCurrentSession();
			Criteria isuCriteria = session.createCriteria(IssueEntity.class);
				if(project != null) isuCriteria.add(Restrictions.eq("projectEntity.id", project.getId() ) );
				if(status != null) isuCriteria.add(Restrictions.eq("IssueStatusEnum.status", status ) );
				if(assignee != null) isuCriteria.add(Restrictions.eq("assigneeEntity.id", assignee.getId() ) );
				if(issueType != null) isuCriteria.add(Restrictions.eq("IssueTypeEnum.issueType", issueType ) );
				if(fromDate != null) isuCriteria.add(Restrictions.eq("IssueEntity.created", fromDate ) );
				if(toDate != null) isuCriteria.add(Restrictions.eq("IssueEntity.updated", toDate ) );
					
			entityList = isuCriteria.list();
			for (IssueEntity is : entityList) {
				Issue isue = mappingService.map(is, Issue.class);
				resultList.add(isue);
			}
		} catch (HibernateException ex) {
        	LOG.error("Hibernate error occured while getIssues many parametrs", ex.getMessage());
        	throw new DaoException(ex);
		} catch (Exception ex) {
			LOG.error("Hibernate error occured while getIssues many parametrs", ex.getMessage());
			throw new DaoException(ex);
		}
		return resultList;
	}

	
	

}
