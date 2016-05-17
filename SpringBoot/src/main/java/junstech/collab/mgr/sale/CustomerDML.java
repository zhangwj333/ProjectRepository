package junstech.collab.mgr.sale;

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
import junstech.model.Customer;
import junstech.model.TableProperty;
import junstech.model.User;
import junstech.service.CriteriaService;
import junstech.service.GoodService;
import junstech.service.PrivilegeService;
import junstech.service.CustomerService;
import junstech.service.UserService;
import junstech.util.AESEncryption;
import junstech.util.ENVConfig;
import junstech.util.MetaData;
import junstech.util.LanguageUtil;

@Controller
public class CustomerDML extends BaseController{

	public CustomerDML() {
	}
	
	CustomerService customerService;

	public CustomerService getCustomerService() {
		return customerService;
	}

	@Autowired
	public void setCustomerService(CustomerService customerService) {
		this.customerService = customerService;
	}
	
	@RequestMapping(value = "/queryCustomers")
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
		List<Customer> customers = customerService.selectCustomers(map);
		List<TableProperty> tablepropertys = new ArrayList<TableProperty>();
		List<TableProperty> searchFactors = new ArrayList<TableProperty>();
		tablepropertys.add(new TableProperty("id", LanguageUtil.getString("id")));
		tablepropertys.add(new TableProperty("name", LanguageUtil.getString("name")));
		tablepropertys.add(new TableProperty("address", LanguageUtil.getString("address")));
		tablepropertys.add(new TableProperty("phone", LanguageUtil.getString("phone")));
		tablepropertys.add(new TableProperty("email", LanguageUtil.getString("email")));
		tablepropertys.add(new TableProperty("credit", LanguageUtil.getString("credit")));
		tablepropertys.add(new TableProperty("note", LanguageUtil.getString("note")));
		mv.addObject("tablepropertys", tablepropertys);
		mv.addObject("tablelines", customers);
		mv.addObject("criteria", "Customer");
		mv.addObject("page", page);
		mv.addObject("size", size);
		mv.addObject("title", LanguageUtil.getString("customerTitle"));
		searchFactors.add(new TableProperty("id", id));
		searchFactors.add(new TableProperty("key", key));
		searchFactors.add(new TableProperty("page", page));
		searchFactors.add(new TableProperty("size", size));
		mv.addObject("searchFactors", searchFactors);
		if (customers.size() < size) {
			mv.addObject("lastpage", page);
		}
		mv.addObject("pagelink", "queryCustomers");
		mv.setViewName("query");
		mv.addObject(MetaData.ProcessResult, MetaData.ProcessSuccess);
		return this.outputView(session, mv);
	}
	
	@RequestMapping(value = "/queryCustomer")
	public ModelAndView queryUser(@RequestParam("id") long id, HttpServletRequest request, HttpSession session)
			throws Exception {
		ModelAndView mv = new ModelAndView();
		Customer customer = customerService.selectCustomer(id);
		List<TableProperty> tablepropertys = new ArrayList<TableProperty>();
		tablepropertys.add(new TableProperty("id", "ID"));
		tablepropertys.add(new TableProperty("name", LanguageUtil.getString("name")));
		tablepropertys.add(new TableProperty("address", LanguageUtil.getString("address")));
		tablepropertys.add(new TableProperty("phone", LanguageUtil.getString("phone")));
		tablepropertys.add(new TableProperty("email", LanguageUtil.getString("email")));
		tablepropertys.add(new TableProperty("credit", LanguageUtil.getString("credit")));
		tablepropertys.add(new TableProperty("note", LanguageUtil.getString("note")));
		mv.addObject("tablepropertys", tablepropertys);
		mv.addObject("tableline", customer);
		mv.setViewName("criteriaShow");
		mv.addObject(MetaData.ProcessResult, MetaData.ProcessSuccess);
		return this.outputView(session, mv);
	}

	@RequestMapping(value = "/editCustomer")
	public ModelAndView editUser(@RequestParam("id") long id, HttpServletRequest request, HttpSession session)
			throws Exception {
		ModelAndView mv = new ModelAndView();
		Customer customer = customerService.selectCustomer(id);
		List<TableProperty> tablepropertys = new ArrayList<TableProperty>();
		tablepropertys.add(new TableProperty("id", LanguageUtil.getString("id")));
		tablepropertys.add(new TableProperty("name", LanguageUtil.getString("name")));
		tablepropertys.add(new TableProperty("address", LanguageUtil.getString("address")));
		tablepropertys.add(new TableProperty("phone", LanguageUtil.getString("phone")));
		tablepropertys.add(new TableProperty("email", LanguageUtil.getString("email")));
		tablepropertys.add(new TableProperty("credit", LanguageUtil.getString("credit")));
		tablepropertys.add(new TableProperty("note", LanguageUtil.getString("note")));
		mv.addObject("tablepropertys", tablepropertys);
		mv.addObject("tableline", customer);
		mv.addObject("action", "editCustomerProcess");
		mv.addObject("modelAttribute", "customer");
		mv.setViewName("genEdit");
		mv.addObject(MetaData.ProcessResult, MetaData.ProcessSuccess);
		return this.outputView(session, mv);
	}

	@RequestMapping(value = "/editCustomerProcess")
	public ModelAndView editUserProcess(@ModelAttribute Customer customer, HttpServletRequest request, HttpSession session)
			throws Exception {
		ModelAndView mv = new ModelAndView();

		try {
			customerService.editCustomer(customer);
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
	
	@RequestMapping(value = "/createCustomer")
	public ModelAndView createUser(HttpServletRequest request, HttpSession session) throws Exception {
		ModelAndView mv = new ModelAndView();
		List<TableProperty> tablepropertys = new ArrayList<TableProperty>();
		tablepropertys.add(new TableProperty("name", LanguageUtil.getString("name")));
		tablepropertys.add(new TableProperty("address", LanguageUtil.getString("address")));
		tablepropertys.add(new TableProperty("phone", LanguageUtil.getString("phone")));
		tablepropertys.add(new TableProperty("email", LanguageUtil.getString("email")));
		tablepropertys.add(new TableProperty("credit", LanguageUtil.getString("credit")));
		tablepropertys.add(new TableProperty("note", LanguageUtil.getString("note")));
		
		Customer customer = new Customer();
		mv.addObject("tablepropertys", tablepropertys);
		mv.addObject("tableline", customer);
		mv.addObject("action", "createCustomerProcess");
		mv.addObject("modelAttribute", "customer");
		mv.setViewName("genCreate");

		return this.outputView(session, mv);
	}

	@RequestMapping(value = "/createCustomerProcess")
	public ModelAndView createUserProcess(@ModelAttribute Customer customer, HttpServletRequest request, HttpSession session)
			throws Exception {
		ModelAndView mv = new ModelAndView();

		try {
			customerService.createCustomer(customer);
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

	@RequestMapping(value = "/deleteCustomer")
	public ModelAndView deleteUserProcess(@RequestParam("id") String id, HttpServletRequest request, HttpSession session)
			throws Exception {
		ModelAndView mv = new ModelAndView();

		try {
			String tempid = id.split(",", 2)[0];
			customerService.deleteCustomer(Long.parseLong(tempid));
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
