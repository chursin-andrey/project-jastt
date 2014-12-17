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

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import com.jastt.business.domain.entities.Issue;
import com.jastt.business.services.ReportingService;

@Service("reportingService")
public class ReportingServiceImpl implements ReportingService {

//	private static final Logger LOG = LoggerFactory.getLogger(ReportingServiceImpl.class);
			
	@Override
	public void exportToPdf(List<Issue> issueList) throws JRException, IOException {
		if (issueList == null) return;
		List<IssueReportBean> issueReportBeanList = new ArrayList<IssueReportBean>();
		for (Issue issue : issueList) {
			issueReportBeanList.add(new IssueReportBean(issue));
		}
		
		Map<String,Object> params= new HashMap<String,Object>();
		params.put("currDate", new Date());
		
		File jasperFile = new File(FacesContext.getCurrentInstance().getExternalContext().getRealPath("/reports/issueReport.jasper"));
		JRBeanCollectionDataSource beanCollectionDataSource = new JRBeanCollectionDataSource(issueReportBeanList);
		JasperPrint jasperPrint = JasperFillManager.fillReport(jasperFile.getPath(), params, beanCollectionDataSource);
		
		HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
		response.addHeader("Content-disposition","attachment; filename=issueReport.pdf");
		ServletOutputStream stream = response.getOutputStream();
		
		JasperExportManager.exportReportToPdfStream(jasperPrint, stream);
		
		stream.flush();
		stream.close();
		FacesContext.getCurrentInstance().responseComplete();
	}

}