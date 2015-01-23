package com.jastt.business.services;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRException;

import com.jastt.business.domain.entities.Issue;

public interface ReportingService {
	public void exportToPdf(String reportFileName, OutputStream outStream, Map<String, Object> params, 
			Collection<?> beanCollection) throws JRException, IOException;
	public void exportToXlsx(String reportFileName, OutputStream outStream, Map<String, Object> reportParams, 
			Collection<?> beanCollection) throws JRException, IOException;
	public void exportToXls(String reportFileName, OutputStream outStream, Map<String, Object> reportParams, 
			Collection<?> beanCollection) throws JRException, IOException;
}
