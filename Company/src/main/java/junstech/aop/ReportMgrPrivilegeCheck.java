package junstech.aop;

import java.util.Date;

import javax.servlet.http.HttpSession;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.aop.aspectj.annotation.MetadataAwareAspectInstanceFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import junstech.exception.LoginException;
import junstech.exception.PrivilegeException;
import junstech.model.Privilege;
import junstech.model.User;
import junstech.util.MetaData;

@Aspect
public class ReportMgrPrivilegeCheck {

	public ReportMgrPrivilegeCheck() {

	}

	@Pointcut("execution(* junstech.collab.mgr.report.*.*(..))")
	private void aopAspect() {
	}// define a pointcut

	@Before("aopAspect()")
	public void doAccessCheck() throws LoginException, PrivilegeException {
		//System.out.println("AOP: before collab 2");
		ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
		HttpSession session = attr.getRequest().getSession(true);
		User user = (User) session.getAttribute("user");
		if (user == null) {
			String logMsg = new Date() + " 用户没有登录: Invaild break in for " + attr.getRequest().getRequestURI()
					+ "\r\nHacker info: " + attr.getRequest().getRemoteAddr() + "\r\n"
					+ attr.getRequest().getRemoteHost() + "\r\n" + attr.getRequest().getRemoteUser();
			System.out.println(logMsg);
			throw new LoginException();
		}

		String url = attr.getRequest().getRequestURI();
		boolean accessRight = false;
		if (url.contains("query") || url.contains("summary")) {
			for (Privilege privilege : user.getPrivileges()) {
				if (privilege.getProgramid() == MetaData.ReportMgrModule) {
					if (privilege.getPrivilege().contains("R")) {
						accessRight = true;
					}
				}
			}
		}

		if (url.contains("create") || url.contains("edit") || url.contains("submit")) {
			for (Privilege privilege : user.getPrivileges()) {
				if (privilege.getProgramid() == MetaData.ReportMgrModule) {
					if (privilege.getPrivilege().contains("W")) {
						accessRight = true;
					}
				}
			}
		}

		if (url.contains("delete")) {
			for (Privilege privilege : user.getPrivileges()) {
				if (privilege.getProgramid() == MetaData.ReportMgrModule) {
					if (privilege.getPrivilege().contains("D")) {
						accessRight = true;
					}
				}
			}
		}

		if (!accessRight) {
			throw new PrivilegeException();
		}
	}

	@AfterReturning("aopAspect()")
	public void doAfter() {
		//System.out.println("AOP: final collab 5");
	}

	@After("aopAspect()")
	public void after() {
		//System.out.println("AOP: after collab 4");
	}

	@AfterThrowing("aopAspect()")
	public void doAfterThrow() {
		//System.out.println("AOP: Exception");
	}

	@Around("aopAspect()")
	public Object doBasicProfiling(ProceedingJoinPoint pjp) throws Throwable {
		//System.out.println("AOP: before collab action 1");
		Object object = pjp.proceed();//
		//System.out.println("AOP: after collab action 3");
		return object;
	}
}
