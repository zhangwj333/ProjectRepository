package junstech.collab.mgr.purchase;

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
import junstech.model.Supplier;
import junstech.model.TableProperty;
import junstech.model.User;
import junstech.service.CriteriaService;
import junstech.service.GoodService;
import junstech.service.PrivilegeService;
import junstech.service.SupplierService;
import junstech.service.UserService;
import junstech.util.AESEncryption;
import junstech.util.ENVConfig;
import junstech.util.MetaData;
import junstech.util.LanguageUtil;

@Controller
public class SupplierDML extends BaseController{

	public SupplierDML() {
	}
	
	SupplierService supplierService;

	public SupplierService getSupplierService() {
		return supplierService;
	}

	@Autowired
	public void setSupplierService(SupplierService supplierService) {
		this.supplierService = supplierService;
	}
	
	@RequestMapping(value = "/querySuppliers")
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
		List<Supplier> suppliers = supplierService.selectSuppliers(map);
		List<TableProperty> tablepropertys = new ArrayList<TableProperty>();
		List<TableProperty> searchFactors = new ArrayList<TableProperty>();
		tablepropertys.add(new TableProperty("id", LanguageUtil.getString("id")));
		tablepropertys.add(new TableProperty("supplier", LanguageUtil.getString("supplier")));
		tablepropertys.add(new TableProperty("phone", LanguageUtil.getString("phone")));
		tablepropertys.add(new TableProperty("address", LanguageUtil.getString("address")));
		tablepropertys.add(new TableProperty("note", LanguageUtil.getString("note")));
		mv.addObject("tablepropertys", tablepropertys);
		mv.addObject("tablelines", suppliers);
		mv.addObject("criteria", "Supplier");
		mv.addObject("page", page);
		mv.addObject("size", size);
		mv.addObject("title", LanguageUtil.getString("supplierTitle"));
		searchFactors.add(new TableProperty("id", id));
		searchFactors.add(new TableProperty("key", key));
		searchFactors.add(new TableProperty("page", page));
		searchFactors.add(new TableProperty("size", size));
		mv.addObject("searchFactors", searchFactors);
		if (suppliers.size() < size) {
			mv.addObject("lastpage", page);
		}
		mv.addObject("pagelink", "querySuppliers");
		mv.setViewName("query");
		mv.addObject(MetaData.ProcessResult, MetaData.ProcessSuccess);
		return this.outputView(session, mv);
	}
	
	@RequestMapping(value = "/querySupplier")
	public ModelAndView queryUser(@RequestParam("id") int id, HttpServletRequest request, HttpSession session)
			throws Exception {
		ModelAndView mv = new ModelAndView();
		Supplier supplier = supplierService.selectSupplier(id);
		List<TableProperty> tablepropertys = new ArrayList<TableProperty>();
		tablepropertys.add(new TableProperty("id", LanguageUtil.getString("id")));
		tablepropertys.add(new TableProperty("supplier", LanguageUtil.getString("supplier")));
		tablepropertys.add(new TableProperty("phone", LanguageUtil.getString("phone")));
		tablepropertys.add(new TableProperty("address", LanguageUtil.getString("address")));
		tablepropertys.add(new TableProperty("note", LanguageUtil.getString("note")));
		mv.addObject("tablepropertys", tablepropertys);
		mv.addObject("tableline", supplier);
		mv.setViewName("criteriaShow");
		mv.addObject(MetaData.ProcessResult, MetaData.ProcessSuccess);
		return this.outputView(session, mv);
	}

	@RequestMapping(value = "/editSupplier")
	public ModelAndView editUser(@RequestParam("id") int id, HttpServletRequest request, HttpSession session)
			throws Exception {
		ModelAndView mv = new ModelAndView();
		Supplier supplier = supplierService.selectSupplier(id);
		List<TableProperty> tablepropertys = new ArrayList<TableProperty>();
		tablepropertys.add(new TableProperty("id", LanguageUtil.getString("id")));
		tablepropertys.add(new TableProperty("supplier", LanguageUtil.getString("supplier")));
		tablepropertys.add(new TableProperty("phone", LanguageUtil.getString("phone")));
		tablepropertys.add(new TableProperty("address", LanguageUtil.getString("address")));
		tablepropertys.add(new TableProperty("note", LanguageUtil.getString("note")));
		mv.addObject("tablepropertys", tablepropertys);
		mv.addObject("tableline", supplier);
		mv.addObject("action", "editSupplierProcess");
		mv.addObject("modelAttribute", "supplier");
		mv.setViewName("genEdit");
		mv.addObject(MetaData.ProcessResult, MetaData.ProcessSuccess);
		return this.outputView(session, mv);
	}

	@RequestMapping(value = "/editSupplierProcess")
	public ModelAndView editUserProcess(@ModelAttribute Supplier supplier, HttpServletRequest request, HttpSession session)
			throws Exception {
		ModelAndView mv = new ModelAndView();

		try {
			supplierService.editSupplier(supplier);
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
	
	@RequestMapping(value = "/createSupplier")
	public ModelAndView createUser(HttpServletRequest request, HttpSession session) throws Exception {
		ModelAndView mv = new ModelAndView();
		List<TableProperty> tablepropertys = new ArrayList<TableProperty>();
		tablepropertys.add(new TableProperty("supplier", LanguageUtil.getString("supplier")));
		tablepropertys.add(new TableProperty("phone", LanguageUtil.getString("phone")));
		tablepropertys.add(new TableProperty("address", LanguageUtil.getString("address")));
		tablepropertys.add(new TableProperty("note", LanguageUtil.getString("note")));
		
		Supplier supplier = new Supplier();
		mv.addObject("tablepropertys", tablepropertys);
		mv.addObject("tableline", supplier);
		mv.addObject("action", "createSupplierProcess");
		mv.addObject("modelAttribute", "supplier");
		mv.setViewName("genCreate");
		mv.addObject(MetaData.ProcessResult, MetaData.ProcessSuccess);
		return this.outputView(session, mv);
	}

	@RequestMapping(value = "/createSupplierProcess")
	public ModelAndView createUserProcess(@ModelAttribute Supplier supplier, HttpServletRequest request, HttpSession session)
			throws Exception {
		ModelAndView mv = new ModelAndView();

		try {
			supplierService.createSupplier(supplier);
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

	@RequestMapping(value = "/deleteSupplier")
	public ModelAndView deleteUserProcess(@RequestParam("id") String id, HttpServletRequest request, HttpSession session)
			throws Exception {
		ModelAndView mv = new ModelAndView();

		try {
			String tempid = id.split(",", 2)[0];
			supplierService.deleteSupplier(Integer.parseInt(tempid));
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
