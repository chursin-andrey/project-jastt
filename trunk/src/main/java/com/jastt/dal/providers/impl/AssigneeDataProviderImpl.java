package com.jastt.dal.providers.impl;


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
import com.jastt.dal.entities.AssigneeEntity;
import com.jastt.dal.providers.AssigneeDataProvider;
import com.jastt.utils.annotations.DefaultProfile;


@Repository
@DefaultProfile
public class AssigneeDataProviderImpl extends BaseDataProviderImpl<AssigneeEntity, Assignee, Integer> implements AssigneeDataProvider {
	
	private static final Logger LOG = LoggerFactory.getLogger(AssigneeDataProviderImpl.class);


	@Override
	public List<Assignee> getAssigneesByProject(Project project) {
		// TODO Auto-generated method stub
		return null;
	}

}
