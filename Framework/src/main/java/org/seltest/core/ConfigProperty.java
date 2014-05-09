package org.seltest.core;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import org.seltest.driver.Browser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Class for loading the Application properties
 * @author adityas
 *
 */
public final class ConfigProperty {
	private static String name ="webapp.properties";
	private static final String USER_HOME =System.getProperty("user.home");
	private static Properties property;
	private static final Logger log = LoggerFactory.getLogger(ConfigProperty.class);
	static{
		property= new Properties();
		try {
			property.load(ConfigProperty.class.getClassLoader().getResourceAsStream("webapp.properties"));
			//Setting ATU Property Path
			String path = new File("./","src/main/resources/atu.properties").getAbsolutePath();
			System.setProperty("atu.reporter.config", path);
		} catch (IOException e) {
			throw new SelTestException("Unable to Load webapp.properties");
		}
	}

	private static String getProperty(String key){
		return property.getProperty(key);
	}

	/**
	 * Returns the Browser name in lower case <br/>
	 * <b> Field : browser
	 */
	public static String getBrowser(){
		//Validate Browser 
		String browser = getProperty("browser").toLowerCase();
		if(!(	browser.equalsIgnoreCase(Browser.CHROME.name())	||
				browser.equalsIgnoreCase(Browser.FIREFOX.name())||
				browser.equalsIgnoreCase(Browser.IE.name()))){
			throw new SelTestException(name+".properties Value , Invalid Browser : Browser '"+browser+"' not support by framework");
		}
		log.debug("Browser Loaded : {} ",browser);
		return browser;
	}

	/**
	 * Returns the Login Page Url <br/>
	 * <b> Field : loginUrl
	 */
	public static String getBaseUrl(){
		//Validate URL Has http:// 
		String URL =getProperty("baseUrl");
		if((!URL.contains("http"))){
			throw new  SelTestException(name+".properties Value , Invalid URL '"+URL+"' : should contain protocol name Ex http://");
		}
		log.debug("Url Loaded : {} ",URL);
		return URL;
	}

	public static String getDriverPath(){
		String path = USER_HOME+getProperty("driverPath");
		log.debug("Driver Path : {} ",path);
		return path;
	}
	
	public static String getWaitType(){
		
		String waitType = getProperty("waitType").toLowerCase();
		
		if(!(waitType.equals("explicit")||waitType.equals("implicit"))){
			throw new SelTestException("Invalid Wait Type : "+waitType);
		}
		
		log.debug("Wait Type  : {} ",waitType);
		return waitType;
	}
	
	/**
	 * Return Implicit Wait Time 
	 * @param
	 */
	public static int getImplicitWaitTime(){
		int waitValue = Integer.parseInt(getProperty("implicitWait"));
		log.debug("Implicit Wait Time : {} ",waitValue);
		return waitValue;
	}
	
	/**
	 * Return Max Time out for explicit Wait
	 * Returns the int
	 * @param
	 */
	public static int getExplictWaitTime(){
		int waitValue = Integer.parseInt(getProperty("explictWait"));
		log.debug("Explicit Wait TimeOut : {} ",waitValue);
		return waitValue;
	}

	public static Boolean getEventFiring(){
		if(getProperty("eventfiring").equalsIgnoreCase("true")){
			log.debug(" Event Firing : {} ",true);
			return true;
		}else{
			log.debug(" Event Firing : {} ",false);
			return false;
		}
	}

	/**
	 * Returns Full screen Option <br/>
	 * <b> Field : fullscreen
	 */
	public static Boolean getFullScreen(){
		if(getProperty("fullscreen").equalsIgnoreCase("true")){
			log.debug(" FullScreen Mode : {} ",true);
			return true;
		}else{
			log.debug(" FullScreen Mode : {} ",false);
			return false;
		}
	}
	
	/**
	 * Returns the Browser Instance Type <br/>
	 * <b> Field : browserInstance
	 */
	public static String getBrowserInstance(){
		String browserInstance = getProperty("browserInstance");
		if(!(browserInstance.equalsIgnoreCase("suite")||browserInstance.equalsIgnoreCase("tests"))){
			throw new SelTestException("Invalid Browser Instance Option : "+browserInstance);
		}
		return browserInstance;
	}
	
	/**
	 * Return on which case screen shot should be captured <br/>
	 * <b> Field : captureScreenshot
	 */
	public static String getScreenshot(){
		String screenshotOption = getProperty("captureScreenshot").toLowerCase();
		
		if(!(screenshotOption.equals("all")||screenshotOption.equals("result")||screenshotOption.equals("assertion"))){
			throw new SelTestException("Invalid ScreenShot Option : "+screenshotOption);
		}
		return screenshotOption;
	}
	
	
}
