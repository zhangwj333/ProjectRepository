package framework;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import junstech.collab.mgr.user.UserDML;
import junstech.model.Privilege;
import junstech.model.User;
import junstech.service.PrivilegeService;
import junstech.service.UserService;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:spring-jdbc.xml", "classpath:spring-mvc.xml"})
public class MybatisTest{

	UserService userService;
	
	PrivilegeService privilegeService;
	
	
	public PrivilegeService getPrivilegeService() {
		return privilegeService;
	}

	@Autowired
	public void setPrivilegeService(PrivilegeService privilegeService) {
		this.privilegeService = privilegeService;
	}

	public UserService getUserService() {
		return userService;
	}

	@Autowired
	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	@Test
	public void MybatisTest() throws Exception{
		/*
		SimpleDateFormat df = new SimpleDateFormat(" yyyy-MM-dd HH:mm:ss");
		Map<String, Object> map = new HashMap<String, Object>();
		int num = 2;
		map.put("id", num- num);
		map.put("username", "xian");
		map.put("startdate", "2015-07-29");
		map.put("enddate", "2015-08-29 05:00:00");
		map.put("prev", num);
		map.put("next", num);
		List<User> users = userService.getAllUser(map);
		int userCount = userService.getAllUser().size();
		int privilegeCount = privilegeService.getAllPrivilege().size();
		User user = userService.getUserWithPrivilege("18588884563");
		user.setUsername("junstech~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		System.out.println(user.getUsername());
		//privilegeService.createPrivilege(user.getPrivileges());
		try {
			userService.createUser(user);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Assert.assertEquals(privilegeCount, privilegeService.getAllPrivilege().size()); 
		Assert.assertEquals(userCount, userService.getAllUser().size());
		*/    
	}

}
