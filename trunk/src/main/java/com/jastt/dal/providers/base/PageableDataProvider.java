package com.jastt.dal.providers.base;

import java.io.Serializable;

import com.jastt.business.domain.entities.PersistentEntity;
import com.jastt.dal.entities.GenericDalEntity;
import com.jastt.dal.providers.pagination.LoadOptions;
import com.jastt.dal.providers.pagination.PagedList;

public interface PageableDataProvider<T extends GenericDalEntity<ID>, 
										K extends PersistentEntity<ID>, 
										ID extends Serializable> 
		extends GenericDataProvider<T, K, ID> {

	/**
	 * Gets {@link PagedList} of entries using given {@link LoadOptions}
	 * 
	 */
	public PagedList<K> getObjects(LoadOptions loadOptions, Class<T> dalEntityClass, Class<K> domainEntityClass);
}
