package junstech.exception;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;  
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;
import org.springframework.web.bind.annotation.ExceptionHandler;

import junstech.collab.BaseController;
import junstech.util.JacksonUtil;
import junstech.util.LogUtil;
import junstech.util.MetaData;
import junstech.util.LanguageUtil;

@Component
public class ExceptionController extends BaseController implements HandlerExceptionResolver{
	
	@ExceptionHandler
	public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object obj,
			Exception ex) {
		ModelAndView mv = new ModelAndView();
		
		if (ex instanceof BusinessException){
			mv.addObject("failcause", "business");
			mv.addObject("message", LanguageUtil.getString("BusinessException"));
			mv.addObject(MetaData.completeReturnPage, "redirect.htm?view=content");
			mv.addObject(MetaData.setTargetFrame, MetaData.setTargetAsContentFrame);
		}
		else if (ex instanceof LoginException){
			mv.addObject("failcause", "login");
			mv.addObject("message", LanguageUtil.getString("LoginException"));
			mv.addObject(MetaData.completeReturnPage, "userLogout.htm");
			mv.addObject(MetaData.setTargetFrame, MetaData.setTargetAsFullWindow);
		}
		else if (ex instanceof PrivilegeException){
			mv.addObject("failcause", "privilege");
			mv.addObject("message", LanguageUtil.getString("PrivilegeException"));
			mv.addObject(MetaData.completeReturnPage, "redirect.htm?view=content");
			mv.addObject(MetaData.setTargetFrame, MetaData.setTargetAsContentFrame);
		}
		else {
			ex.printStackTrace();
			LogUtil.logger.error(ex.getMessage());
			mv.addObject("message", LanguageUtil.getString("OtherException") + "\r\n" + ex.getMessage() + ex.toString());
			mv.addObject(MetaData.completeReturnPage, "redirect.htm?view=content");
			mv.addObject(MetaData.setTargetFrame, MetaData.setTargetAsContentFrame);
		}
		
		mv.addObject(MetaData.setNoteTitle, LanguageUtil.getString("ExceptionTitle"));
		mv.addObject(MetaData.setNoteType, MetaData.cosmoDanger);
		mv.addObject(MetaData.ProcessResult, MetaData.ProcessFail);
		mv.setViewName("complete");
		try {
			return this.outputView((HttpSession) request.getSession(), mv);
		} catch (Exception e) {
			e.printStackTrace();		
			MappingJackson2JsonView json = new MappingJackson2JsonView();
			String output = JacksonUtil.obj2json(mv.getModel());
			json.addStaticAttribute("output", output);
			return new ModelAndView(json);
		}
	}
	

}
