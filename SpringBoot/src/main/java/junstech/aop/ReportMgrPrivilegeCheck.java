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
import junstech.util.CommonMethod;
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
		CommonMethod.checkPrivilege(attr, session, MetaData.ReportMgrModule);	
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
