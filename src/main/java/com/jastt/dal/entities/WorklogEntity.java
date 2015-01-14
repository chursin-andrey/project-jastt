package com.jastt.dal.entities;



import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * WorklogEntity generated by hbm2java
 */
@Entity
@Table(name = "WORKLOG")
public class WorklogEntity extends GenericDalEntity<Integer> implements java.io.Serializable {

	private IssueEntity issueEntity;
	private String self;
	private String author;
	private Date created;
	private Date updated;
	private Date started;
	private int timeSpent;

	public WorklogEntity() {
	}

	public WorklogEntity(IssueEntity issueEntity, String self, String author, Date created,
			Date updated, Date started, int timeSpent) {
		this.issueEntity = issueEntity;
		this.self = self;
		this.author = author;
		this.created = created;
		this.updated = updated;
		this.started = started;
		this.timeSpent = timeSpent;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ISSUE_ID", nullable = false)
	public IssueEntity getIssueEntity() {
		return this.issueEntity;
	}

	public void setIssueEntity(IssueEntity issueEntity) {
		this.issueEntity = issueEntity;
	}

	@Column(name = "AUTHOR", nullable = false, length = 100)
	public String getAuthor() {
		return this.author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	@Column(name = "SELF", unique = true, nullable = false, length = 255)
	public String getSelf() {
		return this.self;
	}

	public void setSelf(String self) {
		this.self = self;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATED", nullable = false, length = 26)
	public Date getCreated() {
		return this.created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "UPDATED", nullable = false, length = 26)
	public Date getUpdated() {
		return this.updated;
	}

	public void setUpdated(Date updated) {
		this.updated = updated;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "STARTED", nullable = false, length = 26)
	public Date getStarted() {
		return this.started;
	}

	public void setStarted(Date started) {
		this.started = started;
	}

	@Column(name = "TIME_SPENT", nullable = false)
	public int getTimeSpent() {
		return this.timeSpent;
	}

	public void setTimeSpent(int timeSpent) {
		this.timeSpent = timeSpent;
	}

}