package junstech.util;

import java.util.Date;

import javax.servlet.http.HttpSession;

import org.springframework.web.context.request.ServletRequestAttributes;

import junstech.exception.LoginException;
import junstech.exception.PrivilegeException;
import junstech.model.Privilege;
import junstech.model.User;

public class CommonMethod {

	public static void checkPrivilege(ServletRequestAttributes attr, HttpSession session, int criteria) throws LoginException, PrivilegeException{
		User user = (User) session.getAttribute("user");
		String url = attr.getRequest().getRequestURI();
		if (user == null) {
			String logMsg = new Date() + " Invaild break in for " + attr.getRequest().getRequestURI()
					+ "\r\nHacker info: " + attr.getRequest().getRemoteAddr() + "\r\n"
					+ attr.getRequest().getRemoteHost() + "\r\n" + attr.getRequest().getRemoteUser();
			System.out.println(logMsg);
			if(url.contains("android")){
				attr.getRequest().setAttribute("privilegeFail", "nologin");
			}
			throw new LoginException();
		}

		
		boolean accessRight = false;
		if (url.contains("query") || url.contains("summary")) {
			for (Privilege privilege : user.getPrivileges()) {
				if (privilege.getProgramid() == criteria) {
					if (privilege.getPrivilege().contains("R")) {
						accessRight = true;
					}
				}
			}
		}

		if (url.contains("create") || url.contains("edit") || url.contains("submit")) {
			for (Privilege privilege : user.getPrivileges()) {
				if (privilege.getProgramid() == criteria) {
					if (privilege.getPrivilege().contains("W")) {
						accessRight = true;
					}
				}
			}
		}

		if (url.contains("delete")) {
			for (Privilege privilege : user.getPrivileges()) {
				if (privilege.getProgramid() == criteria) {
					if (privilege.getPrivilege().contains("D")) {
						accessRight = true;
					}
				}
			}
		}
		if (!accessRight && attr.getRequest().getRequestURI().contains("android")) {
			attr.getRequest().setAttribute("privilegeFail", "noprivilege");
		}else if(!accessRight){
			throw new PrivilegeException();
		}else if(attr.getRequest().getRequestURI().contains("android")){
			attr.getRequest().setAttribute("privilegeFail", "pass");
		}
	}
}
