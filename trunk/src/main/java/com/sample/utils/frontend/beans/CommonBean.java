package com.sample.utils.frontend.beans;

import java.io.Serializable;

import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.FacesContext;

import org.primefaces.context.RequestContext;
import org.springframework.beans.factory.annotation.Autowired;

import com.sample.business.domain.entities.User;
import com.sample.business.services.CurrentUserService;

/**
 * Common frontend bean which supports basic operations (like
 * {@link #showDialog(String)})
 * 
 * @author tillias
 * 
 */
public abstract class CommonBean implements Serializable {
	private static final long serialVersionUID = 1L;

	@Autowired
	private CurrentUserService currentUserService;

	protected void showDialog(String dialogName) {

		final RequestContext context = RequestContext.getCurrentInstance();
		context.execute(String.format("%s.show()", dialogName));
	}

	protected User getCurrentUser() {
		return currentUserService.getCurrentUser();
	}

	protected void errorMessage(String title, String message) {
		addMessage(FacesMessage.SEVERITY_ERROR, title, message);
	}

	private void addMessage(Severity severity, String title, String message) {
		FacesContext context = FacesContext.getCurrentInstance();
		context.addMessage(null, new FacesMessage(severity, title, message));
	}
}
