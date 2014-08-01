package org.seltest.test;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.events.AbstractWebDriverEventListener;
import org.seltest.core.Browser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WebEventListener extends AbstractWebDriverEventListener {

	private static final Logger log = LoggerFactory
			.getLogger(WebEventListener.class);
	private static final LoggerUtil logger = LoggerUtil.getLogger();
	private final Browser browser = new Browser();

	public void afterClickOn(WebElement element, WebDriver driver) {
		log.trace("After Click On : {} ", element);
		/*
		 * NOTE : Dont Add any check here as Alert will fail if some action is
		 * done after click
		 */
	}

	public void beforeFindBy(By by, WebElement element, WebDriver driver) {
		log.trace("Before Find By :{} ", by);
	}

	public void afterFindBy(By by, WebElement element, WebDriver driver) {
		log.trace("After Find By : {} ", by);
	}

	public void afterNavigateTo(String url, WebDriver driver) {
		log.info(LoggerUtil.webFormat() + "(NAVIGATE)	-> To Url : {} ", url);
		browser.waitForPageLoaded();
		ReportUtil.reportWebStep(null, "GO TO ", url, "");
	}

	public void beforeClickOn(WebElement element, WebDriver driver) {
		String elemIdentity = getElementIdentity(element);
		log.info(LoggerUtil.webFormat() + "(CLICK ON)	-> Element = '{}'",
				elemIdentity);
		ReportUtil.reportWebStep(element, "CLICK", elemIdentity, "");

	}

	public void afterChangeValueOf(WebElement element, WebDriver driver) {
		String elemValue = element.getAttribute("value");
		String elemIdentity = getElementIdentity(element);
		log.info(LoggerUtil.webFormat() + "(CHANGED)	-> Element = '"
				+ elemIdentity + "' New Value = '{}'", elemValue);
		ReportUtil.reportWebStep(element, "CHANGED", elemIdentity, elemValue);

	}

	public void beforeChangeValueOf(WebElement element, WebDriver driver) {
		String elemValue = element.getAttribute("value");
		String elemIdentity = getElementIdentity(element);
		log.info(LoggerUtil.webFormat() + "(CHANGING)	-> Element = '"
				+ elemIdentity + "' Old Value = '{}' ", elemValue);

	}

	public void onException(Throwable throwable, WebDriver driver) {
		if ((throwable instanceof NoSuchElementException)
				|| (throwable instanceof StaleElementReferenceException)
				|| (throwable instanceof AssertionError)) {

			log.debug("( HANDLED EXCEPTION) 	-> Message = "
					+ throwable.getClass());
			log.trace(throwable.getMessage());
		} else {
			log.info(LoggerUtil.webFormat() + "(EXCEPTION) 	-> Message = "
					+ throwable.getLocalizedMessage() + " ");
			logger.exception(throwable); // stack trace as a string
			ReportUtil.reportException("EXCEPTION", throwable
					.getLocalizedMessage().substring(0, 50), "");// TODO Make it
																	// Small
																	// message
			log.trace(throwable.getMessage());
		}
	}

	private String getElementIdentity(WebElement element) {

		String elemId = element.getAttribute("id");
		String elemClass = element.getAttribute("class");
		String elemName = element.getAttribute("name");
		String elemText = element.getText();
		String elementIdentity = "";

		if ((elemId != null) && (!elemId.isEmpty())) {
			elementIdentity = elemId;
		} else if ((elemClass != null) && (!elemClass.isEmpty())) {
			elementIdentity = elemClass;
		} else if ((elemName != null) && (!elemName.isEmpty())) {
			elementIdentity = elemName;
		} else if ((elemText != null) && (!elemText.isEmpty())) {
			elementIdentity = elemText;
		}

		return elementIdentity;

	}

}
