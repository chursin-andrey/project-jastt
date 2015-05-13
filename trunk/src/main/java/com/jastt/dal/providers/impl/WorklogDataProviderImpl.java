package com.jastt.dal.providers.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.jastt.business.domain.entities.Issue;
import com.jastt.business.domain.entities.Project;
import com.jastt.business.domain.entities.Worklog;
import com.jastt.dal.entities.WorklogEntity;
import com.jastt.dal.exceptions.DaoException;
import com.jastt.dal.providers.WorklogDataProvider;
import com.jastt.dal.providers.worklog.WorklogSearchOptions;
import com.jastt.utils.annotations.DefaultProfile;

@Repository
@DefaultProfile
public class WorklogDataProviderImpl extends
		BaseDataProviderImpl<WorklogEntity, Worklog, Integer> implements
		WorklogDataProvider {
	
	private static final long serialVersionUID = 1L;

	private static final Logger LOG = LoggerFactory.getLogger(WorklogDataProviderImpl.class);
	
	private Criteria buildCriteria(Session session, WorklogSearchOptions options) {
		Criteria cr = session.createCriteria(WorklogEntity.class)
				.createAlias("issueEntity", "issue");
		
		Project project = options.getProject();
		List<String> authors = options.getAuthors();
		Set<String> issueType = options.getIssueType();
		Set<String> issueStatus = options.getIssueStatus();
		Date fromDate = options.getFromDate();
		Date toDate = options.getToDate();
		
		if (project != null && project.getId() != null) 
			cr.add(Restrictions.eq("issue.projectEntity.id", project.getId()));
		
		if (authors != null && !authors.isEmpty()) {
			Criterion[] criterions = new Criterion[authors.size()];
			int i = 0;
			for (String author : authors) {
				criterions[i++] = Restrictions.eq("author", author);
			}
			
			cr.add(Restrictions.or(criterions));
		}
		
		if (issueType != null && !issueType.isEmpty()) cr.add(Restrictions.in("issue.issueType", issueType));
		if (issueStatus != null && !issueStatus.isEmpty()) cr.add(Restrictions.in("issue.status", issueStatus));
		
		if (fromDate != null) cr.add(Restrictions.ge("started", fromDate));
		if (toDate != null) cr.add(Restrictions.le("started", toDate));
		
		return cr;
	}
	
	@Transactional
	@Override
	public List<Worklog> getWorklogs(Issue issue) {
		Session session = sessionFactory.getCurrentSession();
		
		List<Worklog> worklogList = new ArrayList<Worklog>();
		try {
			Criteria cr = session.createCriteria(WorklogEntity.class)
					.add(Restrictions.eq("issueEntity.id", issue.getId()));
			List<WorklogEntity> worklogEntityList = cr.list();
			
			for (WorklogEntity worklogEntity : worklogEntityList) {
				Worklog worklog = mappingService.map(worklogEntity, Worklog.class);
				worklogList.add(worklog);
			}
			
		} catch (HibernateException ex) {
			LOG.error("Hibernate error while loading worklog for issue", ex);
        	throw new DaoException(ex);
		} catch (Exception ex) {
			LOG.error("Unknown error occured while loading worklog for issue", ex);
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
			Criteria cr = session.createCriteria(WorklogEntity.class)
					.add(Restrictions.eq("self", self));
			WorklogEntity dataEntity = (WorklogEntity) cr.uniqueResult();			
			
			if (dataEntity != null) {
				worklog = mappingService.map(dataEntity, Worklog.class);
			}
			
		} catch (HibernateException ex) {
			LOG.error("Hibernate error while loading worklog by self", ex);
        	throw new DaoException(ex);
		} catch (Exception ex) {
			LOG.error("Unknown error occured while loading worklog by self", ex);
        	throw new DaoException(ex);
		}
		return worklog;
	}

	@Transactional
	@Override
	public List<Worklog> getWorklogs(Project project) {
		Session session = sessionFactory.getCurrentSession();
		
		List<Worklog> worklogList = new ArrayList<Worklog>();
		try {
			Criteria cr = session.createCriteria(WorklogEntity.class)
					.createAlias("issueEntity", "issue")
					.add(Restrictions.eq("issue.projectEntity.id", project.getId()));
			@SuppressWarnings("unchecked")
			List<WorklogEntity> worklogEntityList = cr.list();
			
			for (WorklogEntity worklogEntity : worklogEntityList) {
				Worklog worklog = mappingService.map(worklogEntity, Worklog.class);
				worklogList.add(worklog);
			}
		} catch (HibernateException ex) {
			LOG.error("Hibernate error while loading worklog for project", ex);
        	throw new DaoException(ex);
		} catch (Exception ex) {
			LOG.error("Unknown error occured while loading worklog for project", ex);
        	throw new DaoException(ex);
		}
		return worklogList;
	}

	@Transactional
	@Override
	public List<Worklog> getWorklogs(WorklogSearchOptions options) {
		Session session = sessionFactory.getCurrentSession();
		
		List<Worklog> worklogList = new ArrayList<Worklog>();
		try {
			Criteria cr = buildCriteria(session, options);
			@SuppressWarnings("unchecked")
			List<WorklogEntity> worklogEntityList = cr.list();
			
			for (WorklogEntity worklogEntity : worklogEntityList) {
				Worklog worklog = mappingService.map(worklogEntity, Worklog.class);
				worklogList.add(worklog);
			}
		} catch (HibernateException ex) {
			LOG.error("Hibernate error while loading worklog for SearchOptions", ex);
        	throw new DaoException(ex);
		} catch (Exception ex) {
			LOG.error("Unknown error occured while loading worklog for SearchOptions", ex);
        	throw new DaoException(ex);
		}
		return worklogList;
	}

	@Transactional
	@Override
	public List<String> getWorklogAuthors(Project project) {
		Session session = sessionFactory.getCurrentSession();
		
		List<String> authorList = new ArrayList<String>();
		try {
			Criteria cr = session.createCriteria(WorklogEntity.class)
					.createAlias("issueEntity", "issue")
					.add(Restrictions.eq("issue.projectEntity.id", project.getId()))
					.setProjection(Projections.distinct(Projections.property("author")));
			authorList = cr.list();
		} catch (HibernateException ex) {
			LOG.error("Hibernate error while loading worklog authors", ex);
        	throw new DaoException(ex);
		} catch (Exception ex) {
			LOG.error("Unknown error occured while loading worklog authors", ex);
        	throw new DaoException(ex);
		}
		return authorList;
	}

	@Transactional
	@Override
	public List<String> getWorklogIssueTypes(Project project) {
		Session session = sessionFactory.getCurrentSession();
		
		List<String> issueTypeList = new ArrayList<String>();
		try {
			Criteria cr = session.createCriteria(WorklogEntity.class)
					.createAlias("issueEntity", "issue")
					.add(Restrictions.eq("issue.projectEntity.id", project.getId()))
					.setProjection(Projections.distinct(Projections.property("issue.issueType")));
			issueTypeList = cr.list();
		} catch (HibernateException ex) {
			LOG.error("Hibernate error while loading worklog issue types", ex);
        	throw new DaoException(ex);
		} catch (Exception ex) {
			LOG.error("Unknown error occured while loading worklog issue types", ex);
        	throw new DaoException(ex);
		}		
		return issueTypeList;
	}

	@Transactional
	@Override
	public List<String> getWorklogIssueStatuses(Project project) {
		Session session = sessionFactory.getCurrentSession();
		
		List<String> issueStatusList = new ArrayList<String>();
		try {
			Criteria cr = session.createCriteria(WorklogEntity.class)
					.createAlias("issueEntity", "issue")
					.add(Restrictions.eq("issue.projectEntity.id", project.getId()))
					.setProjection(Projections.distinct(Projections.property("issue.status")));
			issueStatusList = cr.list();
		} catch (HibernateException ex) {
			LOG.error("Hibernate error while loading worklog issue statuses", ex);
        	throw new DaoException(ex);
		} catch (Exception ex) {
			LOG.error("Unknown error occured while loading worklog issue statuses", ex);
        	throw new DaoException(ex);
		}		
		return issueStatusList;
	}

}
