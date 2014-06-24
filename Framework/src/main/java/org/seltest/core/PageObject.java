package org.seltest.core;

import org.openqa.selenium.WebDriver;
import org.seltest.test.LoggerUtil;

/**
 * Super class for all Page Object <br/>
 * All Page Object Should implement this interface
 * 
 * @author adityas
 */
public class PageObject {
	protected WebDriver driver;
	private final LoggerUtil logger = LoggerUtil.getLogger();
	protected final static Step step = new Step();

	public PageObject(WebDriver driver){
		logger.trace("Created Page Object on Driver : {} ",driver.hashCode());
		this.driver=driver;
	}
}
