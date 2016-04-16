package junstech.util;

import org.springframework.beans.BeansException; 
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory; 
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer; 

import java.util.Properties; 

/** 
* PropertyPlaceholderConfigurer process Properties
* 
* @author zhangwj333 2015-12-18 16:47 
*/ 
public class PropertyPlaceholderConfigurerExt extends PropertyPlaceholderConfigurer{ 

        @Override 
        protected void processProperties(ConfigurableListableBeanFactory beanFactory, Properties props) 
                        throws BeansException { 
                String password = props.getProperty("jdbc.pass"); 
                if (password != null) { 
                		//解密jdbc.password属性值，并重新设置 
                        try {
							props.setProperty("jdbc.pass", AESEncryption.decrypt(password, ENVConfig.encryptKey));
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} 
                }
                super.processProperties(beanFactory, props); 

        } 
        
        
}
