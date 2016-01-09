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
		tablepropertys.add(new TableProperty("id", "ID"));
		tablepropertys.add(new TableProperty("supplier", "供应商"));
		tablepropertys.add(new TableProperty("phone", "联系电话"));
		tablepropertys.add(new TableProperty("address", "地址"));
		tablepropertys.add(new TableProperty("note", "备注"));
		mv.addObject("tablepropertys", tablepropertys);
		mv.addObject("tablelines", suppliers);
		mv.addObject("criteria", "Supplier");
		mv.addObject("page", page);
		mv.addObject("size", size);
		mv.addObject("title", "供应商");
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
	
	@RequestMapping(value = "/querySupplier", method = RequestMethod.GET)
	public ModelAndView queryUser(@RequestParam("id") int id, HttpServletRequest request, HttpSession session)
			throws Exception {
		ModelAndView mv = new ModelAndView();
		Supplier supplier = supplierService.selectSupplier(id);
		List<TableProperty> tablepropertys = new ArrayList<TableProperty>();
		tablepropertys.add(new TableProperty("id", "ID"));
		tablepropertys.add(new TableProperty("supplier", "供应商"));
		tablepropertys.add(new TableProperty("phone", "联系电话"));
		tablepropertys.add(new TableProperty("address", "地址"));
		tablepropertys.add(new TableProperty("note", "备注"));
		mv.addObject("tablepropertys", tablepropertys);
		mv.addObject("tableline", supplier);
		mv.setViewName("criteriaShow");
		mv.addObject(MetaData.ProcessResult, MetaData.ProcessSuccess);
		return this.outputView(session, mv);
	}

	@RequestMapping(value = "/editSupplier", method = RequestMethod.GET)
	public ModelAndView editUser(@RequestParam("id") int id, HttpServletRequest request, HttpSession session)
			throws Exception {
		ModelAndView mv = new ModelAndView();
		Supplier supplier = supplierService.selectSupplier(id);
		List<TableProperty> tablepropertys = new ArrayList<TableProperty>();
		tablepropertys.add(new TableProperty("id", "ID"));
		tablepropertys.add(new TableProperty("supplier", "供应商"));
		tablepropertys.add(new TableProperty("phone", "联系电话"));
		tablepropertys.add(new TableProperty("address", "地址"));
		tablepropertys.add(new TableProperty("note", "备注"));
		mv.addObject("tablepropertys", tablepropertys);
		mv.addObject("tableline", supplier);
		mv.addObject("action", "editSupplierProcess");
		mv.addObject("modelAttribute", "supplier");
		mv.setViewName("genEdit");
		mv.addObject(MetaData.ProcessResult, MetaData.ProcessSuccess);
		return this.outputView(session, mv);
	}

	@RequestMapping(value = "/editSupplierProcess", method = RequestMethod.POST)
	public ModelAndView editUserProcess(@ModelAttribute Supplier supplier, HttpServletRequest request, HttpSession session)
			throws Exception {
		ModelAndView mv = new ModelAndView();

		try {
			supplierService.editSupplier(supplier);
			mv.addObject("message", "更新供应商成功");
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
	
	@RequestMapping(value = "/createSupplier")
	public ModelAndView createUser(HttpServletRequest request, HttpSession session) throws Exception {
		ModelAndView mv = new ModelAndView();
		List<TableProperty> tablepropertys = new ArrayList<TableProperty>();
		tablepropertys.add(new TableProperty("supplier", "供应商"));
		tablepropertys.add(new TableProperty("phone", "联系电话"));
		tablepropertys.add(new TableProperty("address", "地址"));
		tablepropertys.add(new TableProperty("note", "备注"));
		
		Supplier supplier = new Supplier();
		mv.addObject("tablepropertys", tablepropertys);
		mv.addObject("tableline", supplier);
		mv.addObject("action", "createSupplierProcess");
		mv.addObject("modelAttribute", "supplier");
		mv.setViewName("genCreate");
		mv.addObject(MetaData.ProcessResult, MetaData.ProcessSuccess);
		return this.outputView(session, mv);
	}

	@RequestMapping(value = "/createSupplierProcess", method = RequestMethod.POST)
	public ModelAndView createUserProcess(@ModelAttribute Supplier supplier, HttpServletRequest request, HttpSession session)
			throws Exception {
		ModelAndView mv = new ModelAndView();

		try {
			supplierService.createSupplier(supplier);
			mv.addObject("message", "新建供应商成功");
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

	@RequestMapping(value = "/deleteSupplier", method = RequestMethod.GET)
	public ModelAndView deleteUserProcess(@RequestParam("id") int id, HttpServletRequest request, HttpSession session)
			throws Exception {
		ModelAndView mv = new ModelAndView();

		try {
			supplierService.deleteSupplier(id);
			mv.addObject("message", "删除供应商成功");
			mv.addObject(MetaData.setNoteType, MetaData.cosmoSuccess);
			mv.addObject(MetaData.ProcessResult, MetaData.ProcessSuccess);
		} catch (Exception e) {
			mv.addObject("message", "删除失败，请重新操作!");
			mv.addObject(MetaData.setNoteType, MetaData.cosmoDanger);
			mv.addObject(MetaData.ProcessResult, MetaData.ProcessFail);
		}

		mv.addObject(MetaData.setNoteTitle, "结果");
		mv.addObject(MetaData.completeReturnPage, "redirect.htm?view=content");
		mv.addObject(MetaData.setTargetFrame, MetaData.setTargetAsContentFrame);
		mv.setViewName("complete");
		return this.outputView(session, mv);
	}
}
