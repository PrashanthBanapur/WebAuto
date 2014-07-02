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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WebEventListener extends AbstractWebDriverEventListener {

	private static final int MAX_RETRY = Integer.parseInt(Config.exceptionRetry.getValue());
	private static final int DEFAULT_WAIT = Integer.parseInt(Config.defaultWait.getValue());
	private static final Logger log = LoggerFactory.getLogger(WebEventListener.class);
	private static final LoggerUtil logger = LoggerUtil.getLogger();


	public void afterClickOn(WebElement element , WebDriver driver){
		log.trace("After Click On : {} ",element);
		/*NOTE : Dont Add any check here 
		 * as POP will fail if some action is done after click
		 */
	}
	public void beforeFindBy(By by, WebElement element, WebDriver driver) {
		int retry=0;
		log.trace("Before Find By :{} ",by);
		while(retry<MAX_RETRY){
			try{
				StepUtil.waitElementVisible(driver,by,DEFAULT_WAIT);
				break;
			}catch(TimeoutException ex){
				if(retry==MAX_RETRY/2){
					StepUtil.reloadPage(driver);
				}
			}finally{
				retry++;
			}
		}
	}

	public void afterFindBy(By by, WebElement element, WebDriver driver) {
		log.trace("After Find By : {} ",by);
	}

	public void afterNavigateTo(String url, WebDriver driver) {
		log.info(LoggerUtil.webFormat()+"(NAVIGATE)	-> To Url : {} ",url);
		StepUtil.waitForPageLoaded(driver);
		ReportUtil.reportWebStep(null,"GO TO ", url , "");
	}

	public void beforeClickOn(WebElement element, WebDriver driver) {
		String elementValue;
		if(element.getAttribute("value")!=null){
			elementValue=element.getAttribute("value");
		}else{
			elementValue=element.getText();
		}
		log.info(LoggerUtil.webFormat()+"(CLICK ON)	-> Element = '{}'",elementValue);
		ReportUtil.reportWebStep(element,"CLICK",elementValue,"");

	}

	public void afterChangeValueOf(WebElement element , WebDriver driver){
		String elemValue = element.getAttribute("value");
		String elemName = element.getAttribute("name");
		String elemId = element.getAttribute("id");
		if(elemId.length()>3){
			log.info(LoggerUtil.webFormat()+"(CHANGED)	-> Element = '"+elemId+"' New Value = '{}'",elemValue);
			ReportUtil.reportWebStep(element,"CHANGED",elemId,elemValue);

		}else{
			log.info(LoggerUtil.webFormat()+"(CHANGED)	-> Element = '"+elemName+"' New Value = '{}'",elemValue);
			ReportUtil.reportWebStep(element ,"CHANGED",elemId,elemValue);
		}

	}

	public void beforeChangeValueOf(WebElement element, WebDriver driver) {
		String elemValue = element.getAttribute("value");
		String elemName = element.getAttribute("name");
		String elemId = element.getAttribute("id");
		if(elemId.length()>3){
			log.info(LoggerUtil.webFormat()+"(CHANGING)	-> Element = '"+elemId+"' Old Value = '{}' ",elemValue);
		}else {
			log.info(LoggerUtil.webFormat()+"(CHANGING)	-> Element = '"+elemName+"' Old Value = '{}' ",elemValue);

		}
	}


	public void onException(Throwable throwable, WebDriver driver) {
		if(		(throwable instanceof NoSuchElementException) ||
				(throwable instanceof StaleElementReferenceException) ||
				(throwable instanceof AssertionError)){

			log.debug("( HANDLED EXCEPTION) 	-> Message = "+throwable.getClass());
			log.trace(throwable.getMessage());
		}
		else{
			log.info(LoggerUtil.webFormat()+"(EXCEPTION) 	-> Message = "+throwable.getLocalizedMessage()+" ");
			logger.exception(throwable); // stack trace as a string
			ReportUtil.reportException("EXCEPTION", throwable.getLocalizedMessage().substring(0, 50), "");// TODO Make it Small message
			log.trace(throwable.getMessage());
		}
	}


}
