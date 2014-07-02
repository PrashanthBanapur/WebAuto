/**
 * 
 */
package org.seltest.core;

import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.seltest.test.LoggerUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author adityas
 * All the Methods for executing Test Steps which are not provided by Webdriver
 */
public class StepUtil {

	private static final Logger log = LoggerFactory.getLogger(StepUtil.class);
	private static final int MAX_RETRY = Integer.parseInt(Config.exceptionRetry.getValue());
	private static final Step step= new Step();


	/**
	 * 
	 * Wait for the mentioned Time in seconds
	 * <b> Uses tread.sleep
	 * @param Time in seconds
	 */
	public static void simpleWait(int minWait){
		log.debug("(WAIT SEC)	-> TIME = "+minWait+" sec ");
		org.openqa.selenium.browserlaunchers.Sleeper.sleepTightInSeconds(minWait);
	}
	public static void simpleWaitMillSec(int millSec){
		log.debug("(WAIT MILL-SEC)	-> TIME = "+millSec+" mill sec ");
		org.openqa.selenium.browserlaunchers.Sleeper.sleepTight(millSec);
	}

	/**
	 * Wait for the Time mentioned in properties file
	 */
	public static void defaultWait(){
		int defaultWait = Integer.parseInt(Config.defaultWait.getValue());
		simpleWait(defaultWait);
	}
	public static void waitForPageLoaded(WebDriver driver) {

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

	public static void waitElementVisible(WebDriver driver , By by , int time){
		WebDriverWait wait = new WebDriverWait(driver,time);
		wait.until(ExpectedConditions.visibilityOfElementLocated(by));
		wait.pollingEvery(1, TimeUnit.SECONDS);
		wait.ignoring(NoSuchElementException.class);
		wait.ignoring(StaleElementReferenceException.class);
	}

	/**
	 * Switch to the window based on the title <br/>
	 * Use <b>clickSwitch() </b> to switch to new window
	 * @param driver
	 * @param title
	 * @return status 
	 * @exception IllegalArgumentException if both the title are same
	 */
	public static void windowSwitch(WebDriver driver,String title){
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
	public static void windowClose(WebDriver driver){
		Set<String> windows = driver.getWindowHandles();
		log.info(LoggerUtil.webFormat()+"(CLOSE WINDOW)	-> Page Title : {} ",driver.getTitle());
		driver.close();
		for(String window : windows){
			driver.switchTo().window(window);
			break;
		}
	}


	/**
	 * Get the Row of a table 
	 * @param driver
	 * @param table
	 * @param unique
	 * @return
	 */
	public static List<WebElement> getRow(WebDriver driver ,WebElement table , String unique){
		int page=1;
		String rowText = null;
		while(true){
			StepUtil.simpleWait(5);
			
			List<WebElement> allRows = findElementsWithoutStale(table, By.tagName("tr")); 
			// And iterate over them, getting the cells 
			for (WebElement row : allRows) { 
				rowText= step.getText(row);
				if(rowText.contains(unique)){
					List<WebElement> cells = findElementsWithoutStale(row,By.tagName("td")); 
					log.trace("Row Found : {} ", rowText);
					return cells;
				}
			}
			
			//Next Page
			page++;
			if(step.isPresent(By.linkText(((page)+"")), driver)){
				driver.findElement(By.linkText((page)+"")).click();
			}else{
				return null;
			}
		}
	}

	private static List<WebElement> findElementsWithoutStale(WebElement element , By by){
		int retry = 0;
		List<WebElement> toReturn = null;
		while(retry <MAX_RETRY){
			try{
				toReturn = element.findElements(by); 
				break;
			}catch(StaleElementReferenceException ex){
				defaultWait();
				log.debug("Handling Stale Exception in findElementsWithoutStale Method !!! ");
			}finally{
				retry++;
			}
		}
		return toReturn;
	}

	public static boolean clickRow(List<WebElement> cells , String button){
		if(cells!=null){
			for (WebElement cell : cells) { 
				String cellText = cell.getText();
				if(cellText.contains(button)){
					cell.findElement(By.linkText(button)).click();
					return true;
				}
			}
			return false;
		}else{
			throw new SelTestException("Cells Are Empty ! ");
		}
	}

	public static void reloadPage(WebDriver driver){
		driver.navigate().refresh();
		log.trace("Reloading Page ");
		waitForPageLoaded(driver);
	}

	public static int getLatency(WebDriver driver){

		return 0; //TODO GET LATENCY

	}

	public static void acceptAlert(WebDriver driver){
		driver.switchTo().alert().accept();
		log.trace("Alert Accepted :");
	}

}

