package junstech.service.imp;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import junstech.dao.ReportMapper;
import junstech.model.Report;
import junstech.service.ReportService;

@Service("reportService")
public class ReportServiceImp implements ReportService {
	private ReportMapper reportMapper;

	public ReportMapper getReportMapper() {
		return reportMapper;
	}

	@Autowired
	public void setReportMapper(ReportMapper reportMapper) {
		this.reportMapper = reportMapper;
	}

	public List<Report> getAllReport() {
		return reportMapper.selectAll();
	}

	@Transactional(propagation=Propagation.REQUIRED, rollbackFor = { Exception.class })
	public void createReport(Report report) throws Exception {
		reportMapper.insert(report);

	}

	@Transactional(propagation=Propagation.REQUIRED, rollbackFor = { Exception.class })
	public void editReport(Report report) throws Exception {
		reportMapper.updateByPrimaryKey(report);
	}

	@Transactional(propagation=Propagation.NOT_SUPPORTED)
	public Report selectReport(Long id) throws Exception {
		return reportMapper.selectByPrimaryKey(id);
	}

	@Transactional(propagation=Propagation.NOT_SUPPORTED)
	public Report selectReport(String reporttime) throws Exception {
		return reportMapper.selectByReportTime(reporttime);
	}

	@Transactional(propagation=Propagation.REQUIRED, rollbackFor = { Exception.class })
	public void deleteReport(Long id) throws Exception {
		reportMapper.deleteByPrimaryKey(id);

	}

	public ReportServiceImp() {
	}
}
