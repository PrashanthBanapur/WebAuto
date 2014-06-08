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
import org.seltest.driver.DriverManager;
import org.seltest.test.LoggerUtil;
import org.testng.SkipException;

/**
 * @author adityas
 * All the Methods for executing Test Steps which are not provided by Webdriver
 */
public class StepUtil {
	
	private static final LoggerUtil logger = LoggerUtil.getLogger();
	private static final Step STEP= new Step();


	/**
	 * 
	 * Wait for the mentioned Time in seconds
	 * <b> Uses tread.sleep
	 * @param Time in seconds
	 */
	public static void simpleWait(int minWait){
		logger.debug("(SIMPLE WAIT)	-> TIME = "+minWait+" sec ");
		try {
			Thread.sleep(minWait*1000);
		} catch (InterruptedException e) {
			throw new SelTestException("Wait Error  for : "+minWait);
		}
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


	public static List<WebElement> waitElementWithoutStale(WebDriver driver , List<WebElement> elements){
		int i=0;
		while(i< Integer.parseInt(Config.explictWaitMaxTimeout.getValue())){
			try{
				if(elements.get(elements.size()-1).isDisplayed())
					return elements;
			}catch(StaleElementReferenceException e){
				StepUtil.simpleWait(1);
			}finally{
				i++;
			}
		}
		return elements;

	}
	/**
	 * Wait For a particular expected condition . <br/>
	 * <b> Max Timeout depends on config properties <b/>
	 * @see ExpectedConditions
	 */
	public static void waitElementVisible(WebDriver driver ,By by){
		WebDriverWait wait = new WebDriverWait(driver,Integer.parseInt(Config.explictWaitMaxTimeout.getValue()));
		wait.until(ExpectedConditions.visibilityOfElementLocated(by));
		wait.pollingEvery(1, TimeUnit.SECONDS);
		wait.ignoring(NoSuchElementException.class);
		wait.ignoring(StaleElementReferenceException.class);

	}

	public static void waitElementVisible(WebDriver driver , By by , int time){
		WebDriverWait wait = new WebDriverWait(driver,time);
		wait.until(ExpectedConditions.visibilityOfElementLocated(by));
		wait.pollingEvery(1, TimeUnit.SECONDS);
		wait.ignoring(NoSuchElementException.class);
		wait.ignoring(StaleElementReferenceException.class);
	}

	/**
	 * Implicit wait for the mentioned time
	 * @param Time in seconds
	 */
	public static void waitImplicit(int minWait) {
		WebDriver driver = DriverManager.getDriver();
		driver.manage().timeouts().implicitlyWait(minWait, TimeUnit.SECONDS);

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
		logger.web(" (SWITCH WINDOW)	-> To Page : "+title);

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
		logger.web(" (CLOSE WINDOW)	-> Page Title : "+driver.getTitle());
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
		int i=0;
		String rowText = null;
		while(true){
			simpleWait(2);// TODO Need As findElements does not have explicit Wait
			// Now get all the TR elements from the table 
			try{
				List<WebElement> allRows = table.findElements(By.tagName("tr")); 
				// And iterate over them, getting the cells 
				for (WebElement row : allRows) { 

					rowText= row.getText();

					if(rowText.contains(unique)){
						List<WebElement> cells = row.findElements(By.tagName("td")); 
						return cells;
					}
				}
			}catch(StaleElementReferenceException ex){
				reloadPage(driver);
				i=0;
			}

			//Next Page
			if(STEP.isPresent(By.linkText(((2+i)+"")), driver)){
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

	public static void reloadPage(WebDriver driver){
		driver.navigate().refresh();
		waitForPageLoaded(driver);
	}

	public static int getLatency(WebDriver driver){

		return 0; //TODO GET LATENCY

	}

	public static void acceptAlert(WebDriver driver){
		//	StepUtil.simpleWait(3);
		driver.switchTo().alert().accept();
	}

	/**
	 *  Draws a red border around the found element
	 * @param driver
	 * @param element
	 * @return current border
	 */
	public static String highlightElement(WebDriver driver,WebElement element) {
		// draw a border around the found element
		String border = null;
		if (driver instanceof JavascriptExecutor) {
			border = (String) ((JavascriptExecutor)driver).executeScript(SCRIPT_GET_ELEMENT_BORDER, element);
			((JavascriptExecutor)driver).executeScript("arguments[0].style.border='3px solid red'", element);
		}
		return border;

	}

	/**
	 * Un Highlight Already Highlighted Element 
	 * @param driver
	 * @return
	 */
	public static WebElement unhighlightElement(WebDriver driver , WebElement element , String border){
		if (driver instanceof JavascriptExecutor) {
			try {
				((JavascriptExecutor)driver).executeScript(SCRIPT_UNHIGHLIGHT_ELEMENT, element, border);
			} catch (StaleElementReferenceException ignored) {
				// the page got reloaded, the element isn't there
			} 
		}
		return element;
	}


	private static final String SCRIPT_GET_ELEMENT_BORDER = 
			" var elem = arguments[0]; "+
					" if (elem.currentStyle) { "+
					"   var style = elem.currentStyle; "+
					"   var border = style['borderTopWidth'] "+
					"           + ' ' + style['borderTopStyle'] "+
					"          + ' ' + style['borderTopColor'] "+
					"          + ';' + style['borderRightWidth'] "+
					"          + ' ' + style['borderRightStyle'] "+
					"          + ' ' + style['borderRightColor'] "+
					"          + ';' + style['borderBottomWidth'] "+
					"          + ' ' + style['borderBottomStyle'] "+
					"          + ' ' + style['borderBottomColor'] "+
					"          + ';' + style['borderLeftWidth'] "+
					"          + ' ' + style['borderLeftStyle'] "+
					"          + ' ' + style['borderLeftColor']; "+
					"	} else if (window.getComputedStyle) { "+
					"  var style = document.defaultView.getComputedStyle(elem); "+
					"   var border = style.getPropertyValue('border-top-width') "+
					"          + ' ' + style.getPropertyValue('border-top-style') "+
					"           + ' ' + style.getPropertyValue('border-top-color') "+
					"           + ';' + style.getPropertyValue('border-right-width') "+
					"           + ' ' + style.getPropertyValue('border-right-style') "+
					"           + ' ' + style.getPropertyValue('border-right-color') "+
					"           + ';' + style.getPropertyValue('border-bottom-width') "+
					"           + ' ' + style.getPropertyValue('border-bottom-style') "+
					"           + ' ' + style.getPropertyValue('border-bottom-color') "+
					"           + ';' + style.getPropertyValue('border-left-width') "+
					"           + ' ' + style.getPropertyValue('border-left-style') "+
					"           + ' ' + style.getPropertyValue('border-left-color'); "+
					"	} "+
					"return border;" ;

	private static final String SCRIPT_UNHIGHLIGHT_ELEMENT =
			"	var elem = arguments[0]; "+
					"var borders = arguments[1].split(';');"+
					"elem.style.borderTop = borders[0];"+
					"elem.style.borderRight = borders[1];"+
					"elem.style.borderBottom = borders[2];"+
					"elem.style.borderLeft = borders[3];";
} 


