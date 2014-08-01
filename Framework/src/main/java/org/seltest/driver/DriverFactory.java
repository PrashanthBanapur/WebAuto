package org.seltest.driver;

import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.firefox.internal.ProfilesIni;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.seltest.core.Config;
import org.seltest.test.LoggerUtil;

final class DriverFactory {

	private static String browser;
	private static String driverPath;

	private static final LoggerUtil logger = LoggerUtil.getLogger();

	static {
		browser = Config.browser.getValue();
		driverPath = Config.driverPath.getValue();
		System.setProperty("org.apache.commons.logging.Log",
				"org.apache.commons.logging.impl.SimpleLog");
		System.setProperty(
				"org.apache.commons.logging.simplelog.log.org.apache.http",
				"warn");
	}

	/**
	 * Method to get WebDriver based on app properties
	 */
	static WebDriver getDriver() {
		WebDriver driver = null;
		if (browser.equalsIgnoreCase("FIREFOX")) {
			ProfilesIni profile = new ProfilesIni();
			FirefoxProfile ffprofile = profile.getProfile("default");
			driver = new FirefoxDriver(ffprofile);

		} else if (browser.equalsIgnoreCase("CHROME")) {
			System.setProperty("webdriver.chrome.driver", driverPath
					+ "/chromedriver" + getExtension());
			try {
				driver = new ChromeDriver();
			} catch (IllegalStateException ex) {
				logger.exception(ex);
			}

		} else if (browser.equalsIgnoreCase("ANDROID")) {
			driver = new RemoteWebDriver(DesiredCapabilities.android());

		} else if (browser.equalsIgnoreCase("IE")) {
			System.setProperty("webdriver.ie.driver", driverPath + "/iedriver"
					+ getExtension());
			try {
				driver = new InternetExplorerDriver();
			} catch (IllegalStateException ex) {
				logger.exception(ex);
			}
		} else if (browser.equalsIgnoreCase("SAFARI")) {
			System.setProperty("webdriver.safari.driver", driverPath
					+ "/safaridriver" + getExtension());
			try {
				driver = new SafariDriver();
			} catch (IllegalStateException ex) {
				logger.exception(ex);
			}
		}

		return driver;

	}

	// Return Extension based on OS
	private static String getExtension() {
		Platform current = Platform.getCurrent();

		if (Platform.MAC.is(current)) {
			return "";
		} else if (Platform.WINDOWS.is(current)) {
			return ".exe";
		} else if (Platform.LINUX.is(current)) {
			return "";
		} else {
			return "";
		}
	}

}
