package org.seltest.core;

import java.util.List;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import org.openqa.selenium.support.ui.Select;
import org.seltest.driver.DriverManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Class contains all the method to be used to intract with WebElement <br/>
 * Most of the methods are wrapper of WebElement Class . With some added
 * features to handle exceptions 
 * @see WebElement
 * @see StaleElementReferenceException
 * @see TimeoutException
 * @author adityas
 *
 */
public class Element {
	private final Browser browser = new Browser();
	public final WaitEvent wait = new WaitEvent();
	private static Logger log = LoggerFactory.getLogger(Element.class);
	private static final int MAX_RETRY = Integer.parseInt(Config.exceptionRetry.getValue());
	private static final int EXCEPTION_INTERVAL = Integer.parseInt(Config.waitMaxTimeout.getValue())/MAX_RETRY;


	Element(){
	}

	/**
	 * Select a Value based on visible text from List 
	 * @param ddList List to select from
	 * @param val Text to select 
	 */
	public void select(WebElement ddList ,String val){
		Select option = new Select(ddList);
		option.selectByVisibleText(val);
		log.trace("Selct Value : {}  from List : {} ",val,ddList );
	}


	/**
	 * Click on a Checkbox by finding using css
	 * @param element WebElement which has check box
	 */
	public void checkboxClick(WebElement element) {
		log.trace("Click on CheckBox : {}",element);
		click(element.findElement(By.cssSelector("input[type='checkbox']")));
	}

	/**
	 * Click on a Checkbox only if its not selected
	 * @param element WebElement which has check box
	 */
	public void checkboxSelect(WebElement element) {
		WebElement checkbox =element.findElement(By.cssSelector("input[type='checkbox']"));
		if(!checkbox.isSelected()){
			click(checkbox);
		}
		log.trace("Check Box Select : {} ",element);
	}
	/**
	 * Get the Text selected in a drop down list
	 * @param ddList List to find the text      
	 */
	public String getSelectedText(WebElement ddList){
		log.trace("Text in Drop Down List : {} " , ddList);
		Select option = new Select(ddList);
		String text = option.getFirstSelectedOption().getText();
		log.trace("Select Text for ddList : {}  value : {} ",ddList , text);
		return text;
	}


	/**
	 * Clear the Text in a Text Field
	 * @param element
	 * @see WebElement
	 */
	public void clear(WebElement element) {
		element.clear();
		log.trace("Clear Text on Elemetn : {} ",element);
	}

	/**
	 * Click on the WebElement and Wait For Page Load
	 * @param element
	 * @see WebElement
	 */
	public synchronized void clickAndWait(WebElement element) {
		log.trace(" Element Click And Wait : {} " , element);
		click(element);
		browser.waitForPageLoaded(DriverManager.getDriver());
	}

	/**
	 * Click on WebElement Called by all click Methods
	 * @param element
	 */
	public synchronized void click(WebElement element){

		String browserName = Config.browser.getValue();
		// Click on Body Tag if IE Browser to Focus
		if(browserName.equalsIgnoreCase("ie")){
			WebDriver driver = DriverManager.getDriver();
			driver.switchTo().window(driver.getWindowHandle());//Force Focus
		}

		log.trace(" Element Click : {} " , element);
		element.click();
	}


	/**
	 * 
	 * @param lstRadioButton List of Radio Buttons
	 * @param val Unique button value
	 */
	public void clickRadioButton(List<WebElement> lstRadioButton, String val) {
		log.trace("Click on Radio Button : {} value : {} ",lstRadioButton,val);
		for(WebElement radioBtn : lstRadioButton){
			if(getText(radioBtn).equals(val)){
				radioBtn.findElement(By.cssSelector("input[type='radio']")).click();			}
		}

	}

	/**
	 * Click on a Button inside a WebElement using type="button"
	 * @param WebElement containing button
	 */
	public void clickButton(WebElement element) {
		log.trace("Click on Button inside Element : {}  ",element);
		findElement(element,By.cssSelector("input[type='button']")).click();

	}

	/**
	 * Click on a WebElement and switch to the new Window
	 * @param element
	 */
	public synchronized void clickAndSwitch(WebElement element){
		log.trace("Click And Switch  : {} ",element);
		WebDriver driver = DriverManager.getDriver();
		String winHandleBefore = driver.getWindowHandle();
		click(element);
		Set<String> windows = driver.getWindowHandles();

		for(String window : windows){
			if(!window.equals(winHandleBefore)){
				driver.switchTo().window(window);
			}
		}
		browser.waitForPageLoaded(driver);
	}

	/**
	 * Method to Find the text in WebElement
	 * @param element
	 * @return Text in the field
	 */
	public String getText(WebElement element) {
		log.trace("Get Text for Element : {} ",element);
		int retry = 0;
		while(retry<MAX_RETRY){
			try{
				String value = element.getAttribute("value");
				String text = element.getText();
				if(value == null && text ==null ){
					browser.simpleWait(3);
				}else if(value != null){				
					return value;
				}else if(text != null){
					log.trace("Go Text : {} on Retry : {} ",text,retry);
					return element.getText();
				}
			}catch(StaleElementReferenceException e){
				browser.simpleWait(EXCEPTION_INTERVAL);
				log.debug("Stale Element Exception in getText !!! "+retry);
				if(retry==MAX_RETRY/2){
					WebDriver driver =DriverManager.getDriver();
					browser.reloadPage(driver);
				}
			}finally{
				retry++;
			}
		}
		throw new SelTestException("Unable to Get Element Text "+element);
	}

	/**
	 * Verify if WebElement is Displayed
	 * @param element
	 * @see WebElement
	 */
	public boolean isDisplayed(WebElement element) {
		log.trace("Is Displayed Element : {}",element);
		WebDriver driver = DriverManager.getDriver();
		WebDriver simpleDriver = browser.getSimpleDriver(driver);
		String value = getValue(element);
		By by = getBy(element,value);
		if(simpleDriver.findElement(by).isDisplayed()){
			return true;
		}else{
			return false;
		}
	}


	/**
	 * Verify if an Element is Enabled
	 * @param element
	 */
	public boolean isEnabled(WebElement element) {
		log.trace("Is Enabled Element : {}",element);
		WebDriver driver = DriverManager.getDriver();
		WebDriver simpleDriver = browser.getSimpleDriver(driver);
		String value = getValue(element);
		By by = getBy(element,value);
		if(simpleDriver.findElement(by).isEnabled()){
			return true;
		}else{
			return false;
		}
	}

	/**
	 * Verifyy if an Element is Selected
	 * @param element
	 */
	public boolean isSelected(WebElement element) {
		log.trace("Is Selected Element : {}", element);
		WebDriver driver = DriverManager.getDriver();
		WebDriver simpleDriver = browser.getSimpleDriver(driver);
		String value = getValue(element);
		By by = getBy(element,value);
		if(simpleDriver.findElement(by).isSelected()){
			return true;
		}else{
			return false;
		}
	}
	/**
	 * Check if WebElement is present in the page
	 * Returns the boolean status
	 * @param
	 */
	public synchronized boolean isPresent(By by){
		log.trace("Is Present Element : {}",by);
		WebDriver driver = DriverManager.getDriver();
		WebDriver simpleDriver = driver;
		if(driver instanceof EventFiringWebDriver){
			EventFiringWebDriver eventFirDriver = (EventFiringWebDriver)driver;
			simpleDriver = eventFirDriver.getWrappedDriver();
		}

		try{
			if(simpleDriver.findElements(by).size()>0){
				return true;
			}}catch(Exception e){
				return false;
			}
		return false;
	}


	/**
	 * Send Text to the WebElement
	 * @param element to which text will be send
	 * @param arg0 text to be send
	 * @see WebElement
	 */
	public void sendKeys(WebElement element ,CharSequence... arg0) {
		log.trace("Send Key : {} on Element : {} ",arg0,element);
		element.sendKeys(arg0);

	}

	/**
	 * Submit a Form 
	 * @param element to be click to submit
	 * @see WebElement
	 */
	public void submit(WebElement element) {
		log.trace("Submit on Element : {}",element);
		element.submit();
	}

	private String getValue(WebElement element){
		String elementType =element.toString();
		int valueBeginIndex = elementType.lastIndexOf(':')+2;
		int valueEndIndex = elementType.lastIndexOf(']');
		String value = elementType.substring(valueBeginIndex, valueEndIndex);
		return value;
	}

	private By getBy(WebElement element,String value) {
		String elementType = element.toString();
		if(elementType.contains("partial link text")){
			return By.partialLinkText(value);
		}else if(elementType.contains("link text")){
			return By.linkText(value);
		}else if(elementType.contains("id")){
			return By.id(value);
		}else if(elementType.contains("name")){
			return By.name(value);
		}else if(elementType.contains("xpath")){
			return By.xpath(value);
		}else if(elementType.contains("class name")){
			return By.className(value);
		}else
			return null;
	}

	//	/**
	//	 * Wait for checking that an element is present on the DOM of a page and visible. <br/>
	//	 * Visibility means that the element is not only displayed but also has a height and width that is greater than 0.
	//	 * @param by
	//	 * @param time
	//	 */
	//	 synchronized void waitElementVisible( By by , int time){
	//		WebDriver driver = DriverManager.getDriver();
	//		WebDriverWait wait = new WebDriverWait(driver,time);
	//		wait.until(ExpectedConditions.visibilityOfElementLocated(by));
	//		wait.pollingEvery(1, TimeUnit.SECONDS);
	//		wait.ignoring(NoSuchElementException.class);
	//		wait.ignoring(StaleElementReferenceException.class);
	//	}

	//	/**
	//	 * An expectation for checking that an element is present on the DOM of a page.<br/>
	//	 *  This does not necessarily mean that the element is visible
	//	 * @param by
	//	 * @param time
	//	 */
	//	private synchronized void waitElementPresent( By by , int time){
	//		WebDriver driver = DriverManager.getDriver();
	//		WebDriverWait wait = new WebDriverWait(driver,time);
	//		wait.until(ExpectedConditions.presenceOfElementLocated(by));
	//		wait.pollingEvery(1, TimeUnit.SECONDS);
	//		wait.ignoring(NoSuchElementException.class);
	//		wait.ignoring(StaleElementReferenceException.class);
	//	}


	public List<WebElement> getRow(WebElement table , String unique){
		log.trace("Get Row for Table : {} with Unique : {} ",table,unique);
		String rowText = null;
		while(true){
			browser.simpleWait(5);
			List<WebElement> allRows = findElementsWithoutStale(table, By.tagName("tr")); 
			// And iterate over them, getting the cells 
			for (WebElement row : allRows) { 
				rowText= getText(row);
				if(rowText.contains(unique)){
					List<WebElement> cells = findElementsWithoutStale(row,By.tagName("td")); 
					log.trace("Row Found : {} ", rowText);
					return cells;
				}
			}
			return null;
		}
	}

	public List<WebElement> getRow(WebElement table , int rowNum){
		log.trace("Get Row for Table : {} with Index : {} ",table,rowNum);
		while(true){
			browser.simpleWait(5);
			WebElement row =table.findElement(By.xpath("./tbody/tr["+rowNum+"]"));
			List<WebElement> cells = findElementsWithoutStale(row,By.tagName("td")); 
			log.trace("Row Found : {} ", row.getText());
			return cells;
		}
	}

	/**
	 * Get the Row of a table 
	 * @param driver
	 * @param table
	 * @param unique
	 * @return
	 */
	public synchronized  List<WebElement> getRowWithPagenation(WebElement table , String unique){
		log.trace("Get Row with Pagenatin for Table : {} with Unique : {} ",table,unique);
		WebDriver driver = DriverManager.getDriver();
		int page=1;
		String rowText = null;
		while(true){
			browser.simpleWait(5);

			List<WebElement> allRows = findElementsWithoutStale(table, By.tagName("tr")); 
			// And iterate over them, getting the cells 
			for (WebElement row : allRows) { 
				rowText= getText(row);
				if(rowText.contains(unique)){
					List<WebElement> cells = findElementsWithoutStale(row,By.tagName("td")); 
					log.trace("Row Found : {} ", rowText);
					return cells;
				}
			}

			//Next Page
			page++;
			if(isPresent(By.linkText(((page)+"")))){
				click(driver.findElement(By.linkText((page)+"")));
			}else{
				return null;
			}
		}
	}
	public  boolean clickRow(List<WebElement> cells , String button){
		log.trace("Click on Row :{}  value : {} ",cells,button);
		if(cells!=null){
			for (WebElement cell : cells) { 
				String cellText = getText(cell);
				if(cellText.contains(button)){
					click(cell.findElement(By.linkText(button)));
					return true;
				}
			}
			throw new SelTestException(" Button : "+button+" Not Found !");
		}else{
			throw new SelTestException("Cells Are Empty ! ");
		}
	}

	public  boolean clickRow(List<WebElement> cells , int index){
		log.trace("Click on Row :{}  index : {} ",cells,index);
		if(cells!=null){
			click(cells.get(index-1));
			return true;
		}else{
			throw new SelTestException("Cells Are Empty ! ");
		}
	}

	private  List<WebElement> findElementsWithoutStale(WebElement element , By by){
		int retry = 0;
		List<WebElement> toReturn = null;
		while(retry <MAX_RETRY){
			try{
				toReturn = element.findElements(by); 
				break;
			}catch(StaleElementReferenceException ex){
				browser.simpleWait(EXCEPTION_INTERVAL);
				log.debug("Handling Stale Exception in findElementsWithoutStale Method !!! ");
			}finally{
				retry++;
			}
		}
		return toReturn;
	}


	public String getTagName(WebElement element) {
		return element.getTagName();
	}

	public String getAttribute(WebElement element ,String name) {
		return element.getAttribute(name);
	}

	public List<WebElement> findElements(WebElement element ,By by) {
		return element.findElements(by);
	}

	public WebElement findElement(WebElement element ,By by) {
		return element.findElement(by);
	}


	public Point getLocation(WebElement element) {
		return element.getLocation();
	}

	/**
	 * What is the width and height of the rendered element .<br/>
	 *	Returns: The size of the element on the page.
	 * @param element
	 * @return
	 */
	public Dimension getSize(WebElement element) {
		return element.getSize();
	}

	public String getCssValue(WebElement element ,String propertyName) {

		return element.getCssValue(propertyName);
	}

}
