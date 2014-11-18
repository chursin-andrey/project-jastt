package com.jastt.dal.providers.impl;

import java.util.Date;
import java.util.List;

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
import com.jastt.business.enums.IssueStatusEnum;
import com.jastt.business.enums.IssueTypeEnum;
import com.jastt.dal.entities.IssueEntity;
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
		} finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
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
		} finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
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
		} finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }

		return iss;
	}

	@Override
	public Issue getLatestIssue() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void saveIssues(List<Issue> issues) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<Issue> getIssues(Project project, IssueStatusEnum status,
			List<Assignee> assignees, IssueTypeEnum issueType, Date fromDate,
			Date toDate) {
		// TODO Auto-generated method stub
		return null;
	}

}
