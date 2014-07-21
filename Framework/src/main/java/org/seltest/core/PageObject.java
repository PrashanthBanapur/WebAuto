package org.seltest.core;

import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Super class for all Page Object <br/>
 * All Page Object Should implement this interface
 * 
 * @author adityas
 */
public class PageObject {
	protected WebDriver driver;
	private final Logger log = LoggerFactory.getLogger(PageObject.class);
	/**
	 * An Element Class Instance which should be used to interact with the WebElements 
	 */
	protected final Element element = new Element();
	/**
	 * A Browser Class Instance which should be used to interact with browsers
	 */
	protected final Browser browser = new Browser();

	public PageObject(WebDriver driver){
		log.trace("Created Page Object on Driver : {} ",driver.hashCode());
		this.driver=driver;
	}
}
