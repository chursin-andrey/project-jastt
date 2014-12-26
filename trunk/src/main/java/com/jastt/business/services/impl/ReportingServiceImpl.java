package com.jastt.business.services.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleXlsReportConfiguration;
import net.sf.jasperreports.export.SimpleXlsxReportConfiguration;

import org.springframework.stereotype.Service;

import com.jastt.business.domain.entities.Issue;
import com.jastt.business.services.ReportingService;

@Service("reportingService")
public class ReportingServiceImpl implements ReportingService {

//	private static final Logger LOG = LoggerFactory.getLogger(ReportingServiceImpl.class);
//	Map<String,Object> params= new HashMap<String,Object>();
	
	private JRDataSource buildIssueDataSource(List<Issue> issues) {
		if (issues == null || issues.isEmpty()) return new JREmptyDataSource();
		
		List<IssueReportBean> issueReportBeanList = new ArrayList<IssueReportBean>();
		for (Issue issue : issues) {
			issueReportBeanList.add(new IssueReportBean(issue));
		}
		return new JRBeanCollectionDataSource(issueReportBeanList);
	}
	
	@Override
	public void exportToPdf(List<Issue> issueList, OutputStream outStream) throws JRException, IOException {
		InputStream reportStream = getClass().getResourceAsStream("/pdfIssueReport.jasper");
		if (reportStream == null) return;
		//TODO: place try/catch to handle errors internally
		JRDataSource ds = buildIssueDataSource(issueList);
		Map<String, Object> params= new HashMap<String, Object>();
		params.put("currDate", new Date());
		
		JasperPrint jasperPrint = JasperFillManager.fillReport(reportStream, params, ds);
		
		JasperExportManager.exportReportToPdfStream(jasperPrint, outStream);
		
		reportStream.close();
	}

	@Override
	public void exportToXlsx(List<Issue> issueList, OutputStream outStream) throws JRException, IOException {
		InputStream reportStream = getClass().getResourceAsStream("/xlsIssueReport.jasper");
		if (reportStream == null) return;
		//TODO: place try/catch to handle errors internally
		JRDataSource ds = buildIssueDataSource(issueList);
		Map<String, Object> params= new HashMap<String, Object>();
		params.put("currDate", new Date());
		
		JasperPrint jasperPrint = JasperFillManager.fillReport(reportStream, params, ds);
		
		JRXlsxExporter exporter = new JRXlsxExporter();
		exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
		exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(outStream));
		SimpleXlsxReportConfiguration configuration = new SimpleXlsxReportConfiguration();
		exporter.setConfiguration(configuration);
		exporter.exportReport();
		
		reportStream.close();
	}

	@Override
	public void exportToXls(List<Issue> issueList, OutputStream outStream) throws JRException, IOException {
		InputStream reportStream = getClass().getResourceAsStream("/xlsIssueReport.jasper");
		if (reportStream == null) return;
		//TODO: place try/catch to handle errors internally
		JRDataSource ds = buildIssueDataSource(issueList);
		Map<String, Object> params= new HashMap<String, Object>();
		params.put("currDate", new Date());
		
		JasperPrint jasperPrint = JasperFillManager.fillReport(reportStream, params, ds);
		
		JRXlsExporter exporter = new JRXlsExporter();
		exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
		exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(outStream));
		SimpleXlsReportConfiguration configuration = new SimpleXlsReportConfiguration();
		exporter.setConfiguration(configuration);
		exporter.exportReport();
		
		reportStream.close();
	}

}