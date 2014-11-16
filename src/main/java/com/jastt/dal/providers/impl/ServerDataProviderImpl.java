package com.jastt.dal.providers.impl;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.jastt.business.domain.entities.Server;
import com.jastt.dal.entities.ServerEntity;
import com.jastt.dal.providers.ServerDataProvider;
import com.jastt.utils.annotations.DefaultProfile;


@Repository
@DefaultProfile
public class ServerDataProviderImpl extends BaseDataProviderImpl<ServerEntity, Server, Integer> implements ServerDataProvider {
	
	private static final Logger LOG = LoggerFactory.getLogger(ServerDataProviderImpl.class);

	@Transactional
	@Override
	public Server getServerByUrl(String url) {
		Session session = sessionFactory.getCurrentSession();

		Server server = null;

		try {
			Criteria criteria = session.createCriteria(ServerEntity.class);
			criteria.add(Restrictions.eq("url", url));

			ServerEntity dataEntity = (ServerEntity) criteria.uniqueResult();
			if (dataEntity != null) {
				server = mappingService.map(dataEntity, Server.class);
			}
		} catch (Exception ex) {
			LOG.error(String.format("Error loading server by url=%s", url), ex);
		} finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }

		return server;
	}

	@Transactional
	@Override
	public Server getServerByName(String name) {
		Session session = sessionFactory.getCurrentSession();

		Server server = null;

		try {
			Criteria criteria = session.createCriteria(ServerEntity.class);
			criteria.add(Restrictions.eq("name", name));

			ServerEntity dataEntity = (ServerEntity) criteria.uniqueResult();
			if (dataEntity != null) {
				server = mappingService.map(dataEntity, Server.class);
			}
		} catch (Exception ex) {
			LOG.error(String.format("Error loading server by name=%s", name), ex);
		} finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }

		return server;
	}

}
