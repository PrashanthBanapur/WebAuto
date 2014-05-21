package org.seltest.driver;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import org.seltest.core.Config;
import org.seltest.test.WebEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

final class DriverFactory {

	private static Logger log = LoggerFactory.getLogger(DriverFactory.class);
	private static String browser;
	private static String driverPath;
	private static Boolean eventFiring;
	private static Boolean fullscreen;

	static{
		browser = Config.browser.getValue();
		driverPath=Config.driverPath.getValue();
		eventFiring=Boolean.parseBoolean(Config.eventfiring.getValue());
		fullscreen=Boolean.parseBoolean(Config.fullscreen.getValue());
	}
	/**
	 * Method to get WebDriver based on app properties
	 */
	 static WebDriver getDriver(){
		WebDriver driver=null;
		if(browser.equalsIgnoreCase("FIREFOX")){
			driver = new FirefoxDriver();
			log.debug("Firefox Driver Created : {}" ,driver);

		}else if(browser.equalsIgnoreCase("CHROME")){			
			System.setProperty("webdriver.chrome.driver", driverPath+"/chromedriver.exe");
			driver = new ChromeDriver();
			log.debug("Chrome Driver Created : {}" ,driver);

		}else if(browser.equalsIgnoreCase("ANDROID")){
			driver = new RemoteWebDriver(DesiredCapabilities.android());
			log.debug("Android Driver Created : {}" ,driver);

		}else if(browser.equalsIgnoreCase("IE")){
			System.setProperty("webdriver.ie.driver", driverPath+"/iedriver.exe");
			driver = new InternetExplorerDriver();
			log.debug("IE Driver Created : {}" ,driver);
		}
		// Adding Web Event Listner
		if(eventFiring){
			EventFiringWebDriver efirDriver = new EventFiringWebDriver(driver);
			WebEventListener driverListner = new WebEventListener();
			driver = efirDriver.register(driverListner);
		}
		else {
			log.warn("FrameWork Wont Work Properly : 'Event Firing' Set To : '{}'",eventFiring);
		}
		
		if(fullscreen){
			driver.manage().window().maximize();
		}
		
		return driver;
	}
}
