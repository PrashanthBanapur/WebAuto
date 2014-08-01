package org.seltest.driver;

import java.net.MalformedURLException;
import java.net.URL;

import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.seltest.core.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RemoteDriverFactory {

	private static String url;
	private static String browser;
	private static String version;
	private static String platform;
	private static Logger log = LoggerFactory
			.getLogger(RemoteDriverFactory.class);

	static {
		browser = Config.browser.getValue();
		version = Config.version.getValue();
		platform = Config.platform.getValue();
		url = Config.gridUrl.getValue();
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

		DesiredCapabilities capability = new DesiredCapabilities();

		if (platform.equalsIgnoreCase("windows")) {
			capability.setPlatform(Platform.WINDOWS);
		} else if (platform.equalsIgnoreCase("mac")) {
			capability.setPlatform(Platform.MAC);
		} else if (platform.equalsIgnoreCase("linux")) {
			capability.setPlatform(Platform.LINUX);
		}else if (platform.equalsIgnoreCase("vista")){
			capability.setPlatform(Platform.VISTA);
		}

		capability.setBrowserName(browser.toLowerCase());

		if (version.equals("any")) {
			capability.setVersion(version);
		}

		try {
			driver = new RemoteWebDriver(new URL(url), capability);
		} catch (MalformedURLException e) {
			log.error("Invalid URL !!" + url);
			e.printStackTrace();
		}

		return driver;
	}

}
