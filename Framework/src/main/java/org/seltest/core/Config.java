package org.seltest.core;

import java.io.File;
import java.io.IOException;
import java.util.Properties;


public enum Config {
	browser,
	baseUrl,
	driverPath,
	eventfiring,
	fullscreen,
	captureScreenshot,
	waitType,
	implicitWait,
	explictWait;

	private static final String PATH = "webapp.properties";
	private static Properties property;
	private String value;

	private void init(){

		if(property==null){
			setAtuReport();
			property = new Properties();
			try {
				property.load(Config.class.getClassLoader().getResourceAsStream(PATH));
			} catch (IOException e) {
				throw new SelTestException("Unable to Load Resources : "+PATH);
			}
		}
		value = (String) property.get(this.toString());
	}
	public String getValue() {
		if (value == null) {
			init();
		}
		return value;
	}

	private static void setAtuReport(){
		String path = new File("./","src/main/resources/atu.properties").getAbsolutePath();
		System.setProperty("atu.reporter.config", path);
	}

}
