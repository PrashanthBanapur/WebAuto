/**
 * 
 */
package org.seltest.core;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.seltest.driver.DriverListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.SkipException;

/**
 * @author adityas
 *
 */
public class StepUtil {
	private static final Logger log = LoggerFactory.getLogger("STEP");

	/**
	 * 
	 * Wait for the mentioned Time in seconds
	 * <b> Uses tread.sleep
	 * @param 
	 */
	public static void simpleWait(int minWait){
		log.debug("		|-(SIMPLE WAIT)	-> TIME = '{}' ",minWait);
		try {
			Thread.sleep(minWait*1000);
		} catch (InterruptedException e) {
			throw new SelTestException("Wait Error  for : "+minWait);
		}
	}

	/**
	 * Implicit wait for the mentioned time
	 * @param Time in seconds
	 */
	public static void waitImplicit(WebDriver driver ,int minWait) {

		driver.manage().timeouts().implicitlyWait(minWait, TimeUnit.SECONDS);

	}
	/**
	 * Wait For a particular expected condition
	 * @see ExpectedConditions
	 */
	public static void waitElement(WebDriver driver , ExpectedCondition<WebElement> expectedCondition){
		WebDriverWait wait = new WebDriverWait(driver, ConfigProperty.getExplictWaitTime());
		wait.until(expectedCondition);
		wait.pollingEvery(1, TimeUnit.SECONDS);
		wait.ignoring(NoSuchElementException.class);
	}


	/**
	 * Select a Value based on visible text from List 
	 * @param ddList
	 * @param val
	 */
	public static void select(WebElement ddList ,String val){
		Select option = new Select(ddList);
		option.selectByVisibleText(val);
	}

	/**
	 * Get the Text selected in a drop down list
	 * @see driver.getText() method      
	 */
	public static String getSelectedText(WebElement ddList){
		Select option = new Select(ddList);
		return option.getFirstSelectedOption().getText();
	}


	/**
	 * Switch to the new window opened after clicking a link
	 * @param driver
	 * @param title
	 * @return status 
	 */
	public static Boolean switchWindow(WebDriver driver,String title){
		String testName = DriverListener.getTestName();
		Set<String> windows = driver.getWindowHandles();
		log.info("		|<{}>	--(SWITCH WINDOW)	-> To Page : {}",testName,title);
		for(String window : windows){
			WebDriver switchWin=driver.switchTo().window(window);
			if(switchWin.getTitle().equals(title)){
				return true;
			}
		}
		return false;
	}

	/**
	 * Close the current window and switch back to previous window
	 * @param driver
	 */
	public static void windowClose(WebDriver driver){
		String testName = DriverListener.getTestName();
		Set<String> windows = driver.getWindowHandles();
		log.info("		|<{}>	-(CLOSE WINDOW)	-> Page Title : {}",testName,driver.getTitle());
		driver.close();
		for(String window : windows){
			driver.switchTo().window(window);
			break;
		}
	}

	/**
	 * Check if WebElement is present in the page
	 * Returns the boolean
	 * @param
	 */
	public static boolean isPresent(By by,WebDriver driver){

		try{
			if(driver.findElements(by).size()>0){
				return true;
			}}catch(Exception e){
				return false;
			}
		return false;
	}

	/**
	 * Method to Find the text in WebElement
	 * @param element
	 * @return Text in the field
	 */
	public static String getText(WebElement element) {
		String value = element.getAttribute("value");
		if(value!= null){
			return value;
		}else{
			return element.getText();
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
		int i=0;

		while(true){
			StepUtil.simpleWait(2);// TODO Need As findElements does not have explicit Wait
			// Now get all the TR elements from the table 
			List<WebElement> allRows = table.findElements(By.tagName("tr")); 

			// And iterate over them, getting the cells 
			for (WebElement row : allRows) { 
				String rowText = row.getText();
				if(rowText.contains(unique)){
					List<WebElement> cells = row.findElements(By.tagName("td")); 
					return cells;
				}
			}
			//Next Page
			if(StepUtil.isPresent(By.linkText(((2+i)+"")), driver)){
				driver.findElement(By.linkText((2+i)+"")).click();
			}else{
				return null;
			}
			++i;
		}
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
			throw new SkipException("Cells Are Empty ! ");
		}
	}

	public static int getLatency(WebDriver driver){

		return 0; //TODO GET LATENCY

	}


}


