package com.jastt.business.services.jira;

public class JiraClientException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;

	public JiraClientException() {
	}

	public JiraClientException(String descr) {
		super(descr);
	}
	
}
