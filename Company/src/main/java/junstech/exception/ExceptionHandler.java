package junstech.exception;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;  
import org.springframework.web.servlet.ModelAndView;

import junstech.util.LogUtil;
import junstech.util.MetaData;

@Component
public class ExceptionHandler implements HandlerExceptionResolver{
	
	public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object obj,
			Exception ex) {
		ModelAndView mv = new ModelAndView();
		
		if (ex instanceof BusinessException){
			mv.addObject("message", "系统错误，请联系维护人员！");
			mv.addObject(MetaData.completeReturnPage, "redirect.htm?view=content");
			mv.addObject(MetaData.setTargetFrame, MetaData.setTargetAsContentFrame);
		}
		else if (ex instanceof LoginException){
			mv.addObject("message", "你未登录或未操作时间过长，请重新登录！");
			mv.addObject(MetaData.completeReturnPage, "userLogout.htm");
			mv.addObject(MetaData.setTargetFrame, MetaData.setTargetAsFullWindow);
		}
		else if (ex instanceof PrivilegeException){
			mv.addObject("message", "你没有权限进行该操作！");
			mv.addObject(MetaData.completeReturnPage, "redirect.htm?view=content");
			mv.addObject(MetaData.setTargetFrame, MetaData.setTargetAsContentFrame);
		}
		else {
			ex.printStackTrace();
			LogUtil.logger.error(ex.getMessage());
			mv.addObject("message", "未知系统错误，请联系维护人员！");
			mv.addObject(MetaData.completeReturnPage, "redirect.htm?view=content");
			mv.addObject(MetaData.setTargetFrame, MetaData.setTargetAsContentFrame);
		}
		
		mv.addObject(MetaData.setNoteTitle, "出错啦!");
		mv.addObject(MetaData.setNoteType, MetaData.cosmoDanger);

		mv.setViewName("complete");
		return mv;
	}
	

}
