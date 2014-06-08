package org.seltest.test;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.concurrent.TimeUnit;

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

	private final Logger log = LoggerFactory.getLogger("STEP");
	//	private static final int MIN_WAIT=3; // Imp don't reduce the time unless you are sure
	private static final String WAIT_TYPE = Config.waitType.getValue();
	private static final int IMPLICIT_WAIT_TIME = Integer.parseInt(Config.implicitWait.getValue());
	private static final int MAX_EXPLICIT_WAIT = Integer.parseInt(Config.explictWaitMaxTimeout.getValue());
	private static final LoggerUtil logger = LoggerUtil.getLogger();


	public void afterClickOn(WebElement element , WebDriver driver){
		//Implicit Wait 
		if(WAIT_TYPE.equals("implicit")){
			StepUtil.simpleWait(IMPLICIT_WAIT_TIME);
		}
		//		else{
		//			StepUtil.simpleWait(MIN_WAIT);
		//		}
	}
	public void beforeFindBy(By by, WebElement element, WebDriver driver) {
		int i=0;
		if(WAIT_TYPE.equals("explicit")){
			while(i<MAX_EXPLICIT_WAIT){
				try{
					StepUtil.waitElementVisible(driver,by,10);
					break;
				}catch(TimeoutException ex){
					StepUtil.reloadPage(driver);
				}finally{
					i=i+10;
				}
			}
		}
	}


	public void afterNavigateTo(String url, WebDriver driver) {
		logger.web("(NAVIGATE)	-> To Url : "+url);
		//		StepUtil.simpleWait(MIN_WAIT);
		driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
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
		if((throwable instanceof NoSuchElementException) || (throwable instanceof StaleElementReferenceException)){
			logger.web("( HANDLED EXCEPTION) 	-> Message = "+throwable.getClass());
		}
		else{
			logger.web("(EXCEPTION) 	-> Message = "+throwable.getLocalizedMessage()+" ");
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			throwable.printStackTrace(pw);
			log.error(sw.toString()); // stack trace as a string
			ReportUtil.reportException("EXCEPTION", throwable.getLocalizedMessage().substring(0, 50), "");// TODO Make it Small message
		}
	}


}
