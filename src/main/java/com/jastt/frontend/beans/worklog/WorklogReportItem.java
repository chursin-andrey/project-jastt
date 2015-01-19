package com.jastt.frontend.beans.worklog;

import java.util.List;

import com.jastt.business.domain.entities.Worklog;

public class WorklogReportItem {

	private String author;
	private int totalTimeSpent;
	private List<Worklog> worklogs;

	public WorklogReportItem() {
		
	}
	
	public WorklogReportItem(String author, List<Worklog> worklogs) {
		this.author = author;
		setWorklogs(worklogs);
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public int getTotalTimeSpent() {
		return totalTimeSpent;
	}

	/*public void setTotalTimeSpent(int totalTimeSpent) {
		this.totalTimeSpent = totalTimeSpent;
	}*/

	public List<Worklog> getWorklogs() {
		return worklogs;
	}

	public void setWorklogs(List<Worklog> worklogs) {
		this.worklogs = worklogs;
		totalTimeSpent = 0;
		if (worklogs == null) return;
		for (Worklog worklog : worklogs) {
			totalTimeSpent += worklog.getTimeSpent();
		}
	}
}
