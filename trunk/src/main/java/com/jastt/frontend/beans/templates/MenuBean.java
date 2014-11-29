package com.jastt.frontend.beans.templates;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.jastt.utils.annotations.SessionScope;

@Component(value="menuBean")
@SessionScope
public class MenuBean {

	private String menuItem = "report";

	public void click(String menuItem) {
		this.menuItem = menuItem;
	}

	public String cssClass(String menuItem) {
		if (this.menuItem.equals(menuItem)) {
			return "active";
		}

		return StringUtils.EMPTY;
	}
}
