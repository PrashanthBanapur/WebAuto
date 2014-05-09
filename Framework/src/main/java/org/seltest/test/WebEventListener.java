package org.seltest.test;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.events.AbstractWebDriverEventListener;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.seltest.core.ConfigProperty;
import org.seltest.core.StepUtil;
import org.seltest.driver.DriverListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WebEventListener extends AbstractWebDriverEventListener {

	private final Logger log = LoggerFactory.getLogger("STEP");
	private final int MIN_WAIT=3; // Imp don't reduce the time unless you are sure



	public void afterClickOn(WebElement element , WebDriver driver){
		//Implicit Wait 
		if(ConfigProperty.getWaitType().equals("implicit")){
			StepUtil.simpleWait(ConfigProperty.getImplicitWaitTime());
		}else{
			StepUtil.simpleWait(MIN_WAIT);
		}
	}
	public void beforeFindBy(By by, WebElement element, WebDriver driver) {
		if(ConfigProperty.getWaitType().equals("explicit")){
			StepUtil.waitElement(driver,ExpectedConditions.visibilityOfElementLocated(by));
		}
	}


	public void afterNavigateTo(String url, WebDriver driver) {
		log.info("		|<{}>	-(NAVIGATE)	-> To Url : {}",getTestName(),url);
		StepUtil.simpleWait(MIN_WAIT);
		ReportUtil.reportWebStep("GO TO ", url , "");
	}

	public void beforeClickOn(WebElement element, WebDriver driver) {
		String elementValue;
		if(element.getAttribute("value")!=null){
			elementValue=element.getAttribute("value");
		}else{
			elementValue=element.getText();
		}
		log.info("		|<{}>	-(CLICK ON)	-> Element = '{}' ",getTestName(),elementValue);
		ReportUtil.reportWebStep("CLICK",elementValue,"");

	}

	public void afterChangeValueOf(WebElement element , WebDriver driver){
		String elemValue = element.getAttribute("value");
		String elemName = element.getAttribute("name");
		String elemId = element.getAttribute("id");
		if(elemId.length()>3){
			log.info("		|<{}>	-(CHANGED)	-> Element = '{}' New Value = '{}' ",getTestName(),elemId,elemValue);
			ReportUtil.reportWebStep("CHANGED",elemId,elemValue);

		}else{
			log.info("		|<{}>	-(CHANGED)	-> Element = '{}' New Value = '{}' ",getTestName(),elemName,elemValue);
			ReportUtil.reportWebStep("CHANGED",elemId,elemValue);

		}
	}

	public void beforeChangeValueOf(WebElement element, WebDriver driver) {
		String elemValue = element.getAttribute("value");
		String elemName = element.getAttribute("name");
		String elemId = element.getAttribute("id");
		if(elemId.length()>3){
			log.info("		|<{}>	-(CHANGING)	-> Element = '{}' Old Value = '{}' ",getTestName(),elemId,elemValue);
		}else {
			log.info("		|<{}>	-(CHANGING)	-> Element = '{}' Old Value = '{}' ",getTestName(),elemName,elemValue);

		}
	}


	public void onException(Throwable throwable, WebDriver driver) {
		if(!(throwable instanceof NoSuchElementException) ){
			log.error("		|<{}>	-(EXCEPTION) 	-> Message = {} ",getTestName(),throwable.getLocalizedMessage());
			ReportUtil.reportException("EXCEPTION", throwable.getLocalizedMessage().substring(0, 50), "");// TODO Make it Small message
		}
	}

	private String getTestName(){
		String name = DriverListener.getTestName();
		if(name!=null)
			return name;
		else
			return "config";
	}

}
