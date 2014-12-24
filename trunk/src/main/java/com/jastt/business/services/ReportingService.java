package com.jastt.business.services;

import java.io.IOException;
import java.util.List;

import net.sf.jasperreports.engine.JRException;

import com.jastt.business.domain.entities.Issue;

public interface ReportingService {
	public void exportToPdf(List<Issue> issues) throws JRException, IOException;
	public void exportToXlsx(List<Issue> issues) throws JRException, IOException;
	public void exportToXls(List<Issue> issues) throws JRException, IOException;
}