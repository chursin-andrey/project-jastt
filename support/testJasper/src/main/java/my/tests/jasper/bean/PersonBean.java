package my.tests.jasper.bean;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import my.tests.jasper.model.Person;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
//import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ManagedBean
@RequestScoped
public class PersonBean {
	
	private static final Logger LOG = LoggerFactory.getLogger(PersonBean.class);

	private List<Person> personList = new ArrayList<Person>();

	public List<Person> getPersonList() {
		personList.clear();
		
		Person person = new Person();
		person.setFirstName("John");
		person.setLastName("Doe");
		Calendar calendar = Calendar.getInstance();
		calendar.set(1980, 1, 1);
		person.setDateOfBirth(calendar.getTime());
		personList.add(person);
		
		person = new Person();
		person.setFirstName("Andrew");
		person.setLastName("Black");
		calendar.set(1985, 6, 15);
		person.setDateOfBirth(calendar.getTime());
		personList.add(person);
		
		return personList;
	}

	public void setPersonList(List<Person> personList) {
		this.personList = personList;
	}
	
	public void exportToPDF(ActionEvent actionEvent) throws JRException, IOException {
		Map<String,Object> params= new HashMap<String,Object>();
		params.put("currDate", new Date());
		
		File jasperFile = new File(FacesContext.getCurrentInstance().getExternalContext().getRealPath("/testReport.jasper"));
		JRBeanCollectionDataSource beanCollectionDataSource = new JRBeanCollectionDataSource(getPersonList());
		JasperPrint jasperPrint = JasperFillManager.fillReport(jasperFile.getPath(), params, beanCollectionDataSource);
		
		HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
		response.addHeader("Content-disposition","attachment; filename=report.pdf");
		ServletOutputStream stream = response.getOutputStream();
		
		JasperExportManager.exportReportToPdfStream(jasperPrint, stream);
		
		stream.flush();
		stream.close();
		FacesContext.getCurrentInstance().responseComplete();
	}
	
	public void exportToXls(ActionEvent actionEvent) throws JRException, IOException {
		Map<String,Object> params= new HashMap<String,Object>();
		params.put("currDate", new Date());
		
		File jasperFile = new File(FacesContext.getCurrentInstance().getExternalContext().getRealPath("/testReport.jasper"));
		JRBeanCollectionDataSource beanCollectionDataSource = new JRBeanCollectionDataSource(getPersonList());
		JasperPrint jasperPrint = JasperFillManager.fillReport(jasperFile.getPath(), params, beanCollectionDataSource);
		
		HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
		response.addHeader("Content-disposition","attachment; filename=report.xls");
		ServletOutputStream stream = response.getOutputStream();
		JRXlsExporter exporter = new JRXlsExporter();
		exporter.setParameter(JRXlsExporterParameter.JASPER_PRINT, jasperPrint);
		exporter.setParameter(JRXlsExporterParameter.IS_IGNORE_CELL_BORDER, Boolean.FALSE);
		exporter.setParameter(JRXlsExporterParameter.OUTPUT_STREAM, stream);
		exporter.exportReport();
		
		stream.flush();
		stream.close();
		FacesContext.getCurrentInstance().responseComplete();
	}
	
	public void exportToXlsx(ActionEvent actionEvent) throws JRException, IOException {
		Map<String,Object> params= new HashMap<String,Object>();
		params.put("currDate", new Date());
		
		File jasperFile = new File(FacesContext.getCurrentInstance().getExternalContext().getRealPath("/testReport.jasper"));
		JRBeanCollectionDataSource beanCollectionDataSource = new JRBeanCollectionDataSource(getPersonList());
		JasperPrint jasperPrint = JasperFillManager.fillReport(jasperFile.getPath(), params, beanCollectionDataSource);
		
		HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
		response.addHeader("Content-disposition","attachment; filename=report.xlsx");
		ServletOutputStream stream = response.getOutputStream();
		JRXlsxExporter exporter = new JRXlsxExporter();
		exporter.setParameter(JRXlsExporterParameter.JASPER_PRINT, jasperPrint);
//		exporter.setParameter(JRXlsExporterParameter.IS_IGNORE_CELL_BORDER, Boolean.TRUE);
		exporter.setParameter(JRXlsExporterParameter.OUTPUT_STREAM, stream);
		exporter.exportReport();
		
		stream.flush();
		stream.close();
		FacesContext.getCurrentInstance().responseComplete();
	}
}