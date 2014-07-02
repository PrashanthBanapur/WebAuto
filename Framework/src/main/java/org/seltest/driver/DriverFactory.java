package org.seltest.driver;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.firefox.internal.ProfilesIni;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import org.seltest.core.Config;
import org.seltest.test.LoggerUtil;
import org.seltest.test.WebEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


final class DriverFactory {

	private static String browser;
	private static String driverPath;
	private static Boolean eventFiring;
	private static Boolean fullscreen;
	private static final Logger log = LoggerFactory.getLogger(DriverFactory.class);
	private static final LoggerUtil logger = LoggerUtil.getLogger();
	private static final int MAX_IMPLICIT_WAIT = 1;
	private static final int MAX_PAGELOAD_WAIT = 3;

	static{
		browser = Config.browser.getValue();
		driverPath=Config.driverPath.getValue();
		eventFiring=Boolean.parseBoolean(Config.eventfiring.getValue());
		fullscreen=Boolean.parseBoolean(Config.fullscreen.getValue());
		System.setProperty("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.SimpleLog");
		System.setProperty("org.apache.commons.logging.simplelog.log.org.apache.http", "warn");
	}
	/**
	 * Method to get WebDriver based on app properties
	 */
	 static WebDriver getDriver(){
		WebDriver driver=null;
		if(browser.equalsIgnoreCase("FIREFOX")){
			ProfilesIni profile = new ProfilesIni();
			FirefoxProfile ffprofile = profile.getProfile("default");
			driver = new FirefoxDriver(ffprofile);

		}else if(browser.equalsIgnoreCase("CHROME")){			
			System.setProperty("webdriver.chrome.driver", driverPath+"/chromedriver.exe");
			try{
			driver = new ChromeDriver();
			}catch(IllegalStateException ex){
				logger.exception(ex);
			}

		}else if(browser.equalsIgnoreCase("ANDROID")){
			driver = new RemoteWebDriver(DesiredCapabilities.android());

		}else if(browser.equalsIgnoreCase("IE")){
			System.setProperty("webdriver.ie.driver", driverPath+"/iedriver.exe");
			try{
			driver = new InternetExplorerDriver();
			}catch(IllegalStateException ex){
				logger.exception(ex);
			}
		}
		// Adding Web Event Listener
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
		
		driver.manage().timeouts().pageLoadTimeout(MAX_PAGELOAD_WAIT, TimeUnit.MINUTES);
		driver.manage().timeouts().implicitlyWait(MAX_IMPLICIT_WAIT, TimeUnit.MINUTES);
		return driver;
	}
}
