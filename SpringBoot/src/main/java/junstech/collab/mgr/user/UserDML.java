package junstech.collab.mgr.user;

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
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import junstech.collab.BaseController;
import junstech.model.Criteria;
import junstech.model.Privilege;
import junstech.model.TableProperty;
import junstech.model.User;
import junstech.service.CriteriaService;
import junstech.service.PrivilegeService;
import junstech.service.UserService;
import junstech.util.AESEncryption;
import junstech.util.ENVConfig;
import junstech.util.MetaData;
import junstech.util.LanguageUtil;

@Controller
public class UserDML extends BaseController{

	public UserDML() {
	}

	UserService userService;

	CriteriaService criteriaService;

	PrivilegeService privilegeService;

	public PrivilegeService getPrivilegeService() {
		return privilegeService;
	}

	@Autowired
	public void setPrivilegeService(PrivilegeService privilegeService) {
		this.privilegeService = privilegeService;
	}

	public CriteriaService getCriteriaService() {
		return criteriaService;
	}

	@Autowired
	public void setCriteriaService(CriteriaService criteriaService) {
		this.criteriaService = criteriaService;
	}

	public UserService getUserService() {
		return userService;
	}

	@Autowired
	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	@InitBinder
	public void initBinder(WebDataBinder binder) {
	    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
	    dateFormat.setLenient(false);
	    binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));//true:���������ֵ��false:����Ϊ��ֵ
	}
	
	@RequestMapping(value = "/queryUsers")
	public ModelAndView queryUsers(@RequestParam("id") String id, @RequestParam("key") String key,
			@RequestParam("startdate") String startdate, @RequestParam("enddate") String enddate,
			@RequestParam("page") int page, @RequestParam("size") int size, HttpServletRequest request,
			HttpSession session) throws Exception {
		ModelAndView mv = new ModelAndView();
		long uid = 0;
		if(id != ""){
			uid = Long.parseLong(id);
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", uid);
		map.put("key", key);
		map.put("startdate", startdate);
		map.put("enddate", enddate);
		map.put("prev",  (page - 1) * size);
		map.put("next",  size);
		List<User> users = userService.getAllUser(map);
		List<TableProperty> tablepropertys = new ArrayList<TableProperty>();
		List<TableProperty> searchFactors = new ArrayList<TableProperty>();
		tablepropertys.add(new TableProperty("id", LanguageUtil.getString("id")));
		tablepropertys.add(new TableProperty("username", LanguageUtil.getString("username")));
		tablepropertys.add(new TableProperty("nickname", LanguageUtil.getString("nickname")));
		tablepropertys.add(new TableProperty("createTime", LanguageUtil.getString("createTime")));
		tablepropertys.add(new TableProperty("lastlogintime", LanguageUtil.getString("lastlogintime")));
		tablepropertys.add(new TableProperty("superuser", LanguageUtil.getString("superuser")));
		mv.addObject("tablepropertys", tablepropertys);
		mv.addObject("tablelines", users);
		mv.addObject("criteria", "User");
		mv.addObject("page", page);
		mv.addObject("size", size);
		mv.addObject("title", LanguageUtil.getString("userTitle"));
		searchFactors.add(new TableProperty("id", id));
		searchFactors.add(new TableProperty("key", key));
		searchFactors.add(new TableProperty("startdate", startdate));
		searchFactors.add(new TableProperty("enddate", enddate));
		searchFactors.add(new TableProperty("page", page));
		searchFactors.add(new TableProperty("size", size));
		mv.addObject("searchFactors", searchFactors);
		if (users.size() < size) {
			mv.addObject("lastpage", page);
		}
		mv.addObject("pagelink", "queryUsers");
		mv.setViewName("query");
		mv.addObject(MetaData.ProcessResult, MetaData.ProcessSuccess);
		return this.outputView(session, mv);
	}

	@RequestMapping(value = "/queryUser")
	public ModelAndView queryUser(@RequestParam("id") long id, HttpServletRequest request, HttpSession session)
			throws Exception {
		ModelAndView mv = new ModelAndView();
		User user = userService.getUserWithPrivilege(id);
		List<TableProperty> tablepropertys = new ArrayList<TableProperty>();
		tablepropertys.add(new TableProperty("id", LanguageUtil.getString("id")));
		tablepropertys.add(new TableProperty("username", LanguageUtil.getString("username")));
		tablepropertys.add(new TableProperty("nickname", LanguageUtil.getString("nickname")));
		tablepropertys.add(new TableProperty("lastlogintime", LanguageUtil.getString("lastlogintime")));
		tablepropertys.add(new TableProperty("superuser", LanguageUtil.getString("superuser")));
		
		mv.addObject("tablepropertys", tablepropertys);
		mv.addObject("tableline", user);
		mv.addObject(MetaData.ProcessResult, MetaData.ProcessSuccess);
		mv.setViewName("criteriaShow");

		return this.outputView(session, mv);
	}

	@RequestMapping(value = "/editUser")
	public ModelAndView editUser(@RequestParam("id") long id, HttpServletRequest request, HttpSession session)
			throws Exception {
		ModelAndView mv = new ModelAndView();
		User user = userService.getUserWithPrivilege(id);
		List<Privilege> currentPrivileges = user.getPrivileges();
		List<Privilege> tmpPrivileges = new ArrayList<Privilege>();
		for (Criteria criteria : criteriaService.getAllCriteria()) {
			tmpPrivileges.add(new Privilege(criteria.getId(), criteria));
		}
		for (Privilege curPrivilege : currentPrivileges) {
			for (int i = 0; i < tmpPrivileges.size(); i++) {
				tmpPrivileges.get(i).setId(user.getId());
				if (tmpPrivileges.get(i).getProgramid() == curPrivilege.getProgramid()) {
					tmpPrivileges.get(i).setKey(curPrivilege.getKey());
					tmpPrivileges.get(i).setPrivilege(curPrivilege.getPrivilege());
				}
			}
		}
		user.setPrivileges(tmpPrivileges);
		user.setPassword(AESEncryption.decrypt(user.getPassword(), ENVConfig.encryptKey));
		List<TableProperty> tablepropertys = new ArrayList<TableProperty>();
		tablepropertys.add(new TableProperty("id", LanguageUtil.getString("id")));
		tablepropertys.add(new TableProperty("username", LanguageUtil.getString("username")));
		tablepropertys.add(new TableProperty("password", LanguageUtil.getString("password")));
		tablepropertys.add(new TableProperty("nickname", LanguageUtil.getString("nickname")));
		tablepropertys.add(new TableProperty("phone", LanguageUtil.getString("phone")));
		tablepropertys.add(new TableProperty("email", LanguageUtil.getString("email")));
		tablepropertys.add(new TableProperty("superuser", LanguageUtil.getString("superuser")));
		mv.addObject("tablepropertys", tablepropertys);
		mv.addObject("tableline", user);
		mv.addObject("action", "editUserProcess");
		mv.addObject("modelAttribute", "user");
		List<TableProperty> subtablepropertys = new ArrayList<TableProperty>();
		subtablepropertys.add(new TableProperty("programname", LanguageUtil.getString("programname")));
		subtablepropertys.add(new TableProperty("privilege", LanguageUtil.getString("privilege")));
		mv.addObject("subtablepropertys", subtablepropertys);
		mv.setViewName("userEdit");
		mv.addObject(MetaData.ProcessResult, MetaData.ProcessSuccess);
		return this.outputView(session, mv);
	}

	@RequestMapping(value = "/editUserProcess")
	public ModelAndView editUserProcess(@ModelAttribute User user, HttpServletRequest request, HttpSession session)
			throws Exception {
		ModelAndView mv = new ModelAndView();

		try {
			userService.updateUser(user);
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

	@RequestMapping(value = "/createUser")
	public ModelAndView createUser(HttpServletRequest request, HttpSession session) throws Exception {
		ModelAndView mv = new ModelAndView();
		List<TableProperty> tablepropertys = new ArrayList<TableProperty>();
		tablepropertys.add(new TableProperty("username", LanguageUtil.getString("username")));
		tablepropertys.add(new TableProperty("password", LanguageUtil.getString("password")));
		tablepropertys.add(new TableProperty("nickname", LanguageUtil.getString("nickname")));
		tablepropertys.add(new TableProperty("phone", LanguageUtil.getString("phone")));
		tablepropertys.add(new TableProperty("superuser", LanguageUtil.getString("superuser")));

		List<Privilege> privileges = new ArrayList<Privilege>();
		List<Criteria> criterias = criteriaService.getAllCriteria();
		for (Criteria criteria : criterias) {
			privileges.add(new Privilege(criteria.getId(), criteria));
		}
		User user = new User();
		user.setPrivileges(privileges);
		mv.addObject("tablepropertys", tablepropertys);
		mv.addObject("tableline", user);
		mv.addObject("action", "createUserProcess");
		mv.addObject("modelAttribute", "user");
		mv.setViewName("userCreate");
		mv.addObject(MetaData.ProcessResult, MetaData.ProcessSuccess);
		return this.outputView(session, mv);
	}

	@RequestMapping(value = "/createUserProcess")
	public ModelAndView createUserProcess(@ModelAttribute User user, HttpServletRequest request, HttpSession session)
			throws Exception {
		ModelAndView mv = new ModelAndView();

		try {
			userService.createUser(user);
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

	@RequestMapping(value = "/deleteUser")
	public ModelAndView deleteUserProcess(@RequestParam("id") long id, HttpServletRequest request, HttpSession session)
			throws Exception {
		ModelAndView mv = new ModelAndView();

		try {
			userService.deleteUser(id);
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
