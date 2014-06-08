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
	protected final static Step step = new Step();

	public PageObject(WebDriver driver){
		log.debug("Created Page Object on Driver : {} ",driver.hashCode());
		this.driver=driver;
	}

	/**
	 * Page Url Should be returned<br/>
	 * <b> Note :Should Add Wait before return Url <b/>
	 */
	public String getUrl() {
		return driver.getCurrentUrl().split("\\?")[0];// Remove parameters
	}


	/**
	 * Page Title Should be returned <br/>
	 * <b> Note :Should Add Wait before return Title <b/>
	 * @return
	 */
	public String getTitle() {
		return driver.getTitle();
	}
}
