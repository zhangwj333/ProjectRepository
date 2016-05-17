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
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import junstech.collab.BaseController;
import junstech.model.Criteria;
import junstech.model.Privilege;
import junstech.model.Paymentaccount;
import junstech.model.TableProperty;
import junstech.model.User;
import junstech.service.CriteriaService;
import junstech.service.GoodService;
import junstech.service.PrivilegeService;
import junstech.service.PaymentaccountService;
import junstech.service.UserService;
import junstech.util.AESEncryption;
import junstech.util.ENVConfig;
import junstech.util.MetaData;
import junstech.util.LanguageUtil;

@Controller
public class PaymentaccountDML extends BaseController{

	public PaymentaccountDML() {
	}
	
	PaymentaccountService paymentaccountService;

	public PaymentaccountService getPaymentaccountService() {
		return paymentaccountService;
	}

	@Autowired
	public void setPaymentaccountService(PaymentaccountService paymentaccountService) {
		this.paymentaccountService = paymentaccountService;
	}
	
	@RequestMapping(value = "/queryPaymentaccounts")
	public ModelAndView queryUsers(@RequestParam("id") String id, @RequestParam("key") String key,
			@RequestParam("page") int page, @RequestParam("size") int size, HttpServletRequest request,
			HttpSession session) throws Exception {
		ModelAndView mv = new ModelAndView();
		int uid = 0;
		if(!id.isEmpty()){
			uid = Integer.parseInt(id);
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", uid);
		map.put("key", key);
		map.put("prev",  (page - 1) * size);
		map.put("next",  size);
		List<Paymentaccount> paymentaccounts = paymentaccountService.selectPaymentaccounts(map);
		List<TableProperty> tablepropertys = new ArrayList<TableProperty>();
		List<TableProperty> searchFactors = new ArrayList<TableProperty>();
		tablepropertys.add(new TableProperty("id", LanguageUtil.getString("id")));
		tablepropertys.add(new TableProperty("payaccount", LanguageUtil.getString("payaccount")));
		mv.addObject("tablepropertys", tablepropertys);
		mv.addObject("tablelines", paymentaccounts);
		mv.addObject("criteria", "Paymentaccount");
		mv.addObject("page", page);
		mv.addObject("size", size);
		mv.addObject("title", LanguageUtil.getString("paymentAccountTitle"));
		searchFactors.add(new TableProperty("id", id));
		searchFactors.add(new TableProperty("key", key));
		searchFactors.add(new TableProperty("page", page));
		searchFactors.add(new TableProperty("size", size));
		mv.addObject("searchFactors", searchFactors);
		if (paymentaccounts.size() < size) {
			mv.addObject("lastpage", page);
		}
		mv.addObject("pagelink", "queryPaymentaccounts");
		mv.setViewName("query");
		mv.addObject(MetaData.ProcessResult, MetaData.ProcessSuccess);
		return this.outputView(session, mv);
	}
	
	@RequestMapping(value = "/queryPaymentaccount", method = RequestMethod.GET)
	public ModelAndView query(@RequestParam("id") int id, HttpServletRequest request, HttpSession session)
			throws Exception {
		ModelAndView mv = new ModelAndView();
		Paymentaccount paymentaccount = paymentaccountService.selectPaymentaccount(id);
		List<TableProperty> tablepropertys = new ArrayList<TableProperty>();
		tablepropertys.add(new TableProperty("id", LanguageUtil.getString("id")));
		tablepropertys.add(new TableProperty("payaccount", LanguageUtil.getString("payaccount")));
		mv.addObject("tablepropertys", tablepropertys);
		mv.addObject("tableline", paymentaccount);
		mv.setViewName("criteriaShow");
		mv.addObject(MetaData.ProcessResult, MetaData.ProcessSuccess);
		return this.outputView(session, mv);
	}

	@RequestMapping(value = "/editPaymentaccount", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam("id") int id, HttpServletRequest request, HttpSession session)
			throws Exception {
		ModelAndView mv = new ModelAndView();
		Paymentaccount paymentaccount = paymentaccountService.selectPaymentaccount(id);
		List<TableProperty> tablepropertys = new ArrayList<TableProperty>();
		tablepropertys.add(new TableProperty("id", LanguageUtil.getString("id")));
		tablepropertys.add(new TableProperty("payaccount", LanguageUtil.getString("payaccount")));
		mv.addObject("tablepropertys", tablepropertys);
		mv.addObject("tableline", paymentaccount);
		mv.addObject("action", "editPaymentaccountProcess");
		mv.addObject("modelAttribute", "paymentaccount");
		mv.setViewName("genEdit");
		mv.addObject(MetaData.ProcessResult, MetaData.ProcessSuccess);
		return this.outputView(session, mv);
	}

	@RequestMapping(value = "/editPaymentaccountProcess", method = RequestMethod.POST)
	public ModelAndView editProcess(@ModelAttribute Paymentaccount paymentaccount, HttpServletRequest request, HttpSession session)
			throws Exception {
		ModelAndView mv = new ModelAndView();

		try {
			paymentaccountService.editPaymentaccount(paymentaccount);
			mv.addObject("message", LanguageUtil.getString("updateSuccess"));
			mv.addObject(MetaData.setNoteType, MetaData.cosmoSuccess);
			mv.addObject(MetaData.ProcessResult, MetaData.ProcessSuccess);
		} catch (Exception e) {
			mv.addObject("message", LanguageUtil.getString("updateFail"));
			mv.addObject(MetaData.setNoteType, MetaData.cosmoDanger);
			mv.addObject(MetaData.ProcessResult, MetaData.ProcessFail);
		}

		mv.addObject(MetaData.setNoteTitle, LanguageUtil.getString("title"));
		mv.addObject(MetaData.completeReturnPage, "redirect.htm?view=content");
		mv.addObject(MetaData.setTargetFrame, MetaData.setTargetAsContentFrame);
		mv.setViewName("complete");
		return this.outputView(session, mv);
	}
	
	@RequestMapping(value = "/createPaymentaccount")
	public ModelAndView create(HttpServletRequest request, HttpSession session) throws Exception {
		ModelAndView mv = new ModelAndView();
		List<TableProperty> tablepropertys = new ArrayList<TableProperty>();
		tablepropertys.add(new TableProperty("payaccount", LanguageUtil.getString("payaccount")));
		
		Paymentaccount paymentaccount = new Paymentaccount();
		mv.addObject("tablepropertys", tablepropertys);
		mv.addObject("tableline", paymentaccount);
		mv.addObject("action", "createPaymentaccountProcess");
		mv.addObject("modelAttribute", "paymentaccount");
		mv.setViewName("genCreate");
		mv.addObject(MetaData.ProcessResult, MetaData.ProcessSuccess);
		return this.outputView(session, mv);
	}

	@RequestMapping(value = "/createPaymentaccountProcess", method = RequestMethod.POST)
	public ModelAndView createProcess(@ModelAttribute Paymentaccount paymentaccount, HttpServletRequest request, HttpSession session)
			throws Exception {
		ModelAndView mv = new ModelAndView();

		try {
			paymentaccountService.createPaymentaccount(paymentaccount);
			mv.addObject("message", LanguageUtil.getString("createSuccess"));
			mv.addObject(MetaData.setNoteType, MetaData.cosmoSuccess);
			mv.addObject(MetaData.ProcessResult, MetaData.ProcessSuccess);
		} catch (Exception e) {
			mv.addObject("message", LanguageUtil.getString("createFail"));
			mv.addObject(MetaData.setNoteType, MetaData.cosmoDanger);
			mv.addObject(MetaData.ProcessResult, MetaData.ProcessFail);
		}

		mv.addObject(MetaData.setNoteTitle, LanguageUtil.getString("title"));
		mv.addObject(MetaData.completeReturnPage, "redirect.htm?view=content");
		mv.addObject(MetaData.setTargetFrame, MetaData.setTargetAsContentFrame);
		mv.setViewName("complete");
		return this.outputView(session, mv);
	}

	@RequestMapping(value = "/deletePaymentaccount")
	public ModelAndView deleteProcess(@RequestParam("id") String id, HttpServletRequest request, HttpSession session)
			throws Exception {
		ModelAndView mv = new ModelAndView();

		try {
			String tempid = id.split(",", 2)[0];
			paymentaccountService.deletePaymentaccount(Integer.valueOf(tempid));
			mv.addObject("message", LanguageUtil.getString("deleteSuccess"));
			mv.addObject(MetaData.setNoteType, MetaData.cosmoSuccess);
			mv.addObject(MetaData.ProcessResult, MetaData.ProcessSuccess);
		} catch (Exception e) {
			mv.addObject("message", LanguageUtil.getString("deleteFail"));
			mv.addObject(MetaData.setNoteType, MetaData.cosmoDanger);
			mv.addObject(MetaData.ProcessResult, MetaData.ProcessFail);
		}

		mv.addObject(MetaData.setNoteTitle, LanguageUtil.getString("title"));
		mv.addObject(MetaData.completeReturnPage, "redirect.htm?view=content");
		mv.addObject(MetaData.setTargetFrame, MetaData.setTargetAsContentFrame);
		mv.setViewName("complete");
		return this.outputView(session, mv);
	}
}
