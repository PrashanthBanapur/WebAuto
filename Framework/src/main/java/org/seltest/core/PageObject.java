package org.seltest.core;

import org.openqa.selenium.WebDriver;

/**
 * All Page Object Should implement this interface
 * @author adityas
 *
 */
public class PageObject {
	protected WebDriver driver;
	protected final static Step STEP = new Step();

	public PageObject(WebDriver driver){
		this.driver=driver;
	}

	/**
	 * Page Url Should be returned<br/>
	 * <b> Note :Should Add Wait before return Url <b/>
	 */
	public String getUrl() {
		System.out.println();
		StepUtil.simpleWait(5);
		return driver.getCurrentUrl().split("\\?")[0];// Remove parameters
	}


	/**
	 * Page Title Should be returned <br/>
	 * <b> Note :Should Add Wait before return Title <b/>
	 * @return
	 */
	public String getTitle() {
		StepUtil.simpleWait(5);
		return driver.getTitle();
	}
}
