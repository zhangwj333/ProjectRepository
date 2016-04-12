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
import junstech.model.Financetype;
import junstech.model.TableProperty;
import junstech.model.User;
import junstech.service.CriteriaService;
import junstech.service.GoodService;
import junstech.service.PrivilegeService;
import junstech.service.FinancetypeService;
import junstech.service.UserService;
import junstech.util.AESEncryption;
import junstech.util.ENVConfig;
import junstech.util.MetaData;
import junstech.util.RedisUtil;

@Controller
public class FinancetypeDML extends BaseController{

	public FinancetypeDML() {
	}
	
	FinancetypeService financetypeService;

	public FinancetypeService getFinancetypeService() {
		return financetypeService;
	}

	@Autowired
	public void setFinancetypeService(FinancetypeService financetypeService) {
		this.financetypeService = financetypeService;
	}
	
	@RequestMapping(value = "/queryFinancetypes")
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
		List<Financetype> financetypes = financetypeService.selectFinancetypes(map);
		List<TableProperty> tablepropertys = new ArrayList<TableProperty>();
		List<TableProperty> searchFactors = new ArrayList<TableProperty>();
		tablepropertys.add(new TableProperty("id", RedisUtil.getString("id")));
		tablepropertys.add(new TableProperty("name", RedisUtil.getString("financeTypeName")));
		mv.addObject("tablepropertys", tablepropertys);
		mv.addObject("tablelines", financetypes);
		mv.addObject("criteria", "Financetype");
		mv.addObject("page", page);
		mv.addObject("size", size);
		mv.addObject("title", RedisUtil.getString("financeTypeTitle"));
		searchFactors.add(new TableProperty("id", id));
		searchFactors.add(new TableProperty("key", key));
		searchFactors.add(new TableProperty("page", page));
		searchFactors.add(new TableProperty("size", size));
		mv.addObject("searchFactors", searchFactors);
		if (financetypes.size() < size) {
			mv.addObject("lastpage", page);
		}
		mv.addObject("pagelink", "queryFinancetypes");
		mv.setViewName("query");
		mv.addObject(MetaData.ProcessResult, MetaData.ProcessSuccess);
		return this.outputView(session, mv);
	}
	
	@RequestMapping(value = "/queryFinancetype", method = RequestMethod.GET)
	public ModelAndView query(@RequestParam("id") int id, HttpServletRequest request, HttpSession session)
			throws Exception {
		ModelAndView mv = new ModelAndView();
		Financetype financetype = financetypeService.selectFinancetype(id);
		List<TableProperty> tablepropertys = new ArrayList<TableProperty>();
		tablepropertys.add(new TableProperty("id", RedisUtil.getString("id")));
		tablepropertys.add(new TableProperty("name", RedisUtil.getString("financeTypeName")));
		mv.addObject("tablepropertys", tablepropertys);
		mv.addObject("tableline", financetype);
		mv.setViewName("criteriaShow");
		mv.addObject(MetaData.ProcessResult, MetaData.ProcessSuccess);
		return this.outputView(session, mv);
	}

	@RequestMapping(value = "/editFinancetype", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam("id") int id, HttpServletRequest request, HttpSession session)
			throws Exception {
		ModelAndView mv = new ModelAndView();
		Financetype financetype = financetypeService.selectFinancetype(id);
		List<TableProperty> tablepropertys = new ArrayList<TableProperty>();
		tablepropertys.add(new TableProperty("id", RedisUtil.getString("id")));
		tablepropertys.add(new TableProperty("name", RedisUtil.getString("financeTypeName")));
		mv.addObject("tablepropertys", tablepropertys);
		mv.addObject("tableline", financetype);
		mv.addObject("action", "editFinancetypeProcess");
		mv.addObject("modelAttribute", "financetype");
		mv.setViewName("genEdit");
		mv.addObject(MetaData.ProcessResult, MetaData.ProcessSuccess);
		return this.outputView(session, mv);
	}

	@RequestMapping(value = "/editFinancetypeProcess", method = RequestMethod.POST)
	public ModelAndView editProcess(@ModelAttribute Financetype financetype, HttpServletRequest request, HttpSession session)
			throws Exception {
		ModelAndView mv = new ModelAndView();

		try {
			financetypeService.editFinancetype(financetype);
			mv.addObject("message", RedisUtil.getString("updateSuccess"));
			mv.addObject(MetaData.setNoteType, MetaData.cosmoSuccess);
			mv.addObject(MetaData.ProcessResult, MetaData.ProcessSuccess);
		} catch (Exception e) {
			mv.addObject("message", RedisUtil.getString("updateFail"));
			mv.addObject(MetaData.setNoteType, MetaData.cosmoDanger);
			mv.addObject(MetaData.ProcessResult, MetaData.ProcessFail);
		}

		mv.addObject(MetaData.setNoteTitle, RedisUtil.getString("title"));
		mv.addObject(MetaData.completeReturnPage, "redirect.htm?view=content");
		mv.addObject(MetaData.setTargetFrame, MetaData.setTargetAsContentFrame);
		mv.setViewName("complete");
		return this.outputView(session, mv);
	}
	
	@RequestMapping(value = "/createFinancetype")
	public ModelAndView create(HttpServletRequest request, HttpSession session) throws Exception {
		ModelAndView mv = new ModelAndView();
		List<TableProperty> tablepropertys = new ArrayList<TableProperty>();
		tablepropertys.add(new TableProperty("name", RedisUtil.getString("financeTypeName")));
		
		Financetype financetype = new Financetype();
		mv.addObject("tablepropertys", tablepropertys);
		mv.addObject("tableline", financetype);
		mv.addObject("action", "createFinancetypeProcess");
		mv.addObject("modelAttribute", "financetype");
		mv.setViewName("genCreate");
		mv.addObject(MetaData.ProcessResult, MetaData.ProcessSuccess);
		return this.outputView(session, mv);
	}

	@RequestMapping(value = "/createFinancetypeProcess", method = RequestMethod.POST)
	public ModelAndView createProcess(@ModelAttribute Financetype financetype, HttpServletRequest request, HttpSession session)
			throws Exception {
		ModelAndView mv = new ModelAndView();

		try {
			financetypeService.createFinancetype(financetype);
			mv.addObject("message", RedisUtil.getString("createSuccess"));
			mv.addObject(MetaData.setNoteType, MetaData.cosmoSuccess);
			mv.addObject(MetaData.ProcessResult, MetaData.ProcessSuccess);
		} catch (Exception e) {
			mv.addObject("message", RedisUtil.getString("createFail"));
			mv.addObject(MetaData.setNoteType, MetaData.cosmoDanger);
			mv.addObject(MetaData.ProcessResult, MetaData.ProcessFail);
		}

		mv.addObject(MetaData.setNoteTitle, RedisUtil.getString("title"));
		mv.addObject(MetaData.completeReturnPage, "redirect.htm?view=content");
		mv.addObject(MetaData.setTargetFrame, MetaData.setTargetAsContentFrame);
		mv.setViewName("complete");
		return this.outputView(session, mv);
	}

	@RequestMapping(value = "/deleteFinancetype")
	public ModelAndView deleteProcess(@RequestParam("id") String id, HttpServletRequest request, HttpSession session)
			throws Exception {
		ModelAndView mv = new ModelAndView();

		try {
			String tempid = id.split(",", 2)[0];
			financetypeService.deleteFinancetype(Integer.valueOf(tempid));
			mv.addObject("message", RedisUtil.getString("deleteSuccess"));
			mv.addObject(MetaData.setNoteType, MetaData.cosmoSuccess);
			mv.addObject(MetaData.ProcessResult, MetaData.ProcessSuccess);
		} catch (Exception e) {
			mv.addObject("message", RedisUtil.getString("deleteFail"));
			mv.addObject(MetaData.setNoteType, MetaData.cosmoDanger);
			mv.addObject(MetaData.ProcessResult, MetaData.ProcessFail);
		}

		mv.addObject(MetaData.setNoteTitle, RedisUtil.getString("title"));
		mv.addObject(MetaData.completeReturnPage, "redirect.htm?view=content");
		mv.addObject(MetaData.setTargetFrame, MetaData.setTargetAsContentFrame);
		mv.setViewName("complete");
		return this.outputView(session, mv);
	}
}
