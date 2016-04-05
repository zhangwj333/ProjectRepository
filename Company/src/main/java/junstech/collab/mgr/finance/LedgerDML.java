package junstech.collab.mgr.finance;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;


import junstech.model.Ledger;
import junstech.collab.BaseController;
import junstech.model.Customer;
import junstech.model.Financereceivable;
import junstech.model.Financetype;
import junstech.model.Purchase;
import junstech.model.Report;
import junstech.model.Supplier;
import junstech.model.TableProperty;
import junstech.model.User;
import junstech.service.LedgerService;
import junstech.service.SupplierService;
import junstech.service.CustomerService;
import junstech.service.FinancereceivableService;
import junstech.service.FinancetypeService;
import junstech.service.LedgerService;
import junstech.util.FileUtil;
import junstech.util.MetaData;

@Controller
public class LedgerDML extends BaseController{

	public LedgerDML() {
	}
	
	FinancereceivableService financetypereceivableService;
	
	FinancetypeService financetypeService;
	
	LedgerService ledgerService;
	
	SupplierService supplierService;
	
	CustomerService customerService;
	
	public FinancereceivableService getFinancetypereceivableService() {
		return financetypereceivableService;
	}

	@Autowired
	public void setFinancetypereceivableService(FinancereceivableService financetypereceivableService) {
		this.financetypereceivableService = financetypereceivableService;
	}

	public SupplierService getSupplierService() {
		return supplierService;
	}

	@Autowired
	public void setSupplierService(SupplierService supplierService) {
		this.supplierService = supplierService;
	}

	public CustomerService getCustomerService() {
		return customerService;
	}

	@Autowired
	public void setCustomerService(CustomerService customerService) {
		this.customerService = customerService;
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
	
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		dateFormat.setLenient(false);
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));// true:���������ֵ��false:����Ϊ��ֵ
	}
	
	
	@RequestMapping(value = "/querySummaryCurrentFinanceReceivables")
	public ModelAndView summaryCurrentFinanceReceivables(@RequestParam("id") String id, @RequestParam("key") String key,
			@RequestParam("page") int page, @RequestParam("size") int size, HttpServletRequest request,
			HttpSession session) throws Exception {
		ModelAndView mv = new ModelAndView();
		int uid = 0;
		if(!id.isEmpty()){
			uid = Integer.parseInt(id);
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("key", key);
		map.put("type", "未结清");
		map.put("prev",  (page - 1) * size);
		map.put("next",  size);
		List<Financereceivable> financereceivables = financetypereceivableService.selectSummary(map);
		List<TableProperty> tablepropertys = new ArrayList<TableProperty>();
		List<TableProperty> searchFactors = new ArrayList<TableProperty>();
		tablepropertys.add(new TableProperty("companyid", "客户"));
		tablepropertys.add(new TableProperty("type", "状态"));
		tablepropertys.add(new TableProperty("totalamount", "未结总金额"));
		tablepropertys.add(new TableProperty("nowpay", "已收金额"));
		tablepropertys.add(new TableProperty("needpay", "剩余金额"));
		tablepropertys.add(new TableProperty("note", "注明"));
		mv.addObject("tablepropertys", tablepropertys);
		mv.addObject("tablelines", financereceivables);
		mv.addObject("criteria", "SummaryCurrentFinanceReceivable");
		mv.addObject("page", page);
		mv.addObject("size", size);
		mv.addObject("title", "存货单");
		mv.addObject("showoper", "no");
		searchFactors.add(new TableProperty("id", id));
		searchFactors.add(new TableProperty("key", key));
		searchFactors.add(new TableProperty("page", page));
		searchFactors.add(new TableProperty("size", size));
		mv.addObject("searchFactors", searchFactors);
		if (financereceivables.size() < size) {
			mv.addObject("lastpage", page);
		}
		mv.addObject("pagelink", "querySummaryCurrentFinanceReceivables");
		mv.setViewName("query");
		mv.addObject(MetaData.ProcessResult, MetaData.ProcessSuccess);
		return this.outputView(session, mv);
	}
	
	@RequestMapping(value = "/queryLedgers")
	public ModelAndView manageLedgers(@RequestParam("id") String id, @RequestParam("key") String key,
			@RequestParam("startdate") String startdate, @RequestParam("enddate") String enddate,
			@RequestParam("page") int page, @RequestParam("size") int size, HttpServletRequest request,
			HttpSession session) throws Exception {
		ModelAndView mv = new ModelAndView();
		return prepareView(mv, id, key, startdate, enddate, page, size, session);
	}
	
	@RequestMapping(value = "/queryLedger", method = RequestMethod.GET)
	public ModelAndView queryLedger(@RequestParam("id") int id, HttpServletRequest request, HttpSession session)
			throws Exception {
		ModelAndView mv = new ModelAndView();
		Ledger ledger = ledgerService.selectLedger(id);
		List<TableProperty> tablepropertys = new ArrayList<TableProperty>();
		tablepropertys.add(new TableProperty("id", "ID"));
		tablepropertys.add(new TableProperty("receiveid", "入账编号"));
		tablepropertys.add(new TableProperty("financetype", "财务条目"));
		tablepropertys.add(new TableProperty("companytype", "公司类型"));
		tablepropertys.add(new TableProperty("companyid", "供应商/客户"));
		tablepropertys.add(new TableProperty("paydate", "入账日期"));
		tablepropertys.add(new TableProperty("amount", "金额"));
		tablepropertys.add(new TableProperty("note", "注明"));
		mv.addObject("tablepropertys", tablepropertys);
		mv.addObject("tableline", ledger);
		mv.setViewName("criteriaShow");
		mv.addObject(MetaData.ProcessResult, MetaData.ProcessSuccess);
		return this.outputView(session, mv);
	}

	@RequestMapping(value = "/queryLedgerProof", method = RequestMethod.GET)
	public ModelAndView queryLedgerProof(@RequestParam("id") Long id, HttpServletRequest request, HttpSession session) throws Exception {
		ModelAndView mv = new ModelAndView();
		Ledger ledger = ledgerService.selectLedger(id);
		mv.addObject("proofPath", ledger.getProof());
		mv.addObject("title", "财务凭证");
		mv.setViewName("proof");
		mv.addObject(MetaData.ProcessResult, MetaData.ProcessSuccess);
		return this.outputView(session, mv);
	}
	
	@RequestMapping(value = "/editLedgerProof", method = RequestMethod.GET)
	public ModelAndView editLedgerProof(@RequestParam("id") Long id, HttpServletRequest request, HttpSession session)
			throws Exception {
		ModelAndView mv = new ModelAndView();
		Ledger ledger = ledgerService.selectLedger(id);
		List<TableProperty> tablepropertys = new ArrayList<TableProperty>();
		tablepropertys.add(new TableProperty("id", "ID"));
		mv.addObject("tablepropertys", tablepropertys);
		mv.addObject("tableline", ledger);
		mv.addObject("action", "editLedgerProofProcess");
		mv.addObject("modelAttribute", "Ledger");
		mv.setViewName("proofEdit");
		mv.addObject(MetaData.ProcessResult, MetaData.ProcessSuccess);
		return this.outputView(session, mv);
	}
	
	@RequestMapping(value = "/editLedgerProofProcess", method = RequestMethod.POST)
	public ModelAndView editLedgerProofProcess(@RequestParam("id") Long id, HttpServletRequest request, HttpSession session)
			throws Exception {
		ModelAndView mv = new ModelAndView();

		try {
			MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;      
	        // ����ļ���      
	        MultipartFile file = multipartRequest.getFile("img");  
	        String path = "/" + df.format(new Date()) + FileUtil.getFileExtension(file.getOriginalFilename());
	        FileUtil.save(file, MetaData.transactionPath + path);
			Ledger ledger = ledgerService.selectLedger(Long.valueOf(id));
			ledger.setProof("/transaction" + path);
			ledgerService.editLedger(ledger);
			mv.addObject("message", "更新账目记录成功");
			mv.addObject(MetaData.setNoteType, MetaData.cosmoSuccess);
			mv.addObject(MetaData.ProcessResult, MetaData.ProcessSuccess);
		} catch (Exception e) {
			e.printStackTrace();
			mv.addObject("message", "更新失败，请重试!");
			mv.addObject(MetaData.setNoteType, MetaData.cosmoDanger);
			mv.addObject(MetaData.ProcessResult, MetaData.ProcessFail);
		}

		mv.addObject(MetaData.setNoteTitle, "结果");
		mv.addObject(MetaData.completeReturnPage, "redirect.htm?view=content");
		mv.addObject(MetaData.setTargetFrame, MetaData.setTargetAsContentFrame);
		mv.setViewName("complete");
		return this.outputView(session, mv);
	}
	
	@RequestMapping(value = "/editLedger", method = RequestMethod.GET)
	public ModelAndView editLedger(@RequestParam("id") int id, HttpServletRequest request, HttpSession session)
			throws Exception {
		ModelAndView mv = new ModelAndView();
		Ledger ledger = ledgerService.selectLedger(id);
		List<TableProperty> tablepropertys = new ArrayList<TableProperty>();
		List<Financetype> types = financetypeService.selectAllFinancetypes();
		List<Supplier> suppliers = supplierService.selectAllSuppliers();
		List<Customer> customers = customerService.selectAllCustomers();
		tablepropertys.add(new TableProperty("id", "ID"));
		tablepropertys.add(new TableProperty("receiveid", "入账编号"));
		tablepropertys.add(new TableProperty("financetype", "财务条目"));
		tablepropertys.add(new TableProperty("companytype", "公司类型"));
		tablepropertys.add(new TableProperty("companyid", "供应商/客户"));
		tablepropertys.add(new TableProperty("paydate", "入账日期"));
		tablepropertys.add(new TableProperty("amount", "金额"));
		tablepropertys.add(new TableProperty("note", "注明"));
		mv.addObject("tablepropertys", tablepropertys);
		mv.addObject("tableline", ledger);
		mv.addObject("types", types);
		mv.addObject("suppliers", suppliers);
		mv.addObject("customers", customers);
		mv.addObject("action", "editLedgerProcess");
		mv.addObject("modelAttribute", "Ledger");
		mv.setViewName("genEdit");
		mv.addObject(MetaData.ProcessResult, MetaData.ProcessSuccess);
		return this.outputView(session, mv);
	}

	@RequestMapping(value = "/editLedgerProcess", method = RequestMethod.POST)
	public ModelAndView editLedgerProcess(@ModelAttribute Ledger ledger, HttpServletRequest request, HttpSession session)
			throws Exception {
		ModelAndView mv = new ModelAndView();

		try {
			ledgerService.editLedger(ledger);
			mv.addObject("message", "更新账目记录成功");
			mv.addObject(MetaData.setNoteType, MetaData.cosmoSuccess);
			mv.addObject(MetaData.ProcessResult, MetaData.ProcessSuccess);
		} catch (Exception e) {
			mv.addObject("message", "更新失败，请重试!");
			mv.addObject(MetaData.setNoteType, MetaData.cosmoDanger);
			mv.addObject(MetaData.ProcessResult, MetaData.ProcessFail);
		}

		mv.addObject(MetaData.setNoteTitle, "结果");
		mv.addObject(MetaData.completeReturnPage, "redirect.htm?view=content");
		mv.addObject(MetaData.setTargetFrame, MetaData.setTargetAsContentFrame);
		mv.setViewName("complete");
		return this.outputView(session, mv);
	}
	
	@RequestMapping(value = "/createLedger")
	public ModelAndView createLedger(HttpServletRequest request, HttpSession session) throws Exception {
		ModelAndView mv = new ModelAndView();
		List<TableProperty> tablepropertys = new ArrayList<TableProperty>();
		tablepropertys.add(new TableProperty("receiveid", "入账编号"));
		tablepropertys.add(new TableProperty("financetype", "财务条目"));
		tablepropertys.add(new TableProperty("companytype", "公司类型"));
		tablepropertys.add(new TableProperty("companyid", "供应商/客户"));
		tablepropertys.add(new TableProperty("paydate", "入账日期"));
		tablepropertys.add(new TableProperty("amount", "金额"));
		tablepropertys.add(new TableProperty("note", "注明"));
		Ledger ledger = new Ledger();
		List<Financetype> types = financetypeService.selectAllFinancetypes();
		List<Supplier> suppliers = supplierService.selectAllSuppliers();
		List<Customer> customers = customerService.selectAllCustomers();
		mv.addObject("tablepropertys", tablepropertys);
		mv.addObject("tableline", ledger);
		mv.addObject("types", types);
		mv.addObject("suppliers", suppliers);
		mv.addObject("customers", customers);
		mv.addObject("action", "createLedgerProcess");
		mv.addObject("modelAttribute", "ledger");
		mv.setViewName("genCreate");

		return this.outputView(session, mv);
	}

	@RequestMapping(value = "/createLedgerProcess", method = RequestMethod.POST)
	public ModelAndView createLedgerProcess(@ModelAttribute Ledger Ledger, HttpServletRequest request, HttpSession session)
			throws Exception {
		ModelAndView mv = new ModelAndView();

		try {
			ledgerService.createLedger(Ledger);
			mv.addObject("message", "新建账目记录成功");
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

	@RequestMapping(value = "/deleteLedger")
	public ModelAndView deleteProcess(@RequestParam("id") String id, @RequestParam("key") String key, //
			@RequestParam("startdate") String startdate, @RequestParam("enddate") String enddate, //
			@RequestParam("page") int page, @RequestParam("size") int size, //
			HttpServletRequest request, HttpSession session) throws Exception {
		ModelAndView mv = new ModelAndView();

		try {
			String tempid = id.split(",", 2)[0];
			ledgerService.deleteLedger(Long.parseLong(tempid));
			mv.addObject(MetaData.ProcessResult, MetaData.ProcessSuccess);
			return prepareView(mv, id.split(",", 2)[1], key, startdate, enddate, page, size, session);
		} catch (Exception e) {
			mv.addObject("message", "删除失败，请重新操作!");
			mv.addObject(MetaData.setNoteType, MetaData.cosmoDanger);
			mv.addObject(MetaData.setNoteTitle, "结果");
			mv.addObject(MetaData.completeReturnPage, "redirect.htm?view=content");
			mv.addObject(MetaData.setTargetFrame, MetaData.setTargetAsContentFrame);
			mv.setViewName("complete");
			mv.addObject(MetaData.ProcessResult, MetaData.ProcessFail);
			return this.outputView(session, mv);
		}
	}
	
	public ModelAndView prepareView(//
			ModelAndView mv, //
			String id, String key, //
			String startdate, String enddate, //
			int page, int size, HttpSession session) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", id);
		map.put("key", key);
		map.put("prev",  (page - 1) * size);
		map.put("next",  size);
		map.put("startdate", startdate);
		map.put("enddate", enddate);
		List<Ledger> ledgers = ledgerService.selectLedgers(map);
		List<TableProperty> tablepropertys = new ArrayList<TableProperty>();
		List<TableProperty> searchFactors = new ArrayList<TableProperty>();
		tablepropertys.add(new TableProperty("receiveid", "入账编号"));
		tablepropertys.add(new TableProperty("financetype", "财务条目"));
		tablepropertys.add(new TableProperty("companyid", "供应商/客户"));
		tablepropertys.add(new TableProperty("paydate", "入账日期"));
		tablepropertys.add(new TableProperty("amount", "金额"));
		tablepropertys.add(new TableProperty("note", "注明"));
		mv.addObject("tablepropertys", tablepropertys);
		mv.addObject("tablelines", ledgers);
		mv.addObject("criteria", "Ledger");
		mv.addObject("page", page);
		mv.addObject("size", size);
		mv.addObject("title", "财务凭证");
		searchFactors.add(new TableProperty("id", id));
		searchFactors.add(new TableProperty("key", key));
		searchFactors.add(new TableProperty("startdate", startdate));
		searchFactors.add(new TableProperty("enddate", enddate));
		searchFactors.add(new TableProperty("page", page));
		searchFactors.add(new TableProperty("size", size));
		mv.addObject("searchFactors", searchFactors);
		if (ledgers.size() < size) {
			mv.addObject("lastpage", page);
		}
		mv.addObject("pagelink", "queryLedgers");
		mv.setViewName("query");
		mv.addObject(MetaData.ProcessResult, MetaData.ProcessSuccess);
		return this.outputView(session, mv);
	}
	
	private static SimpleDateFormat df = new SimpleDateFormat("yyyyMMddhhmmss");
}
