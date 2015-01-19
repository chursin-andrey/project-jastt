package com.jastt.frontend.beans.worklog;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

@FacesConverter("com.jastt.frontend.beans.worklog.IntToHMConverter")
public class IntToHMConverter implements Converter {

	@Override
	public Object getAsObject(FacesContext context, UIComponent component,
			String value) {
		return new Integer(0);
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component,
			Object value) {
		int time = (Integer) value;
		int hh = time/60; int mm = time%60;
		return String.format("%d:%02d", hh, mm);
	}

}
