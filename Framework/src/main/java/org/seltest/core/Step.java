package org.seltest.core;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import org.openqa.selenium.support.ui.Select;


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
	 * Get the Text selected in a drop down list
	 * @param ddList List to find the text      
	 */
	public static String getSelectedText(WebElement ddList){
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
		String value = element.getAttribute("value");
		if(value!= null){
			return value;
		}else{
			return element.getText();
		}
	}

	/**
	 * Verify if WebElement is Displayed
	 * @param element
	 * @see WebElement
	 */
	public boolean isDisplayed(WebElement element) {
		if(element.isDisplayed()){
			return true;
		} else{
			return false;
		}
	}

	/**
	 * Veirfy if an Element is Enabled
	 * @param element
	 * @see WebElement
	 */
	public boolean isEnabled(WebElement element) {
		if(element.isEnabled()){
			return true;
		}else {
			return false;
		}
	}

	public boolean isSelected(WebElement element) {
		if(element.isSelected()){
			return true;
		}else {
			return false;
		}
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
