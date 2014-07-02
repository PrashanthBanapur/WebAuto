package org.seltest.test;

import org.apache.log4j.PatternLayout;
import org.apache.log4j.spi.LoggingEvent;
import org.openqa.selenium.WebDriver;
import org.seltest.driver.DriverManager;

/**
 * 
 * Class to Extend Log4j logging pattern to include ThreadID and Driver Hashcode<br/>
 * Pattern used : <br/> <b>
 * Thread ID : %%i <br/>
 * Driver ID : %%w <b/>
 * @author adityas
 */
public class LoggingPattern extends PatternLayout{
	public String format(LoggingEvent event) {
		   String log = super.format(event);
		   Long threadId = Thread.currentThread().getId();
		   String  driverId = "";
		   WebDriver driver = DriverManager.getDriver();
		   
		  if(driver==null){
			  driverId = "null";
		  }else{
			  driverId = Integer.toString(driver.hashCode());
		  }
		  
		   /*
		   * Replacing all %%i with the current thread Id
		   * Replacing all %%w with the driver hash code
		   */
		   log = log.replaceAll("%i", "T= "+Long.toString(threadId));
		   log = log.replaceAll("%w", "D= "+driverId);
		   return log;
		}
}
