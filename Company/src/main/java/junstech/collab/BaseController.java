package junstech.collab;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import junstech.exception.BusinessException;
import junstech.util.JacksonUtil;

@Controller
public class BaseController {

	public ModelAndView outputView(HttpSession session, ModelAndView mv) throws Exception {
		if (session.getAttribute("logintype").equals("web")) {
			return mv;
		} else if (session.getAttribute("logintype").equals("android")) {
			MappingJackson2JsonView json = new MappingJackson2JsonView();
			String output = JacksonUtil.obj2json(mv.getModel());
			json.addStaticAttribute("output", output);
			return new ModelAndView(json);
		} else {
			throw new BusinessException();
		}
	}
}
