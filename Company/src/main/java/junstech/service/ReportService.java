package junstech.service;


import junstech.model.Report;

public interface ReportService {
	public Report selectReport(Long id) throws Exception;
	
	public Report selectReport(String reporttime) throws Exception;

	public void createReport(Report report) throws Exception;
	
	public void editReport(Report report) throws Exception;
	
	public void deleteReport(Long id) throws Exception;
}
