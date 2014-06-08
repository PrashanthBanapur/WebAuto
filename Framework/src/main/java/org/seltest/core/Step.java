package org.seltest.core;

import java.util.List;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import org.openqa.selenium.support.ui.Select;
import org.seltest.driver.DriverManager;

/**
 * Class contains all the method to be used by Pages to driver browser <br/>
 * Most of the methods are wrapper of WebElement Class . With some added
 * features to handle exceptions 
 * @see WebElement
 * @see StaleElementReferenceException
 * @see TimeoutException
 * @author adishi
 *
 */
public class Step {

	Step(){
	}

	/**
	 * Select a Value based on visible text from List 
	 * @param ddList List to select from
	 * @param val Text to select 
	 */
	public void select(WebElement ddList ,String val){
		Select option = new Select(ddList);
		option.selectByVisibleText(val);
	}


	/**
	 * Click on a Checkbox by finding using css
	 * @param element WebElement which has check box
	 */
	public void Checkboxclick(WebElement element) {
		element.findElement(By.cssSelector("input[type='checkbox']")).click();;
	}

	/**
	 * Click on a Checkbox only if its not selected
	 * @param element WebElement which has check box
	 */
	public void checkboxSelect(WebElement element) {
		WebElement checkbox =element.findElement(By.cssSelector("input[type='checkbox']"));
		if(!checkbox.isSelected()){
			checkbox.click();
		}
	}
	/**
	 * Get the Text selected in a drop down list
	 * @param ddList List to find the text      
	 */
	public String getSelectedText(WebElement ddList){
		Select option = new Select(ddList);
		return option.getFirstSelectedOption().getText();
	}


	/**
	 * Clear the Text in a Text Field
	 * @param element
	 * @see WebElement
	 */
	public void clear(WebElement element) {
		element.clear();
	}

	/**
	 * Click on the WebElement
	 * @param element
	 * @see WebElement
	 */
	public void click(WebElement element) {
		element.click();
		StepUtil.waitForPageLoaded(DriverManager.getDriver());
	}

	/**
	 * Click on a WebElement and switch to the new Window
	 * @param element
	 */
	public void clickAndSwitch(WebElement element){

		WebDriver driver = DriverManager.getDriver();
		String winHandleBefore = driver.getWindowHandle();
		click(element);
		Set<String> windows = driver.getWindowHandles();

		for(String window : windows){
			if(!window.equals(winHandleBefore)){
				driver.switchTo().window(window);
			}
		}
	}


	/**
	 * 
	 * @param lstRadioButton List of Radio Buttons
	 * @param val Unique button value
	 */
	public void clickRadioButton(List<WebElement> lstRadioButton, String val) {

		for(WebElement radioBtn : lstRadioButton){
			if(radioBtn.getAttribute("value").equals(val)){
				radioBtn.click();
			}
		}

	}

	/**
	 * Method to Find the text in WebElement
	 * @param element
	 * @return Text in the field
	 */
	public String getText(WebElement element) {
		
		int i =0;
		while(i<Integer.parseInt(Config.explictWaitMaxTimeout.getValue())){
			try{
				String value = element.getAttribute("value");
				String text = element.getText();
				if(value == null && text ==null ){
					StepUtil.simpleWait(1);
				}else if(value != null){				
					return value;
				}else if(text != null){
					return element.getText();
				}
			}catch(StaleElementReferenceException e){
				StepUtil.simpleWait(1);
				if(i%10==0&& i>1){
					WebDriver driver =DriverManager.getDriver();
					StepUtil.reloadPage(driver);
				}
			}finally{
				++i;
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
		return false;
		// TODO implement without calling event firing
	}

	/**
	 * Verify if an Element is Enabled
	 * @param element
	 * @see WebElement
	 */
	public boolean isEnabled(WebElement element) {
		return false;
		// TODO implement without calling event firing

	}

	public boolean isSelected(WebElement element) {
		return false;
		// TODO implement without calling event firing

	}
	/**
	 * Check if WebElement is present in the page
	 * Returns the boolean status
	 * @param
	 */
	public boolean isPresent(By by,WebDriver driver){
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
		element.sendKeys(arg0);

	}

	/**
	 * Submit a Form 
	 * @param element to be click to submit
	 * @see WebElement
	 */
	public void submit(WebElement element) {
		element.submit();
	}

}
