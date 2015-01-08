package com.jastt.business.domain.entities;

import java.util.Date;

public class Worklog extends PersistentEntity<Integer> {
	
	private static final long serialVersionUID = 1L;
	
	private Issue issue;
	private String self;
	private String author;
	private Date created;
	private Date updated;
	private Date started;
	private int timeSpent;
	
	public Worklog() {
		
	}

	public Worklog(Issue issue, String self, String author, Date created, Date updated,
			Date started, int timeSpent) {
		super();
		this.issue = issue;
		this.self = self;
		this.author = author;
		this.created = created;
		this.updated = updated;
		this.started = started;
		this.timeSpent = timeSpent;
	}

	public Issue getIssue() {
		return issue;
	}

	public void setIssue(Issue issue) {
		this.issue = issue;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getSelf() {
		return self;
	}

	public void setSelf(String self) {
		this.self = self;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public Date getUpdated() {
		return updated;
	}

	public void setUpdated(Date updated) {
		this.updated = updated;
	}

	public Date getStarted() {
		return started;
	}

	public void setStarted(Date started) {
		this.started = started;
	}

	public int getTimeSpent() {
		return timeSpent;
	}

	public void setTimeSpent(int timeSpent) {
		this.timeSpent = timeSpent;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((self == null) ? 0 : self.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Worklog other = (Worklog) obj;
		if (self == null) {
			if (other.getSelf() != null)
				return false;
		} else if (!self.equals(other.getSelf()))
			return false;
		return true;
	}
}
