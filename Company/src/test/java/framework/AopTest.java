package framework;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import junstech.aop.UserMgrPrivilegeCheck;
import junstech.batch.ReportGenerator;
import junstech.collab.mgr.user.UserDML;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring-jdbc.xml", "classpath:spring-mvc.xml" })
public class AopTest {

	@Test
	public void inteceptorTest() throws Exception {

	}
}
