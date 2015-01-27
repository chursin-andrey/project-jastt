package com.jastt.frontend.beans.worklog;

import java.util.Comparator;

import com.jastt.business.domain.entities.Worklog;

public class WorklogComparator implements Comparator<Worklog> {

	@Override
	public int compare(Worklog w1, Worklog w2) {
		//First sort by author
		int authorResult = w1.getAuthor().compareTo(w2.getAuthor());
		if (authorResult != 0) return authorResult;
		//Next sort by started date
		return w1.getStarted().compareTo(w2.getStarted());
	}

}
