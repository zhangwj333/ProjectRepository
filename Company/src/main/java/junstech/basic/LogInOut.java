package junstech.basic;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import junstech.collab.BaseController;
import junstech.model.User;
import junstech.service.UserService;
import junstech.service.imp.UserServiceImp;
import junstech.util.AESEncryption;
import junstech.util.ENVConfig;
import junstech.util.JacksonUtil;
import junstech.util.MetaData;
import junstech.util.RedisUtil;

@Controller
public class LogInOut extends BaseController{

	UserService userService;

	public UserService getUserService() {
		return userService;
	}

	@Autowired
	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	
	@RequestMapping(value = "/androidUserLogin")
	public ModelAndView androidUserLogin(@ModelAttribute User user,HttpServletRequest request, HttpSession session) throws Exception {
		ModelAndView mv = loginUser(user, request, session);
		session.setAttribute("logintype", "android");
		return this.outputView(session, mv);
	}
	
	@RequestMapping(value = "/userLogin", method = RequestMethod.POST)
	public ModelAndView loginUser(@ModelAttribute User user,HttpServletRequest request, HttpSession session) throws Exception {
		ModelAndView mv = new ModelAndView();
		
		User dbuser = userService.getUserWithPrivilege(user.getUsername());
		if (dbuser != null) {
			if (AESEncryption.decrypt(dbuser.getPassword(), ENVConfig.encryptKey).equals(user.getPassword())) {
				dbuser.setLastlogintime(new Date());
				dbuser.setPassword(user.getPassword());
				userService.updateUser(dbuser);
				session.setAttribute("user", dbuser);
				session.setAttribute("logintype", "web");
				if(JudgeIsMoblie(request)){
					session.setAttribute("screen", "sm");
				}
				else{
					session.setAttribute("screen", "md");
				}
				mv.addObject("user", dbuser);
				mv.addObject("theme", "default");
				mv.addObject("sessionid", session.getId());
				mv.addObject(MetaData.ProcessResult, MetaData.ProcessSuccess);
				mv.setViewName("main");
				return mv;
			}else{
				mv.addObject(MetaData.ProcessResult, MetaData.ProcessFail);
			}
		}
		mv.addObject("message", RedisUtil.getString("login"));
		mv.setViewName("login");
		return mv;
	}
	
	@RequestMapping(value = "/userLogout")
	public ModelAndView logoutUser(HttpServletRequest request, HttpSession session) throws Exception {
		ModelAndView mv = new ModelAndView();		
		session.invalidate();
		mv.addObject("message", RedisUtil.getString("logout"));
		mv.setViewName("login");
		return mv;
	}
	
	@RequestMapping(value = "/userTheme", method = RequestMethod.GET)
	public ModelAndView userTheme(@RequestParam("theme") String theme,HttpServletRequest request, HttpSession session) throws Exception {
		ModelAndView mv = new ModelAndView();
		mv.addObject("theme", theme);
		mv.setViewName("main");

		return mv;
	}
	
	@RequestMapping(value = "/redirect", method = RequestMethod.GET)
	public ModelAndView redirect(@RequestParam("view") String view,HttpServletRequest request, HttpSession session) throws Exception {
		ModelAndView mv = new ModelAndView();
		mv.setViewName(view);

		return mv;
	}
		
	public boolean JudgeIsMoblie(HttpServletRequest request) {
		boolean isMoblie = false;
		String[] mobileAgents = { "iphone", "android", "phone", "mobile", "wap", "netfront", "java", "opera mobi",
				"opera mini", "ucweb", "windows ce", "symbian", "series", "webos", "sony", "blackberry", "dopod",
				"nokia", "samsung", "palmsource", "xda", "pieplus", "meizu", "midp", "cldc", "motorola", "foma",
				"docomo", "up.browser", "up.link", "blazer", "helio", "hosin", "huawei", "novarra", "coolpad", "webos",
				"techfaith", "palmsource", "alcatel", "amoi", "ktouch", "nexian", "ericsson", "philips", "sagem",
				"wellcom", "bunjalloo", "maui", "smartphone", "iemobile", "spice", "bird", "zte-", "longcos",
				"pantech", "gionee", "portalmmm", "jig browser", "hiptop", "benq", "haier", "^lct", "320x320",
				"240x320", "176x220", "w3c ", "acs-", "alav", "alca", "amoi", "audi", "avan", "benq", "bird", "blac",
				"blaz", "brew", "cell", "cldc", "cmd-", "dang", "doco", "eric", "hipt", "inno", "ipaq", "java", "jigs",
				"kddi", "keji", "leno", "lg-c", "lg-d", "lg-g", "lge-", "maui", "maxo", "midp", "mits", "mmef", "mobi",
				"mot-", "moto", "mwbp", "nec-", "newt", "noki", "oper", "palm", "pana", "pant", "phil", "play", "port",
				"prox", "qwap", "sage", "sams", "sany", "sch-", "sec-", "send", "seri", "sgh-", "shar", "sie-", "siem",
				"smal", "smar", "sony", "sph-", "symb", "t-mo", "teli", "tim-", "tosh", "tsm-", "upg1", "upsi", "vk-v",
				"voda", "wap-", "wapa", "wapi", "wapp", "wapr", "webc", "winw", "winw", "xda", "xda-",
				"Googlebot-Mobile" };
		if (request.getHeader("User-Agent") != null) {
			for (String mobileAgent : mobileAgents) {
				if (request.getHeader("User-Agent").toLowerCase().indexOf(mobileAgent) >= 0) {
					return true;
				}
			}
		}
		return isMoblie;
	}
	
}