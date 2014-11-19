package com.jastt.business.services.jira;

import java.io.IOException;

public class JiraClientException extends IOException {
	
	private static final long serialVersionUID = 1L;

	private Integer statusCode;
	
	public JiraClientException() {
	}
	
	public JiraClientException(String descr) {
		super(descr);
	}

	public JiraClientException(String descr, int statusCode) {
		super(descr);
		this.statusCode = statusCode;
	}

	/**
	 * @return standard error code of failed http request, or null if the exception cause is not an http error.  
	 * For example, statusCode = 401 if authentication to JIRA server has failed.
	 */
	public Integer getStatusCode() {
		return statusCode;
	}
}
