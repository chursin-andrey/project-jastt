package com.sample.dal.providers.base;

import java.io.Serializable;

import com.sample.dal.exceptions.DaoException;
import com.sample.dal.providers.pagination.LoadOptions;
import com.sample.dal.providers.pagination.PagedList;

/**
 * Base contract for server side pagination
 * 
 * @author tillias
 */
public interface PageableDataProvider<T> {

	/**
	 * Attempts to load data object by it's persistent ID
	 */
	T getObject(Serializable id) throws DaoException;

	/**
	 * Gets {@link PagedList} of entries using given {@link LoadOptions}
	 * 
	 */
	PagedList<T> getObjects(LoadOptions loadOptions);

	/**
	 * Persists given object. Treats it as new
	 */
	void create(T object) throws DaoException;

	/**
	 * Updates existing object.
	 * 
	 * @throws DaoException
	 *             if no persisted object is found
	 */
	void update(T object) throws DaoException;
}
