package com.jastt.business.services.impl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.context.FacesContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Service;

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

import com.jastt.business.domain.entities.Issue;
import com.jastt.business.services.ReportingService;

@Service("reportingService")
public class ReportingServiceImpl implements ReportingService {

//	private static final Logger LOG = LoggerFactory.getLogger(ReportingServiceImpl.class);
			
	@Override
	public void exportToPdf(List<Issue> issueList) throws JRException, IOException {
		JRDataSource ds = new JREmptyDataSource();
		if (issueList != null)
		{
			List<IssueReportBean> issueReportBeanList = new ArrayList<IssueReportBean>();
			for (Issue issue : issueList) {
				issueReportBeanList.add(new IssueReportBean(issue));
			}
			ds = new JRBeanCollectionDataSource(issueReportBeanList);
		}
		
		Map<String,Object> params= new HashMap<String,Object>();
		params.put("currDate", new Date());
		
		File jasperFile = new File(FacesContext.getCurrentInstance().getExternalContext().getRealPath("/reports/pdfIssueReport.jasper"));		
		JasperPrint jasperPrint = JasperFillManager.fillReport(jasperFile.getPath(), params, ds);
		
		HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
		response.addHeader("Content-disposition","attachment; filename=issueReport.pdf");
		ServletOutputStream stream = response.getOutputStream();
		
		JasperExportManager.exportReportToPdfStream(jasperPrint, stream);
		
		stream.flush();
		stream.close();
		FacesContext.getCurrentInstance().responseComplete();
	}

	@Override
	public void exportToXlsx(List<Issue> issueList) throws JRException, IOException {
		JRDataSource ds = new JREmptyDataSource();
		if (issueList != null)
		{
			List<IssueReportBean> issueReportBeanList = new ArrayList<IssueReportBean>();
			for (Issue issue : issueList) {
				issueReportBeanList.add(new IssueReportBean(issue));
			}
			ds = new JRBeanCollectionDataSource(issueReportBeanList);
		}
		
		Map<String,Object> params= new HashMap<String,Object>();
		params.put("currDate", new Date());
		
		File jasperFile = new File(FacesContext.getCurrentInstance().getExternalContext().getRealPath("/reports/xlsIssueReport.jasper"));
		JasperPrint jasperPrint = JasperFillManager.fillReport(jasperFile.getPath(), params, ds);
		
		HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
		response.addHeader("Content-disposition","attachment; filename=issueReport.xlsx");
		ServletOutputStream outputStream = response.getOutputStream();
		
		JRXlsxExporter exporter = new JRXlsxExporter();
		exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
		exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(outputStream));
		SimpleXlsxReportConfiguration configuration = new SimpleXlsxReportConfiguration();
		exporter.setConfiguration(configuration);
		exporter.exportReport();
		
		outputStream.flush();
		outputStream.close();
		FacesContext.getCurrentInstance().responseComplete();
	}

	@Override
	public void exportToXls(List<Issue> issueList) throws JRException, IOException {
		JRDataSource ds = new JREmptyDataSource();
		if (issueList != null)
		{
			List<IssueReportBean> issueReportBeanList = new ArrayList<IssueReportBean>();
			for (Issue issue : issueList) {
				issueReportBeanList.add(new IssueReportBean(issue));
			}
			ds = new JRBeanCollectionDataSource(issueReportBeanList);
		}
		
		Map<String,Object> params= new HashMap<String,Object>();
		params.put("currDate", new Date());
		
		File jasperFile = new File(FacesContext.getCurrentInstance().getExternalContext().getRealPath("/reports/xlsIssueReport.jasper"));
		JasperPrint jasperPrint = JasperFillManager.fillReport(jasperFile.getPath(), params, ds);
		
		HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
		response.addHeader("Content-disposition","attachment; filename=issueReport.xls");
		ServletOutputStream outputStream = response.getOutputStream();
		
		JRXlsExporter exporter = new JRXlsExporter();
		exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
		exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(outputStream));
		SimpleXlsReportConfiguration configuration = new SimpleXlsReportConfiguration();
		exporter.setConfiguration(configuration);
		exporter.exportReport();
		
		outputStream.flush();
		outputStream.close();
		FacesContext.getCurrentInstance().responseComplete();
	}

}