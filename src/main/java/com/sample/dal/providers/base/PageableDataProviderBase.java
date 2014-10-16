package com.sample.dal.providers.base;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.sample.dal.exceptions.DaoException;
import com.sample.dal.providers.pagination.FilterOption;
import com.sample.dal.providers.pagination.LoadOptions;
import com.sample.dal.providers.pagination.PagedList;
import com.sample.dal.providers.pagination.SortOption;
import com.sample.infrastructure.services.MappingService;
import com.sample.utils.CollectionUtils;

/**
 * 
 * @author tillias
 * 
 * @param <B>
 *            Type of data object
 * @param <T>
 *            Type of domain object
 */
public abstract class PageableDataProviderBase<B, T> implements PageableDataProvider<T> {

	private static final Logger LOG = LoggerFactory.getLogger(PageableDataProviderBase.class);

	@Autowired
	private SessionFactory sessionFactory;

	@Autowired
	private MappingService mappingService;

	public Session getSession() {
		return sessionFactory.getCurrentSession();
	}

	public B mapToDataObject(T domainEntity) {
		return mappingService.map(domainEntity, getDataObjectClass());
	}

	public T mapToDomainObject(B dataEntity) {
		return mappingService.map(dataEntity, getDomainObjectClass());
	}

	/**
	 * Gets {@link Class} of data object to be handled by concrete
	 * {@link PageableDataProviderBase} implementation
	 * 
	 * @return
	 */
	public abstract Class<B> getDataObjectClass();

	/**
	 * Gets {@link Class} of domain object to be handled by concrete
	 * {@link PageableDataProviderBase} implementation
	 * 
	 * @return
	 */
	public abstract Class<T> getDomainObjectClass();

	@Transactional
	@Override
	public T getObject(Serializable id) throws DaoException {
		Session session = getSession();

		try {
			@SuppressWarnings("unchecked")
			B dataEntity = (B) session.get(getDataObjectClass(), id);
			return mapToDomainObject(dataEntity);
		} catch (Exception ex) {
			LOG.error(String.format("Error loading object with ID=%s", id));
			throw new DaoException(ex);
		}
	}

	@Transactional
	@Override
	public PagedList<T> getObjects(LoadOptions loadOptions) {
		Session session = getSession();

		PagedList<T> result = new PagedList<>();

		try {
			Criteria countCriteria = session.createCriteria(getDataObjectClass());
			fillCriteria(countCriteria, loadOptions, true);

			long rowsCount = (long) countCriteria.uniqueResult();

			if (rowsCount > 0) {
				Criteria criteria = session.createCriteria(getDataObjectClass());
				fillCriteria(criteria, loadOptions, false);

				@SuppressWarnings("unchecked")
				List<B> dataEntities = criteria.list();
				for (B de : dataEntities) {
					T course = mapToDomainObject(de);
					result.add(course);
				}
			}

			result.setTotalSize(rowsCount);
		} catch (Exception ex) {
			LOG.error("Error loading students", ex);
		}

		return result;
	}

	/**
	 * Default implementation of new business entity persisting. Maps business
	 * entity to the data entity and attempts to save it as new
	 */
	@Transactional
	@Override
	public void create(T object) throws DaoException {
		Session session = sessionFactory.getCurrentSession();

		try {
			B dataEntity = mappingService.map(object, getDataObjectClass());
			session.save(dataEntity);
		} catch (Exception ex) {
			LOG.error("Error saving new data entity", ex);
			throw new DaoException(ex);
		}
	}

	/**
	 * Default implementation of new business entity persisting. Maps business
	 * entity to the data entity and attempts to update it
	 */
	@Transactional
	@Override
	public void update(T object) throws DaoException {
		Session session = sessionFactory.getCurrentSession();

		try {
			B dataEntity = mappingService.map(object, getDataObjectClass());
			session.update(dataEntity);
		} catch (Exception ex) {
			LOG.error("Error updating existing entity", ex);
			throw new DaoException(ex);
		}
	}

	protected void fillCriteria(Criteria source, LoadOptions loadOptions, boolean countOnly) {
		List<FilterOption> filters = loadOptions.getFilters();
		if (CollectionUtils.hasItems(filters)) {
			for (FilterOption f : filters) {
				source.add(Restrictions.eq(f.getField(), f.getValue()));
			}
		}

		Criterion masterFilter = getMasterFilterCriteria(loadOptions);
		if (masterFilter != null) {
			source.add(masterFilter);
		}

		List<SortOption> sorts = loadOptions.getSorts();
		Collections.sort(sorts);

		if (CollectionUtils.hasItems(sorts)) {
			for (SortOption s : sorts) {
				switch (s.getSortDirection()) {
				case ASCENDING:
					source.addOrder(Order.asc(s.getSortField()));
					break;
				case DESCENDING:
					source.addOrder(Order.desc(s.getSortField()));
					break;
				default:
					break;
				}
			}
		}

		if (countOnly) {
			source.setProjection(Projections.rowCount());
		} else {
			source.setFirstResult(loadOptions.getSkipItems());
			source.setMaxResults(loadOptions.getTakeItems());
		}
	}

	protected Criterion getMasterFilterCriteria(LoadOptions loadOptions) {
		return null;
	}
}
