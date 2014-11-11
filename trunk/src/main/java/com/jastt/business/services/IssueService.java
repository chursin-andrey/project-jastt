package com.jastt.business.services;

import com.jastt.business.domain.entities.Project;
import com.jastt.business.domain.entities.Issue;
import com.jastt.business.domain.entities.Assignee;
import com.jastt.business.enums.IssueStatusEnum;
import com.jastt.business.enums.IssueTypeEnum;
import com.jastt.business.enums.PredefinedDateEnum;

import java.util.*;


import java.util.List;

public interface IssueService {
	public List<Issue> getIssues(Project project, IssueStatusEnum status, List<Assignee> assignees, IssueTypeEnum issueType, Date fromDate, Date tillDate);
	public List<Issue> getIssues(Project project, IssueStatusEnum status, List<Assignee> assignees, IssueTypeEnum issueType, PredefinedDateEnum period);
}
