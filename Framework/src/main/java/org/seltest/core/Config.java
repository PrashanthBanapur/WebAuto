package org.seltest.core;

import java.io.IOException;
import java.util.Properties;

import org.openqa.selenium.Platform;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class for Loading framework properties
 * 
 * @author adityas
 */

public enum Config {
	/**
	 * Browser used for framework execution
	 */
	runMode, gridUrl, browser, version, platform, baseUrl,userName, driverPath, eventfiring, fullscreen, debug ,captureScreenshot, waitMaxTimeout, exceptionRetry, dbDriver, dbUrl, dbUsername, dbPassword, ;

	private static final String PATH = "framework.properties";
	private static final Logger log = LoggerFactory.getLogger(Config.class);
	private static Properties property;
	private String value;

	private void init() {

		initProperty();
		// Add To Override Config from Command Line
		String cmdValue =System.getProperty(this.toString());		
		if(cmdValue!= null){
			log.info("Overriding Config : {} using Command Line Value {} ",this.toString(),cmdValue);
			value = cmdValue;
		}else{
			value = (String) property.get(this.toString());

		}
		validate(this, value.toLowerCase());
	}

	public String getValue() {
		if (value == null) {
			log.debug("Config : '{}' Value : '{}' ", this.name(), value);
			init();
		}

		// Driver Path will be in user home
		if (this.equals(driverPath)) {
			value = System.getProperty("user.home") + value;
		}
		return value;
	}

	private void validate(Config config, String val) {

		switch (config) {

		case runMode:

			if (!(val.equals("single") || val.equals("grid"))) {
				throw new SelTestException("Invalid Run Mode !! ");
			}
			break;

		case gridUrl:
			if (!val.contains("http")) {
				throw new SelTestException("Invalid Url Format");
			}
			break;
		case debug :
			log.debug("Debug Value Set At: {} seconds ",val);
			break;
		case browser:
			if (!(val.equals("firefox") || val.equals("chrome")
					|| value.equals("ie") || value.equals("safari"))) {
				throw new SelTestException("Invalid Browser !");
			}

			Platform current = Platform.getCurrent();
			if (val.equals("safari") && !(Platform.MAC.is(current))) {
				throw new SelTestException(
						" Safari Browser Works Only in Mac OS !! ");
			}

			if (val.equals("ie")) {
				if (!Platform.WINDOWS.is(current) && (runMode.getValue().equals("single"))) {
					throw new SelTestException(
							" IE Browser Works Only in Windows OS !!");
				}

				log.warn(" IE Driver need the Browser To Be In Focus !! ");
				log.warn("Please Dont Use Machine !!");
			}

			break;
		case baseUrl:
			if (!val.contains("http")) {
				throw new SelTestException("Invalid Url Format");
			}
			break;
		case userName:
			if(val==null){
				throw new SelTestException("User Name is Blank");
			}
			break;
		case captureScreenshot:
			break;
		case driverPath:
			break;
		case eventfiring:
			if (val.equals("false")) {
				log.warn(" Framework Wont work properly : eventfiring : {} ",
						val);
			}
			break;
		case waitMaxTimeout:
			break;
		case exceptionRetry:
			break;
		case fullscreen:
			break;
		case dbDriver:
			// TODO Add validation
			break;
		case dbPassword:
			break;
		case dbUrl:
			break;
		case dbUsername:
			break;
		default:
			break;
		}

	}

	private void initProperty() {
		if (property == null) {
			property = new Properties();
			try {
				property.load(Config.class.getClassLoader()
						.getResourceAsStream(PATH));
			} catch (IOException e) {
				throw new SelTestException("Unable to Load Resources : " + PATH);
			}
		}

	}

}
