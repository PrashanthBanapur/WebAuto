/**
 * 
 */
package org.seltest.core;

import java.util.Iterator;
import java.util.Set;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.seltest.driver.DriverFactory;
import org.seltest.driver.DriverManager;
import org.seltest.driver.PageElementLocator;
import org.seltest.test.LoggerUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class with All operation related to Browser and not dependent on WebElements
 * 
 * @author adityas
 * */
public class Browser {

	private final Logger log = LoggerFactory.getLogger(Browser.class);

	/**
	 * 
	 * Wait for the mentioned Time in seconds <b> Uses tread.sleep
	 * 
	 * @param Time
	 *            in seconds
	 */
	public void simpleWait(int sec) {
		log.debug("(WAIT SEC)	-> TIME = " + sec + " sec ");
		org.openqa.selenium.browserlaunchers.Sleeper
		.sleepTightInSeconds(sec);
	}

	public void simpleWaitMillSec(int millSec) {
		log.debug("(WAIT MILL-SEC)	-> TIME = " + millSec + " mill sec ");
		org.openqa.selenium.browserlaunchers.Sleeper.sleepTight(millSec);
	}


	/**
	 * Wait For the Page to Load in the Browser
	 * 
	 * @param driver
	 */
	public synchronized void waitForPageLoaded() {
		ExpectedCondition<Boolean> expectation = new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver driver) {
				return ((JavascriptExecutor) DriverManager.getDriver()).executeScript(
						"return document.readyState").equals("complete");
			}
		};

		Wait<WebDriver> wait = new WebDriverWait(DriverManager.getDriver(), 30);
		try {
			wait.until(expectation);
		} catch (Throwable error) {
			new SelTestException(
					"Timeout waiting for Page Load Request to complete.");
		}
	}

	/**
	 * Switch to the window based on the title <br/>
	 * Use <b>clickSwitch() </b> to switch to new window
	 * 
	 * @param driver
	 * @param title
	 * @return status
	 * @exception IllegalArgumentException
	 *                if both the title are same
	 */
	public void windowSwitch(WebDriver driver, String title) {
		Set<String> windows = driver.getWindowHandles();
		String window = null;
		Iterator<String> winItr = windows.iterator();

		if (driver.getTitle().equals(title)) { // Both have same title switch to
			// 2nd
			throw new IllegalArgumentException(
					"Current Title and Swicth window title are same ");
		}
		log.info(LoggerUtil.webFormat() + "(SWITCH WINDOW)	-> To Page : {} ",
				title);

		while (winItr.hasNext()) {
			window = winItr.next();
			WebDriver switchWin = driver.switchTo().window(window);
			if (switchWin.getTitle().equals(title)) {
				break;
			}
		}
	}

	/**
	 * Close the current window and switch back to previous window
	 * 
	 * @param driver
	 */
	public synchronized void windowClose() {
		WebDriver driver = DriverManager.getDriver();
		Set<String> windows = driver.getWindowHandles();
		log.info(LoggerUtil.webFormat() + "(CLOSE WINDOW)	-> Page Title : {} ",
				driver.getTitle());
		driver.close();
		for (String window : windows) {
			driver.switchTo().window(window);
			break;
		}
	}

	/**
	 * Refresh The Page
	 * 
	 * @param driver
	 */
	public synchronized void reloadPage() {
		WebDriver driver = DriverManager.getDriver();
		driver.navigate().refresh();
		log.trace("Reloading Page ");
		waitForPageLoaded();
	}

	public int getLatency(WebDriver driver) {

		return 0; // TODO GET LATENCY

	}

	/**
	 * Accept Alert in Browser
	 * 
	 * @param driver
	 */
	public synchronized void acceptAlert() {

		WebDriver driver = DriverManager.getDriver();
		String browserName = Config.browser.getValue();

		if (browserName.equalsIgnoreCase("safari")) {
			((JavascriptExecutor) driver)
			.executeScript("confirm = function(message){return true;};");
		} else {
			driver.switchTo().alert().accept();
		}

		log.trace("Alert Accepted :");
	}

	/**
	 * Check Alert is Present in Page
	 * @return state
	 */
	public synchronized boolean isAlert(){
		WebDriver driver = DriverManager.getDriver();

		try { 
			driver.switchTo().alert(); 
			return true; 
		} catch (NoAlertPresentException Ex) { 
			return false; 
		} 
	}

	public synchronized void goToURL(String url) {
		WebDriver driver = DriverManager.getDriver();
		driver.get(url);
	}

	public synchronized <T> T createPage(Class<T> pageClass) {
		WebDriver driver = DriverManager.getDriver();
		return PageFactory.initElements(driver, pageClass);
	}

	public synchronized <T>T createPage(PageElementLocator pElement,Class<T> pageClass){
		WebDriver driver = DriverManager.getDriver();
		T page = PageFactory.initElements(driver, pageClass);
		PageFactory.initElements(pElement, page);
		return page;

	}

	public synchronized void verifyStartPage(StartState state) {
		if (!state.isStartState()) {
			state.goToStartPage();
		}
	}

	public WebDriver createBrowser(){
		return DriverFactory.getDriver();
	}

	public void closeBrowser(WebDriver driver){
		driver.quit();
	}
}
