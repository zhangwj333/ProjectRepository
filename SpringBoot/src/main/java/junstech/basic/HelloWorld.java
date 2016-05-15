package junstech.basic;

import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.*;

@Controller
@EnableAutoConfiguration
public class HelloWorld extends SpringBootServletInitializer implements EmbeddedServletContainerCustomizer {

		@RequestMapping(value ="/hello")
	    @ResponseBody
	    public String hello(){
	        return "hello world";
	    }

		@Override  
	    public void customize(ConfigurableEmbeddedServletContainer container) {  
	        container.setPort(10080);  
	    }
		
	    public static void main(String[] args) throws Exception {
	        SpringApplication.run(HelloWorld.class, args);
	    }

}
