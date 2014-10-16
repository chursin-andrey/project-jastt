package com.sample.utils.frontend.beans;

/**
 * Common generic frontend bean which can be used to display data in tabular
 * form.
 * 
 * @author tillias
 * 
 * @param <T>
 */
public abstract class CommonTableBean<T> extends CommonBean {

	private static final long serialVersionUID = 1L;

	private T selectedItem;
	private T newItem;

	public T getSelectedItem() {
		return selectedItem;
	}

	public void setSelectedItem(T selectedItem) {
		this.selectedItem = selectedItem;
	}

	public T getNewItem() {
		return newItem;
	}

	public void setNewItem(T newItem) {
		this.newItem = newItem;
	}

	public abstract void beforeCreate();

	public abstract void create();
}
