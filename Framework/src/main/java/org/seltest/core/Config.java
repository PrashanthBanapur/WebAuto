package org.seltest.core;

import java.io.IOException;
import java.util.Properties;

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
	browser,
	baseUrl,
	driverPath,
	eventfiring,
	fullscreen,
	captureScreenshot,
	explictWaitMaxTimeout,
	exceptionRetry,
	defaultWait,
	dbDriver,
	dbUrl,
	dbUsername,
	dbPassword,	
	;

	private static final String PATH = "framework.properties";
	private static final Logger log = LoggerFactory.getLogger(Config.class);
	private static Properties property;
	private String value;

	private void init(){

		initProperty();
		value = (String) property.get(this.toString());
		validate(this,value.toLowerCase());
	}
	public String getValue() {
		if (value == null) {
			init();
		}
		
		// Driver Path will be in user home
		if(this.equals(driverPath)){
			value = System.getProperty("user.home")+value;
		}
		
		log.debug("Config : '{}' Value : '{}' ",this.name(),value);
		return value;
	}

	private void validate(Config config, String val) {

		switch(config){
		case browser :
			if(!(val.equals("firefox")|| val.equals("chrome") || value.equals("ie"))){
				throw new SelTestException("Invalid Browser !");
			}
			break;
		case baseUrl :
			if(!val.contains("http")){
				throw new SelTestException("Invalid Url Format");
			}
			break;
		case captureScreenshot:
			break;
		case driverPath :
			break;
		case eventfiring :
			if(val.equals("false")){
				log.warn(" Framework Wont work properly : eventfiring : {} ",val);
			}
			break;
		case explictWaitMaxTimeout :
			break;
		case exceptionRetry :
			break;
		case defaultWait :
			break;
		case fullscreen :
			break;
		case dbDriver:
			//TODO Add validation
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
		if(property==null){
			property = new Properties();
			try {
				property.load(Config.class.getClassLoader().getResourceAsStream(PATH));
			} catch (IOException e) {
				throw new SelTestException("Unable to Load Resources : "+PATH);
			}
		}

	}

}
