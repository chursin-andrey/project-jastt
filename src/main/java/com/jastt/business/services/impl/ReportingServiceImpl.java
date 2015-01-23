package com.jastt.business.services.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.Collection;
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

import com.jastt.business.services.ReportingService;

@Service("reportingService")
public class ReportingServiceImpl implements ReportingService, Serializable {
	private static final long serialVersionUID = 1L;

//	private static final Logger LOG = LoggerFactory.getLogger(ReportingServiceImpl.class);
	
	@Override
	public void exportToPdf(String reportFileName, OutputStream outStream, Map<String, Object> reportParams, 
			Collection<?> beanCollection) throws JRException, IOException {
		InputStream reportStream = getClass().getResourceAsStream(reportFileName);
		if (reportStream == null) return;
		//TODO: place try/catch to handle errors internally
		JRDataSource ds = null;
		if (beanCollection == null || beanCollection.isEmpty()) ds = new JREmptyDataSource();
		else ds = new JRBeanCollectionDataSource(beanCollection);

		JasperPrint jasperPrint = JasperFillManager.fillReport(reportStream, reportParams, ds);
		
		JasperExportManager.exportReportToPdfStream(jasperPrint, outStream);
		
		reportStream.close();
	}

	@Override
	public void exportToXlsx(String reportFileName, OutputStream outStream, Map<String, Object> reportParams, 
			Collection<?> beanCollection) throws JRException, IOException {
		InputStream reportStream = getClass().getResourceAsStream(reportFileName);
		if (reportStream == null) return;
		//TODO: place try/catch to handle errors internally
		JRDataSource ds = null;
		if (beanCollection == null || beanCollection.isEmpty()) ds = new JREmptyDataSource();
		else ds = new JRBeanCollectionDataSource(beanCollection);

		JasperPrint jasperPrint = JasperFillManager.fillReport(reportStream, reportParams, ds);
		
		JRXlsxExporter exporter = new JRXlsxExporter();
		exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
		exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(outStream));
		SimpleXlsxReportConfiguration configuration = new SimpleXlsxReportConfiguration();
		exporter.setConfiguration(configuration);
		exporter.exportReport();
		
		reportStream.close();
	}

	@Override
	public void exportToXls(String reportFileName, OutputStream outStream, Map<String, Object> reportParams, 
			Collection<?> beanCollection) throws JRException, IOException {
		InputStream reportStream = getClass().getResourceAsStream(reportFileName);
		if (reportStream == null) return;
		//TODO: place try/catch to handle errors internally
		JRDataSource ds = null;
		if (beanCollection == null || beanCollection.isEmpty()) ds = new JREmptyDataSource();
		else ds = new JRBeanCollectionDataSource(beanCollection);

		JasperPrint jasperPrint = JasperFillManager.fillReport(reportStream, reportParams, ds);
		
		JRXlsExporter exporter = new JRXlsExporter();
		exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
		exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(outStream));
		SimpleXlsReportConfiguration configuration = new SimpleXlsReportConfiguration();
		exporter.setConfiguration(configuration);
		exporter.exportReport();
		
		reportStream.close();
	}

}