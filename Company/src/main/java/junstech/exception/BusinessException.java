package junstech.exception;

import org.springframework.stereotype.Component;

@Component
public class BusinessException extends Exception{
	
	private static final long serialVersionUID = 1L;  
	
	public BusinessException(){
		
	}
}
