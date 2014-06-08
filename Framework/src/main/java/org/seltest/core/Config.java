package org.seltest.core;

import java.io.IOException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Framework Configurations will be loaded from the property file
 * @author adityas
 *
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
	waitType,
	implicitWait,
	explictWaitMaxTimeout,
	dbDriver,
	dbUrl,
	dbUsername,
	dbPassword,	
	;

	private static final String PATH = "config.properties";
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
		case fullscreen :
			break;
		case implicitWait :
			if(Integer.parseInt(val)<3){
				log.warn(" Script may fail if implicit wait is less then 3 sec ");
			}
			break;
		case  waitType :
			if(val.equals("implicit"))
				log.warn(" explict wait improves execution time by 30 %");
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
