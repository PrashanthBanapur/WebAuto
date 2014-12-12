package org.seltest.driver;

import org.openqa.selenium.WebDriver;

public class DriverManager {
	private static int counter =0;
	private static ThreadLocal<WebDriver> webDriver = new ThreadLocal<WebDriver>();
	private static ThreadLocal<Integer> driverCount	= new ThreadLocal<Integer>();

	public static WebDriver getDriver() {
		WebDriver driver = webDriver.get();
		return driver;
	}
	
	public static Integer getCounter(){
		return driverCount.get();
	}

	public static void setWebDriver(WebDriver driver) {
		webDriver.set(driver);
		driverCount.set(counter);
		counter++;
	}
}
