package com.jastt.frontend.beans.worklog;

import java.io.Serializable;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;

import com.jastt.business.domain.entities.Issue;
import com.jastt.business.domain.entities.Worklog;

public class WorklogReportItem implements Serializable {
	private static final long serialVersionUID = 1L;
	
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
	
	public int getNumberOfWorklogs() {
		if (worklogs == null) return 0;
		else return worklogs.size();
	}
	
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

	public String getActuality() {
		HashSet<Issue> issues = new HashSet<Issue>();
		for (Worklog worklog : worklogs) {
			issues.add(worklog.getIssue());
		}
		long sumOfActualities = 0;
		for (Issue issue : issues) {
		    Integer actuality = issue.getActuality();
		    if (actuality!=null) sumOfActualities+=actuality;
		}
		return (sumOfActualities / issues.size()) + "%";
	}

    public static Comparator<WorklogReportItem> getAuthorComparator() {
	return new AuthorComparator();
    }

    private static final class AuthorComparator implements
            Comparator<WorklogReportItem> {
        @Override
        public int compare(WorklogReportItem o1, WorklogReportItem o2) {
        if (o1 == o2) {
            return 0;
        }
        if (o1 == null || o1.author == null) {
            return -1;
        }
        if (o2 == null || o2.author == null) {
            return 1;
        }
        return o1.author.compareTo(o2.author);
        }
    }
}
