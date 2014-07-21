/**
 * 
 */
package org.seltest.core;

import java.util.Iterator;
import java.util.Set;

import org.openqa.selenium.JavascriptExecutor;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.seltest.test.LoggerUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class with All operation related to Browser and not dependent on WebElements
 * @author adityas
 * */
public class Browser {

	private  final Logger log = LoggerFactory.getLogger(Browser.class);


	/**
	 * 
	 * Wait for the mentioned Time in seconds
	 * <b> Uses tread.sleep
	 * @param Time in seconds
	 */
	public  void simpleWait(int minWait){
		log.debug("(WAIT SEC)	-> TIME = "+minWait+" sec ");
		org.openqa.selenium.browserlaunchers.Sleeper.sleepTightInSeconds(minWait);
	}
	public  void simpleWaitMillSec(int millSec){
		log.debug("(WAIT MILL-SEC)	-> TIME = "+millSec+" mill sec ");
		org.openqa.selenium.browserlaunchers.Sleeper.sleepTight(millSec);
	}


	/**
	 * Wait For the Page to Load in the Browser
	 * @param driver
	 */
	public  void waitForPageLoaded(WebDriver driver) {
		ExpectedCondition<Boolean> expectation = new
				ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver driver) {
				return ((JavascriptExecutor)driver).executeScript("return document.readyState").equals("complete");
			}
		};

		Wait<WebDriver> wait = new WebDriverWait(driver,30);
		try {
			wait.until(expectation);
		} catch(Throwable error) {
			new SelTestException("Timeout waiting for Page Load Request to complete.");
		}
	} 

	
	/**
	 * Switch to the window based on the title <br/>
	 * Use <b>clickSwitch() </b> to switch to new window
	 * @param driver
	 * @param title
	 * @return status 
	 * @exception IllegalArgumentException if both the title are same
	 */
	public  void windowSwitch(WebDriver driver,String title){
		Set<String> windows = driver.getWindowHandles();
		String window=null;
		Iterator<String> winItr = windows.iterator();

		if(driver.getTitle().equals(title)){ // Both have same title switch to 2nd
			throw new IllegalArgumentException("Current Title and Swicth window title are same ");
		}
		log.info(LoggerUtil.webFormat()+"(SWITCH WINDOW)	-> To Page : {} ",title);

		while(winItr.hasNext()){
			window = winItr.next();
			WebDriver switchWin=driver.switchTo().window(window);
			if(switchWin.getTitle().equals(title)){
				break;
			}
		}
	}

	/**
	 * Close the current window and switch back to previous window
	 * @param driver
	 */
	public  void windowClose(WebDriver driver){
		Set<String> windows = driver.getWindowHandles();
		log.info(LoggerUtil.webFormat()+"(CLOSE WINDOW)	-> Page Title : {} ",driver.getTitle());
		driver.close();
		for(String window : windows){
			driver.switchTo().window(window);
			break;
		}
	}


	/**
	 * Refresh The Page 
	 * @param driver
	 */
	public  void reloadPage(WebDriver driver){
		driver.navigate().refresh();
		log.trace("Reloading Page ");
		waitForPageLoaded(driver);
	}

	public  int getLatency(WebDriver driver){

		return 0; //TODO GET LATENCY

	}

	/**
	 * Accept Alert in Browser 
	 * @param driver
	 */
	public  void acceptAlert(WebDriver driver){
		driver.switchTo().alert().accept();
		log.trace("Alert Accepted :");
	}

	public WebDriver getSimpleDriver(WebDriver driver){
		WebDriver simpleDriver = driver;
		if(driver instanceof EventFiringWebDriver){
			EventFiringWebDriver eventFirDriver = (EventFiringWebDriver)driver;
			simpleDriver = eventFirDriver.getWrappedDriver();
		}
		return simpleDriver;
	}
}

