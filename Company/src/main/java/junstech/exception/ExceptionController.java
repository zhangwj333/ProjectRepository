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

@Component
public class ExceptionController extends BaseController implements HandlerExceptionResolver{
	
	@ExceptionHandler
	public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object obj,
			Exception ex) {
		ModelAndView mv = new ModelAndView();
		
		if (ex.getCause() instanceof BusinessException){
			mv.addObject("failcause", "business");
			mv.addObject("message", "ϵͳ��������ϵά����Ա��");
			mv.addObject(MetaData.completeReturnPage, "redirect.htm?view=content");
			mv.addObject(MetaData.setTargetFrame, MetaData.setTargetAsContentFrame);
		}
		else if (ex.getCause() instanceof LoginException){
			mv.addObject("failcause", "login");
			mv.addObject("message", "��δ��¼��δ����ʱ������������µ�¼��");
			mv.addObject(MetaData.completeReturnPage, "userLogout.htm");
			mv.addObject(MetaData.setTargetFrame, MetaData.setTargetAsFullWindow);
		}
		else if (ex.getCause() instanceof PrivilegeException){
			mv.addObject("failcause", "privilege");
			mv.addObject("message", "��û��Ȩ�޽��иò�����");
			mv.addObject(MetaData.completeReturnPage, "redirect.htm?view=content");
			mv.addObject(MetaData.setTargetFrame, MetaData.setTargetAsContentFrame);
		}
		else {
			ex.printStackTrace();
			LogUtil.logger.error(ex.getMessage());
			mv.addObject("message", "δ֪ϵͳ��������ϵά����Ա��");
			mv.addObject(MetaData.completeReturnPage, "redirect.htm?view=content");
			mv.addObject(MetaData.setTargetFrame, MetaData.setTargetAsContentFrame);
		}
		
		mv.addObject(MetaData.setNoteTitle, "������!");
		mv.addObject(MetaData.setNoteType, MetaData.cosmoDanger);

		mv.setViewName("complete");
		try {
			return this.outputView((HttpSession) request.getSession(), mv);
		} catch (Exception e) {
			e.printStackTrace();
			mv.addObject(MetaData.ProcessResult, MetaData.ProcessFail);
			MappingJackson2JsonView json = new MappingJackson2JsonView();
			String output = JacksonUtil.obj2json(mv.getModel());
			json.addStaticAttribute("output", output);
			return new ModelAndView(json);
		}
	}
	

}
