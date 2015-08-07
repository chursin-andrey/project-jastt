package com.jastt.frontend.utils;

import java.util.Iterator;

import javax.faces.FacesException;
import javax.faces.application.FacesMessage;
import javax.faces.application.NavigationHandler;
import javax.faces.context.ExceptionHandler;
import javax.faces.context.ExceptionHandlerWrapper;
import javax.faces.context.FacesContext;
import javax.faces.event.ExceptionQueuedEvent;
import javax.faces.event.ExceptionQueuedEventContext;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class DefaultExceptionHandler extends ExceptionHandlerWrapper {
    private static final Log LOG = LogFactory
	    .getLog(DefaultExceptionHandler.class);
    private ExceptionHandler wrapped;

    public DefaultExceptionHandler(ExceptionHandler wrapped) {
	this.wrapped = wrapped;
    }

    @Override
    public ExceptionHandler getWrapped() {
	return this.wrapped;
    }

    @Override
    public void handle() throws FacesException {
//	LOG.error("DefaultExceptionHandler#handle");
	Iterator<ExceptionQueuedEvent> itr = getUnhandledExceptionQueuedEvents()
		.iterator();
	while (itr.hasNext()) {
	    ExceptionQueuedEvent event = itr.next();
	    ExceptionQueuedEventContext context = (ExceptionQueuedEventContext) event
		    .getSource();
	    Throwable thr = context.getException();
	    if (thr instanceof FacesException) {
		FacesContext fc = FacesContext.getCurrentInstance();
		NavigationHandler nav = fc.getApplication()
			.getNavigationHandler();
		try {
		    fc.addMessage(null,
			    new FacesMessage(ExceptionUtils.getStackTrace(thr)));
		    nav.handleNavigation(fc, null, "/public/error.xhtml");
		    fc.renderResponse();
		} finally {
		    itr.remove();
		}
	    }
	}
	getWrapped().handle();
    }
}
