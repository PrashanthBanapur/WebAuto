package org.seltest.driver;

import org.openqa.selenium.WebDriver;
import org.seltest.core.SelTestException;

public class DriverManager {
	private static ThreadLocal<WebDriver> webDriver = new ThreadLocal<WebDriver>();

	public static WebDriver getDriver() {
		WebDriver driver = webDriver.get();
		if(driver==null){
			throw new SelTestException(" Missing driverListener.jar in project classpath");
		}
		return driver;
	}

	public static void setWebDriver(WebDriver driver) {
		webDriver.set(driver);
	}
}
