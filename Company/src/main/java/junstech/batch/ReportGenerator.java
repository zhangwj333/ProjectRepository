package junstech.batch;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.itextpdf.text.DocumentException;

import junstech.model.Financereceivable;
import junstech.model.Financetype;
import junstech.model.Ledger;
import junstech.service.FinancereceivableService;
import junstech.service.FinancetypeService;
import junstech.service.LedgerService;
import junstech.util.PDFReport;


@Component
public class ReportGenerator {

	FinancereceivableService financereceivableService;
	
	LedgerService ledgerService;

	FinancetypeService financetypeService;

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

	@Scheduled(cron = "0 0 0 5 * ?") // execute every month on 5th
	public void GenerateLastMonthReport() throws Exception {
		String date = df.format(new Date());
		String[] dateInfo = date.split("-");
		String month;
		if ((Integer.parseInt(dateInfo[1]) - 1) == 0) {
			dateInfo[0] = String.valueOf(Integer.parseInt(dateInfo[0]) - 1);
			month = "12";
		} else {
			month = StringUtils.leftPad(String.valueOf(Integer.parseInt(dateInfo[1]) - 1), 2, "0");
		}
		String endOfMonth = null;
		if(month.matches("01|03|05|07|08|10|12")){
			endOfMonth = "31";
		}else if(month.matches("04|06|09|11")){
			endOfMonth = "30";
		}else{
			endOfMonth = "28";
			if(Integer.valueOf(dateInfo[0]) % 4 == 0){
				endOfMonth = "29";
			}
		}
		Map<String, Object> mapMonth = new HashMap<String, Object>();
		mapMonth.put("startdate", dateInfo[0] + "-" + month +"-01");
		mapMonth.put("enddate", dateInfo[0] + "-" + month + "-" + endOfMonth);
		Map<String, Object> mapYear = new HashMap<String, Object>();
		mapYear.put("startdate", dateInfo[0] + "-01-01");
		mapYear.put("enddate", dateInfo[0] + "-12-31");
		Map<String, Double> financeSumMonth = new HashMap<String, Double>();
		Map<String, Double> financeSumYear = new HashMap<String, Double>();
		List<Financetype> financetypes = financetypeService.selectAllFinancetypes();
		for(Financetype financetype : financetypes){
			mapMonth.put("name", financetype.getName());
			mapYear.put("name", financetype.getName());
			List<Ledger> ledger = ledgerService.selectTotalByFields(mapMonth);
			if(ledger.get(0).getAmount() == null){
				financeSumMonth.put(financetype.getName(), Double.valueOf(0));
			}else{
				financeSumMonth.put(financetype.getName(), ledger.get(0).getAmount());
			}
			ledger = ledgerService.selectTotalByFields(mapYear);
			if(ledger.get(0).getAmount() == null){
				financeSumYear.put(financetype.getName(), Double.valueOf(0));
			}else{
				financeSumYear.put(financetype.getName(), ledger.get(0).getAmount());
			}
		}
		Map<String, Object> conditionOfFinancereceivable = new HashMap<String, Object>();
		conditionOfFinancereceivable.put("key", "");
		conditionOfFinancereceivable.put("type", "Œ¥Ω·«Â");
		List<Financereceivable> financereceivables = financereceivableService.selectSummary(conditionOfFinancereceivable);
		PDFReport.generateReport(financeSumMonth, financeSumYear, financereceivables);

	}

	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

}
