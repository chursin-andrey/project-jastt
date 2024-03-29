package com.jastt.business.domain.entities;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.joda.time.DateTime;
import org.joda.time.Days;

import com.jastt.business.domain.entities.Assignee;
import com.jastt.business.domain.entities.Project;

public class Issue extends PersistentEntity<Integer>{
	
	private static final long serialVersionUID = -3505282155291252607L;
	
	private String version;
	private Assignee assignee;
	private Project project;
	private String key;
	private String issueType;
	private String status;
	private Date created;
	private Date updated;
	private Date due;
	private Date resolved;
	private String priority;
	private String summary;
	private int timeSpent;
	private Set<Worklog> worklogs = new HashSet<Worklog>(0);
	private String component;
	
	public Issue(){
		
	}
	
	
	public Issue(String version, Assignee assignee, Project project,
			String key, String issueType, String status, Date created,
			Date updated, String priority, int timeSpent) {
		
		super();
		this.version = version;
		this.assignee = assignee;
		this.project = project;
		this.key = key;
		this.issueType = issueType;
		this.status = status;
		this.created = created;
		this.updated = updated;
		this.priority = priority;
		this.timeSpent = timeSpent;
	}


	public String getVersion() {
		return version;
	}
	
	public void setVersion(String version) {
		this.version = version;
	}
	
	public Assignee getAssignee() {
		return assignee;
	}
	
	public void setAssignee(Assignee assignee) {
		this.assignee = assignee;
	}
	
	public Project getProject() {
		return project;
	}
	
	public void setProject(Project project) {
		this.project = project;
	}
	
	public String getKey() {
		return key;
	}
	
	public void setKey(String key) {
		this.key = key;
	}
	
	public String getIssueType() {
		return issueType;
	}
	
	public void setIssueType(String issueType) {
		this.issueType = issueType;
	}
	
	public String getStatus() {
		return status;
	}
	
	public void setStatus(String status) {
		this.status = status;
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
	
	public Date getDue() {
		return due;
	}


	public void setDue(Date due) {
		this.due = due;
	}


	public Date getResolved() {
		return resolved;
	}


	public void setResolved(Date resolved) {
		this.resolved = resolved;
	}


	public String getPriority() {
		return priority;
	}
	
	public void setPriority(String priority) {
		this.priority = priority;
	}
	
	public int getTimeSpent() {
		return timeSpent;
	}
	
	public void setTimeSpent(int timeSpent) {
		this.timeSpent = timeSpent;
	}


	public Integer getActuality() {
		if (due == null || (resolved == null && updated == null)) {
			return 0;
		}
		int daysUntilDue = Days.daysBetween(
				new DateTime(created).withTimeAtStartOfDay(),
				new DateTime(due).withTimeAtStartOfDay()).getDays();
		int daysUntilResolved = Days.daysBetween(
				new DateTime(created).withTimeAtStartOfDay(),
				new DateTime(resolved != null ? resolved : updated)
						.withTimeAtStartOfDay()).getDays();
		return (((1 + daysUntilDue) * 100) / (1 + daysUntilResolved));
	}

	public String getSummary() {
		return summary;
	}


	public void setSummary(String summary) {
		this.summary = summary;
	}

	public Set<Worklog> getWorklogs() {
		return worklogs;
	}


	public void setWorklogs(Set<Worklog> worklogs) {
		this.worklogs = worklogs;
	}


	public String getComponent() {
	    return component;
	}


	public void setComponent(String component) {
	    this.component = component;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((key == null) ? 0 : key.hashCode());
		result = prime * result + ((project == null) ? 0 : project.hashCode());
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
		Issue other = (Issue) obj;
		if (key == null) {
			if (other.getKey() != null)
				return false;
		} else if (!key.equals(other.getKey()))
			return false;
		if (project == null) {
			if (other.getProject() != null)
				return false;
		} else if (!project.equals(other.getProject()))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return key + " " + summary;
	}

	public String getUrl() {
		String serverUrl = project.getServer().getUrl();
		return serverUrl + (serverUrl.endsWith("/") ? "" : "/") + "browse/"
				+ key;
	}

	public static Comparator<Issue> getTypeComparator() {
	    return new IssueTypeComparator();
	}

	public static Comparator<Issue> getNameComparator() {
	    return new NameComparator();
	}

	public static Comparator<Issue> getComponentComparator() {
	    return new ComponentComparator();
	}

	private static final class IssueTypeComparator implements Comparator<Issue> {
	    @Override
	    public int compare(Issue o1, Issue o2) {
	    	if (o1 == o2) {
	    		return 0;
	    	}
	    	if (o1 == null || o1.issueType == null) {
	    		return -1;
	    	}
	    	if (o2 == null || o2.issueType == null) {
	    		return 1;
	    	}
	    	return o1.issueType.compareTo(o2.issueType);
	    }
	}

	private static final class NameComparator implements Comparator<Issue> {
	    @Override
	    public int compare(Issue i1, Issue i2) {	
	    	if (i1 == i2) {
	    		return 0;
	    	}
	    	if (i1 == null || i1.assignee == null) {
	    		return -1;
	    	}
	    	if (i2 == null || i2.assignee == null) {
	    		return 1;
	    	}
	    	return i1.assignee.getName().compareTo(i2.assignee.getName());
	    }
	}

	// TODO
	private static final class ComponentComparator implements Comparator<Issue> {
	    @Override
	    public int compare(Issue o1, Issue o2) {
	    	if (o1 == o2) {
	    		return 0;
	    	}
	    	if (o1 == null || o1.component == null) {
	    		return -1;
	    	}
	    	if (o2 == null || o2.component == null) {
	    		return 1;
	    	}
	    	return o1.component.compareTo(o2.component);
	    }
	}
}