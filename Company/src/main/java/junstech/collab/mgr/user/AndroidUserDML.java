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
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.databind.ObjectMapper;

import junstech.model.Criteria;
import junstech.model.Privilege;
import junstech.model.TableProperty;
import junstech.model.User;
import junstech.service.CriteriaService;
import junstech.service.PrivilegeService;
import junstech.service.UserService;
import junstech.util.AESEncryption;
import junstech.util.ENVConfig;
import junstech.util.JacksonUtil;
import junstech.util.MetaData;

@Controller
public class AndroidUserDML {

	public AndroidUserDML() {
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
	    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	    dateFormat.setLenient(false);
	    binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));//true:允许输入空值，false:不能为空值
	}
	
	@ResponseBody
	@RequestMapping(value = "/androidQueryUsers")
	public String queryUsers(@RequestParam("id") String id, @RequestParam("username") String username,
			@RequestParam("startdate") String startdate, @RequestParam("enddate") String enddate,
			@RequestParam("page") int page, @RequestParam("size") int size, HttpServletRequest request,
			HttpSession session) throws Exception {
		Map<String, Object> returnInfo = new HashMap<String, Object>();  
		String hasPrivilege = (String) request.getAttribute("privilegeFail");
		if(!hasPrivilege.equals("pass")){
			returnInfo.put("reason", (String) request.getAttribute("privilegeFail"));
			returnInfo.put(MetaData.ProcessResult, MetaData.ProcessFail);
			return JacksonUtil.obj2json(returnInfo);
		}
		long uid = 0;
		if(id != ""){
			uid = Long.parseLong(id);
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", uid);
		map.put("username", username);
		map.put("startdate", startdate);
		map.put("enddate", enddate);
		map.put("prev",  (page - 1) * size);
		map.put("next",  size);
		List<User> users = userService.getAllUser(map);
		List<TableProperty> tablepropertys = new ArrayList<TableProperty>();
		tablepropertys.add(new TableProperty("id", "ID"));
		tablepropertys.add(new TableProperty("username", "用户名"));
		tablepropertys.add(new TableProperty("nickname", "昵称"));
		tablepropertys.add(new TableProperty("createTime", "创建时间"));
		tablepropertys.add(new TableProperty("lastlogintime", "上次登录时间"));
		tablepropertys.add(new TableProperty("superuser", "用户类型"));

		returnInfo.put("tablepropertys", tablepropertys);
		returnInfo.put("searchFactors", map);
		returnInfo.put("users", users);
		returnInfo.put(MetaData.ProcessResult, MetaData.ProcessSuccess);
		return JacksonUtil.obj2json(returnInfo);

	}

	@RequestMapping(value = "/androidQueryUser", method = RequestMethod.GET)
	public ModelAndView queryUser(@RequestParam("id") long id, HttpServletRequest request, HttpSession session)
			throws Exception {
		ModelAndView mv = new ModelAndView();
		User user = userService.getUser(id);
		List<TableProperty> tablepropertys = new ArrayList<TableProperty>();
		tablepropertys.add(new TableProperty("id", "ID"));
		tablepropertys.add(new TableProperty("username", "用户名"));
		tablepropertys.add(new TableProperty("nickname", "昵称"));
		tablepropertys.add(new TableProperty("lastlogintime", "上次登录时间"));
		tablepropertys.add(new TableProperty("superuser", "是否管理员"));
		mv.addObject("tablepropertys", tablepropertys);
		mv.addObject("tableline", user);
		mv.setViewName("criteriaShow");

		return mv;
	}

	@RequestMapping(value = "/androidEditUser", method = RequestMethod.GET)
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
		tablepropertys.add(new TableProperty("id", "ID"));
		tablepropertys.add(new TableProperty("username", "用户名"));
		tablepropertys.add(new TableProperty("password", "密码"));
		tablepropertys.add(new TableProperty("nickname", "昵称"));
		tablepropertys.add(new TableProperty("phone", "电话"));
		tablepropertys.add(new TableProperty("email", "E-mail"));
		tablepropertys.add(new TableProperty("superuser", "是否管理员"));
		mv.addObject("tablepropertys", tablepropertys);
		mv.addObject("tableline", user);
		mv.addObject("action", "editUserProcess");
		mv.addObject("modelAttribute", "user");
		List<TableProperty> subtablepropertys = new ArrayList<TableProperty>();
		subtablepropertys.add(new TableProperty("programname", "模块"));
		subtablepropertys.add(new TableProperty("privilege", "权限"));
		mv.addObject("subtablepropertys", subtablepropertys);
		mv.setViewName("userEdit");

		return mv;
	}

	@RequestMapping(value = "/androidEditUserProcess", method = RequestMethod.POST)
	public ModelAndView editUserProcess(@ModelAttribute User user, HttpServletRequest request, HttpSession session)
			throws Exception {
		ModelAndView mv = new ModelAndView();

		try {
			userService.updateUser(user);
			mv.addObject("message", "更新用户成功");
			mv.addObject(MetaData.setNoteType, MetaData.cosmoSuccess);
		} catch (Exception e) {
			mv.addObject("message", "更新失败，请重试!");
			mv.addObject(MetaData.setNoteType, MetaData.cosmoDanger);
		}

		mv.addObject(MetaData.setNoteTitle, "结果");
		mv.addObject(MetaData.completeReturnPage, "redirect.htm?view=content");
		mv.addObject(MetaData.setTargetFrame, MetaData.setTargetAsContentFrame);
		mv.setViewName("complete");
		return mv;
	}

	@RequestMapping(value = "/androidCreateUser")
	public ModelAndView createUser(HttpServletRequest request, HttpSession session) throws Exception {
		ModelAndView mv = new ModelAndView();
		List<TableProperty> tablepropertys = new ArrayList<TableProperty>();
		tablepropertys.add(new TableProperty("username", "用户名"));
		tablepropertys.add(new TableProperty("password", "密码"));
		tablepropertys.add(new TableProperty("nickname", "昵称"));
		tablepropertys.add(new TableProperty("phone", "电话"));
		tablepropertys.add(new TableProperty("superuser", "是否管理员"));
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

		return mv;
	}

	@RequestMapping(value = "/androidCreateUserProcess", method = RequestMethod.POST)
	public ModelAndView createUserProcess(@ModelAttribute User user, HttpServletRequest request, HttpSession session)
			throws Exception {
		ModelAndView mv = new ModelAndView();

		try {
			userService.createUser(user);
			mv.addObject("message", "新建用户成功");
			mv.addObject(MetaData.setNoteType, MetaData.cosmoSuccess);
		} catch (Exception e) {
			mv.addObject("message", "创建失败，请重新操作!");
			mv.addObject(MetaData.setNoteType, MetaData.cosmoDanger);
		}

		mv.addObject(MetaData.setNoteTitle, "结果");
		mv.addObject(MetaData.completeReturnPage, "redirect.htm?view=content");
		mv.addObject(MetaData.setTargetFrame, MetaData.setTargetAsContentFrame);
		mv.setViewName("complete");
		return mv;
	}

	@RequestMapping(value = "/androidDeleteUser", method = RequestMethod.GET)
	public ModelAndView deleteUserProcess(@RequestParam("id") long id, HttpServletRequest request, HttpSession session)
			throws Exception {
		ModelAndView mv = new ModelAndView();

		try {
			userService.deleteUser(id);
			mv.addObject("message", "删除用户成功");
			mv.addObject(MetaData.setNoteType, MetaData.cosmoSuccess);
		} catch (Exception e) {
			mv.addObject("message", "删除失败，请重新操作!");
			mv.addObject(MetaData.setNoteType, MetaData.cosmoDanger);
		}

		mv.addObject(MetaData.setNoteTitle, "结果");
		mv.addObject(MetaData.completeReturnPage, "redirect.htm?view=content");
		mv.addObject(MetaData.setTargetFrame, MetaData.setTargetAsContentFrame);
		mv.setViewName("complete");
		return mv;
	}
}
