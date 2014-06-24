package org.seltest.test;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.events.AbstractWebDriverEventListener;
import org.seltest.core.Config;
import org.seltest.core.StepUtil;

public class WebEventListener extends AbstractWebDriverEventListener {

	private static final int MAX_EXPLICIT_WAIT = Integer.parseInt(Config.explictWaitMaxTimeout.getValue());
	private static final LoggerUtil logger = LoggerUtil.getLogger();


	public void afterClickOn(WebElement element , WebDriver driver){
		// Nothing need now
	}
	public void beforeFindBy(By by, WebElement element, WebDriver driver) {
		int itr=0;
		int reloadInterval=20;

		while(itr<MAX_EXPLICIT_WAIT){
			try{
				StepUtil.waitElementVisible(driver,by,reloadInterval);
				break;
			}catch(TimeoutException ex){
				StepUtil.reloadPage(driver);
			}finally{
				itr=itr+reloadInterval;
			}
		}
	}


	public void afterNavigateTo(String url, WebDriver driver) {
		logger.web("(NAVIGATE)	-> To Url : "+url);
		StepUtil.waitForPageLoaded(driver);
		ReportUtil.reportWebStep("GO TO ", url , "");
	}

	public void beforeClickOn(WebElement element, WebDriver driver) {
		String elementValue;
		if(element.getAttribute("value")!=null){
			elementValue=element.getAttribute("value");
		}else{
			elementValue=element.getText();
		}
		logger.web("(CLICK ON)	-> Element = '"+elementValue+"'");
		String border =StepUtil.highlightElement(driver, element);
		ReportUtil.reportWebStep("CLICK",elementValue,"");
		StepUtil.unhighlightElement(driver, element,border);

	}

	public void afterChangeValueOf(WebElement element , WebDriver driver){
		String elemValue = element.getAttribute("value");
		String elemName = element.getAttribute("name");
		String elemId = element.getAttribute("id");
		String border=StepUtil.highlightElement(driver, element);
		if(elemId.length()>3){
			logger.web("(CHANGED)	-> Element = '"+elemId+"' New Value = '"+elemValue+"'");
			ReportUtil.reportWebStep("CHANGED",elemId,elemValue);

		}else{
			logger.web("(CHANGED)	-> Element = '"+elemName+"' New Value = '"+elemValue+"'");
			ReportUtil.reportWebStep("CHANGED",elemId,elemValue);
		}
		StepUtil.unhighlightElement(driver, element,border);

	}

	public void beforeChangeValueOf(WebElement element, WebDriver driver) {
		String elemValue = element.getAttribute("value");
		String elemName = element.getAttribute("name");
		String elemId = element.getAttribute("id");
		if(elemId.length()>3){
			logger.web("(CHANGING)	-> Element = '"+elemId+"' Old Value = '"+elemValue+"' ");
		}else {
			logger.web("(CHANGING)	-> Element = '"+elemName+"' Old Value = '"+elemValue+"' ");

		}
	}


	public void onException(Throwable throwable, WebDriver driver) {
		if(		(throwable instanceof NoSuchElementException) ||
				(throwable instanceof StaleElementReferenceException) ||
				(throwable instanceof AssertionError)){
			
			logger.debug("( HANDLED EXCEPTION) 	-> Message = "+throwable.getClass());
			logger.trace(throwable.getMessage());
		}
		else{
			logger.web("(EXCEPTION) 	-> Message = "+throwable.getLocalizedMessage()+" ");
			logger.exception(throwable); // stack trace as a string
			ReportUtil.reportException("EXCEPTION", throwable.getLocalizedMessage().substring(0, 50), "");// TODO Make it Small message
			logger.trace(throwable.getMessage());
		}
	}


}
