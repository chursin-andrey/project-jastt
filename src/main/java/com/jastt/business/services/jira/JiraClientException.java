package com.jastt.business.services.jira;

import java.io.IOException;

import com.atlassian.jira.rest.client.api.RestClientException;

public class JiraClientException extends IOException {
	
	private static final long serialVersionUID = 1L;

	private Integer statusCode;
	
	public JiraClientException(Throwable cause) {
		super(cause);
	}
	
	public JiraClientException(RestClientException exception) {
		super(exception.getMessage(), exception);
		this.statusCode = exception.getStatusCode().orNull();
	}

	/** 
	 * @return error code of failed REST request to JIRA server, or null if the exception was caused by another reason. 
	 * A list of possible REST errors and their codes can be found at 
	 * <a href="https://docs.atlassian.com/jira/REST/latest/">JIRA REST API documentation</a>.  
	 * For example, statusCode = 401 if the calling user is not authenticated.
	 */
	public Integer getStatusCode() {
		return statusCode;
	}
}
