package junstech.collab;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import com.google.gson.Gson;

import junstech.exception.BusinessException;
import junstech.util.JacksonUtil;

@Controller
public class BaseController {

	public ModelAndView outputView(HttpSession session, ModelAndView mv) throws Exception {
		String logintype = (String) session.getAttribute("logintype");
		if (logintype == null || logintype.equals("web")) {
			return mv;
		} else if (session.getAttribute("logintype").equals("android")) {
			MappingJackson2JsonView json = new MappingJackson2JsonView();
			Gson gson = new Gson();
			String output = gson.toJson(mv.getModel());
			json.addStaticAttribute("output", output);
			return new ModelAndView(json);
		} else {
			throw new BusinessException();
		}
	}
}
