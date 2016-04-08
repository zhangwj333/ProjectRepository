package junstech.collab.mgr.report;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.itextpdf.text.DocumentException;

import junstech.collab.BaseController;
import junstech.model.Financereceivable;
import junstech.model.Financetype;
import junstech.model.Ledger;
import junstech.model.Report;
import junstech.service.FinancereceivableService;
import junstech.service.FinancetypeService;
import junstech.service.LedgerService;
import junstech.service.ReportService;
import junstech.util.MetaData;
import junstech.util.PDFReport;

@Controller
public class ReportDML extends BaseController {

	FinancereceivableService financereceivableService;

	LedgerService ledgerService;

	FinancetypeService financetypeService;

	ReportService reportService;

	public ReportService getReportService() {
		return reportService;
	}

	@Autowired
	public void setReportService(ReportService reportService) {
		this.reportService = reportService;
	}

	public FinancetypeService getFinancetypeService() {
		return financetypeService;
	}

	@Autowired
	public void setFinancetypeService(FinancetypeService financetypeService) {
		this.financetypeService = financetypeService;
	}

	public LedgerService getLedgerService() {
		return ledgerService;
	}

	@Autowired
	public void setLedgerService(LedgerService ledgerService) {
		this.ledgerService = ledgerService;
	}

	public FinancereceivableService getFinancereceivableService() {
		return financereceivableService;
	}

	@Autowired
	public void setFinancereceivableService(FinancereceivableService financereceivableService) {
		this.financereceivableService = financereceivableService;
	}

	@RequestMapping(value = "/createReportProcess")
	public ModelAndView GenerateLastMonthReport(@RequestParam("time") String time, HttpServletRequest request,
			HttpSession session) throws Exception {
		if(time.isEmpty()){
			return GenerateReport(df.format(new Date()), request, session);
		}
		return GenerateReport(time, request, session);

	}

	@RequestMapping(value = "/queryReportProcess")
	public ModelAndView queryLastMonthReport(@RequestParam("time") String time, HttpServletRequest request,
			HttpSession session) throws Exception {
		ModelAndView mv = new ModelAndView();
		Report report = reportService.selectReport(time);
		mv.addObject("reportPath", report.getPath());
		mv.setViewName("report");
		mv.addObject(MetaData.ProcessResult, MetaData.ProcessSuccess);
		return mv;
	}

	@RequestMapping(value = "/queryHistoryReportProcess")
	public ModelAndView queryHistoryReport(@RequestParam("year") String year, @RequestParam("month") String month,
			HttpServletRequest request, HttpSession session) throws Exception {
		return queryLastMonthReport(year.trim() + StringUtils.leftPad(month.trim(), 2, "0"), request, session);

	}

	public ModelAndView GenerateReport(String targetDate, HttpServletRequest request,
			HttpSession session) throws Exception {
		
		ModelAndView mv = new ModelAndView();
		try {
			System.out.println("开始生成上月报表.........");
			String date = targetDate;
			String[] dateInfo = date.split("-");
			String month;
			if ((Integer.parseInt(dateInfo[1]) - 1) == 0) {
				dateInfo[0] = String.valueOf(Integer.parseInt(dateInfo[0]) - 1);
				month = "12";
			} else {
				month = StringUtils.leftPad(String.valueOf(Integer.parseInt(dateInfo[1]) - 1), 2, "0");
			}
			String endOfMonth = null;
			if (month.matches("01|03|05|07|08|10|12")) {
				endOfMonth = "31";
			} else if (month.matches("04|06|09|11")) {
				endOfMonth = "30";
			} else {
				endOfMonth = "28";
				if (Integer.valueOf(dateInfo[0]) % 4 == 0) {
					endOfMonth = "29";
				}
			}
			Map<String, Object> mapMonth = new HashMap<String, Object>();
			mapMonth.put("startdate", dateInfo[0] + "-" + month + "-01");
			mapMonth.put("enddate", dateInfo[0] + "-" + month + "-" + endOfMonth);
			Map<String, Object> mapYear = new HashMap<String, Object>();
			mapYear.put("startdate", dateInfo[0] + "-01-01");
			mapYear.put("enddate", dateInfo[0] + "-12-31");
			Map<String, Double> financeSumMonth = new HashMap<String, Double>();
			Map<String, Double> financeSumYear = new HashMap<String, Double>();
			List<Financetype> financetypes = financetypeService.selectAllFinancetypes();
			for (Financetype financetype : financetypes) {
				mapMonth.put("name", financetype.getName());
				mapYear.put("name", financetype.getName());
				List<Ledger> ledger = ledgerService.selectTotalByFields(mapMonth);
				if (ledger.isEmpty() || ledger.get(0) == null || ledger.get(0).getAmount() == null) {
					financeSumMonth.put(financetype.getName(), Double.valueOf(0));
				} else {
					financeSumMonth.put(financetype.getName(), ledger.get(0).getAmount());
				}
				ledger = ledgerService.selectTotalByFields(mapYear);
				if (ledger.isEmpty() || ledger.get(0) == null || ledger.get(0).getAmount() == null) {
					financeSumYear.put(financetype.getName(), Double.valueOf(0));
				} else {
					financeSumYear.put(financetype.getName(), ledger.get(0).getAmount());
				}
			}
			Map<String, Object> conditionOfFinancereceivable = new HashMap<String, Object>();
			conditionOfFinancereceivable.put("key", "");
			conditionOfFinancereceivable.put("type", "未结清");
			List<Financereceivable> financereceivables = financereceivableService
					.selectSummary(conditionOfFinancereceivable);
			String fileName = PDFReport.generateReport(financeSumMonth, financeSumYear, financereceivables);
			File file = new File(MetaData.reportPath + fileName);
			if (!file.exists() || (file.getTotalSpace() == 0)) {
				throw new Exception("Fail to create report");
			}
			Report report = reportService.selectReport(dateInfo[0] + month);
			if (report == null) {
				report = new Report();
				report.setReporttime(dateInfo[0] + month);
				report.setPath("/report" + fileName);
				reportService.createReport(report);
			} else {
				report.setPath("/report" + fileName);
				reportService.editReport(report);
			}
			System.out.println("完成生成上月报表.........");
			mv.addObject("message", "新建报表成功");
			mv.addObject(MetaData.setNoteType, MetaData.cosmoSuccess);
			mv.addObject(MetaData.ProcessResult, MetaData.ProcessSuccess);
		} catch (Exception e) {
			mv.addObject("message", "创建失败，请重新操作!");
			mv.addObject(MetaData.setNoteType, MetaData.cosmoDanger);
			mv.addObject(MetaData.ProcessResult, MetaData.ProcessFail);
		}
		mv.addObject(MetaData.setNoteTitle, "结果");
		mv.addObject(MetaData.completeReturnPage, "redirect.htm?view=content");
		mv.addObject(MetaData.setTargetFrame, MetaData.setTargetAsContentFrame);
		mv.setViewName("complete");
		return this.outputView(session, mv);
	}
	
	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

}
