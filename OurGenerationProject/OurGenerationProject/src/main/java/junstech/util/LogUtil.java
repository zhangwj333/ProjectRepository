package junstech.util;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Appender;
import org.apache.log4j.FileAppender;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class LogUtil {

	public static Logger logger = Logger.getLogger(LogUtil.class);

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// System.out.println("This is println message.");

		
		logger.debug("This is debug message.");
	
		logger.info("This is info message.");
	
		logger.error("This is error message.");
		
	}

}
