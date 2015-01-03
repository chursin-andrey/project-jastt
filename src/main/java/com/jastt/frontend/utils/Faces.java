package com.jastt.frontend.utils;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.primefaces.context.RequestContext;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

public class Faces {
	
	public static void message(String toComponent, FacesMessage.Severity severity, String summary, String detail) {
        FacesMessage facesMessage = new FacesMessage(severity, summary, detail);
        FacesContext.getCurrentInstance().addMessage(toComponent, facesMessage);
    }
	
	public static void updateElements(String ids) {
        RequestContext.getCurrentInstance().update(ids);
    }

    public static void showDialog(String dialogName) {
        RequestContext.getCurrentInstance().execute(String.format("PF('%s').show()", dialogName));
    }

    public static void hideDialog(String dialogName) {
        RequestContext.getCurrentInstance().execute(String.format("PF('%s').hide()", dialogName));
    }
    
    public static void info(String toComponent, String summary, String detail) {
        message(toComponent, FacesMessage.SEVERITY_INFO, summary, detail);
    }
    
    public static void warn(String toComponent, String summary, String detail) {
        message(toComponent, FacesMessage.SEVERITY_WARN, summary, detail);
    }
    
    public static void error(String toComponent, String summary, String detail) {
        message(toComponent, FacesMessage.SEVERITY_ERROR, summary, detail);
    }
    
    public static void fatal(String toComponent, String summary, String detail) {
        message(toComponent, FacesMessage.SEVERITY_FATAL, summary, detail);
    }

}
