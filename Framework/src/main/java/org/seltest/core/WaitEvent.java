package org.seltest.core;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.seltest.driver.DriverManager;
import org.seltest.test.LoggerUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WaitEvent {

	private final int MAX_WAIT = Integer.parseInt(Config.waitMaxTimeout
			.getValue());
	private final Logger log = LoggerFactory.getLogger(WaitEvent.class);

	/**
	 * An expectation for checking that an element, known to be present on the
	 * DOM of a page, is visible. Visibility means that the element is not only
	 * displayed but also has a height and width that is greater than 0.
	 * 
	 * @param element
	 */
	public synchronized void visible(WebElement element) {
		log.info(LoggerUtil.webFormat() + "(WAIT VISIBLE)	-> Element = '{}' ",
				LoggerUtil.getElementIdentity(element));
		wait(ExpectedConditions.visibilityOf(element));
	}

	/**
	 * An expectation with the logical opposite condition of the given
	 * condition. <br/>
	 * Condition, Element known to be present on the DOM of a page, is visible.
	 * Visibility means that the element is not only displayed but also has a
	 * height and width that is greater than 0.
	 * 
	 * @param element
	 */
	public synchronized void notVisible(WebElement element) {
		log.info(LoggerUtil.webFormat()
				+ "(WAIT NOT VISIBLE)	-> Element = '{}' ", LoggerUtil.getElementIdentity(element));
		wait(ExpectedConditions.not(ExpectedConditions.visibilityOf(element)));
	}

	/**
	 * An expectation for checking that an element is present on the DOM of a
	 * page. This does not necessarily mean that the element is visible
	 * 
	 * @param locator
	 */
	public synchronized void present(By locator) {
		log.info(LoggerUtil.webFormat() + "(WAIT  PRESENT)	-> Element = '{}' ",
				locator);
		wait(ExpectedConditions.presenceOfElementLocated(locator));
	}

	/**
	 * An expectation with the logical opposite condition of the given
	 * condition. <br/>
	 * Condition , Element is present on the DOM of a page. This does not
	 * necessarily mean that the element is visible
	 * 
	 * @param locator
	 */
	public synchronized void notPresent(By locator) {
		log.info(LoggerUtil.webFormat()
				+ "(WAIT NOT PRESENT)	-> Element = '{}' ", locator);
		wait(ExpectedConditions.not(ExpectedConditions
				.presenceOfElementLocated(locator)));
	}

	/**
	 * An expectation for checking an element is visible and enabled such that
	 * you can click it.
	 * 
	 * @param element
	 */
	public synchronized void clickable(WebElement element) {
		log.info(LoggerUtil.webFormat()
				+ "(WAIT  CLICKABLE)	-> Element = '{}' ", LoggerUtil.getElementIdentity(element));
		wait(ExpectedConditions.elementToBeClickable(element));
	}

	/**
	 * An expectation with the logical opposite condition of the given
	 * condition. <br/>
	 * Condition , Element is visible and enabled such that you can click it.
	 * 
	 * @param element
	 */
	public synchronized void notClickable(WebElement element) {
		log.info(LoggerUtil.webFormat()
				+ "(WAIT NOT CLICKABLE)	-> Element = '{}' ", LoggerUtil.getElementIdentity(element));
		wait(ExpectedConditions.not(ExpectedConditions
				.elementToBeClickable(element)));
	}

	private synchronized void wait(ExpectedCondition<?> expectedCondition) {
		WebDriver driver = DriverManager.getDriver();
		WebDriverWait wait = new WebDriverWait(driver, MAX_WAIT);
		wait.until(expectedCondition);
		wait.pollingEvery(1, TimeUnit.SECONDS);
		wait.ignoring(NoSuchElementException.class);
		wait.ignoring(StaleElementReferenceException.class);
	}
}
