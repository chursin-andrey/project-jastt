package com.jastt.dal.providers.pagination;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.sample.dal.providers.base.PageableDataProvider;

/**
 * Used by {@link PageableDataProvider} to support server side pagination.
 * 
 * @author tillias
 * 
 */
public class LoadOptions implements Serializable {
	private static final long serialVersionUID = -549474241343669160L;

	private List<FilterOption> filters;
	private List<SortOption> sorts;
	private int skipItems;
	private int takeItems;
	private Serializable masterID;

	public LoadOptions() {
		filters = new ArrayList<FilterOption>();
		sorts = new ArrayList<SortOption>();
	}

	public List<FilterOption> getFilters() {
		return filters;
	}

	public void setFilters(List<FilterOption> filters) {
		this.filters = filters;
	}

	public List<SortOption> getSorts() {
		return sorts;
	}

	public void setSorts(List<SortOption> sorts) {
		this.sorts = sorts;
	}

	public int getSkipItems() {
		return skipItems;
	}

	public void setSkipItems(int skipItems) {
		this.skipItems = skipItems;
	}

	public int getTakeItems() {
		return takeItems;
	}

	public void setTakeItems(int takeItems) {
		this.takeItems = takeItems;
	}

	public Serializable getMasterID() {
		return masterID;
	}

	public void setMasterID(Serializable masterID) {
		this.masterID = masterID;
	}
}
