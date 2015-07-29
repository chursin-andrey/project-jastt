package com.jastt.frontend.beans;

import java.util.ArrayList;
import java.util.List;

import com.jastt.business.domain.entities.Issue;

public class IssueGrouping implements Comparable<IssueGrouping>{
    private String name;
    private List<Issue> issues = new ArrayList<Issue>();

    public IssueGrouping(String name) {
	super();
	this.name = name;
    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public List<Issue> getIssues() {
	return issues;
    }

    public void setIssues(List<Issue> issues) {
	this.issues = issues;
    }

    public void addIssue(Issue issue) {
	issues.add(issue);
    }

    @Override
    public int compareTo(IssueGrouping o) {
	return name.compareTo(o.name);
    }

    public int getTotalTimeSpent() {
        int totalTimeSpent = 0;
        for (Issue issue : issues) {
            totalTimeSpent += issue.getTimeSpent();
        }
	return totalTimeSpent;
    }

    public int getIssuesCount() {
	return issues.size();
    }
}
