package org.seltest.core;

import java.io.IOException;
import java.util.Properties;

import org.seltest.test.LoggerUtil;


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
	waitType,
	implicitWait,
	explictWaitMaxTimeout,
	dbDriver,
	dbUrl,
	dbUsername,
	dbPassword,	
	;

	private static final String PATH = "framework.properties";
	private static final LoggerUtil logger = LoggerUtil.getLogger();
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
			logger.debug("Config : '{}' Value : '{}' ",this.name(),value);
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
				logger.warn(" Framework Wont work properly : eventfiring : {} ",val);
			}
			break;
		case explictWaitMaxTimeout :
			break;
		case fullscreen :
			break;
		case implicitWait :
			if(Integer.parseInt(val)<3){
				logger.warn(" Script may fail if implicit wait is less then 3 sec ");
			}
			break;
		case  waitType :
			if(val.equals("implicit"))
				logger.warn(" explict wait improves execution time by 30 %");
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
