package org.seltest.test;

import org.openqa.selenium.WebDriver;
import org.seltest.core.Config;
import org.seltest.driver.DriverManager;


import atu.testng.reports.ATUReports;

public class ReportUtil {

	private static final String screenShot = Config.captureScreenshot.getValue();
	private static final LoggerUtil logger = LoggerUtil.getLogger();
	/**
	 * Report result of test case
	 * @param desp
	 * @param msg1
	 * @param msg2
	 */
	public static void reportResult(String desp , String msg1 , String msg2 ){

		if(screenShot.equals("all")||screenShot.equals("result")){
			reportWithScreenshot(desp, msg1,msg2);
		}else {
			reportWithoutScreenShot(desp, msg1,msg2);
		}

	}
	/**
	 * Report Web Event 
	 * @param desp
	 * @param msg1
	 * @param msg2
	 */
	static void reportWebStep(String desp , String msg1 , String msg2){

		if(screenShot.equals("all")){
			reportWithScreenshot(desp, msg1,msg2);
		}else {
			reportWithoutScreenShot(desp, msg1,msg2);
		}

	}
	/**
	 * Report Test case Assertions
	 * @param desp
	 * @param actual
	 * @param exp
	 */
	public static void reportAssert(String desp , String expected , String actual ){
		if(expected==null){
			expected="";
		}if(desp==null){
			desp="";
		}if(actual==null){
			actual="";
		}
		if(screenShot.equals("assertion")||screenShot.equals("all")||screenShot.equals("result")){
			reportWithScreenshot(desp, expected,actual);
		}else {
			reportWithoutScreenShot(desp, expected, actual);
		}
	}

	/**
	 * Report Exceptions at Runtime
	 * @param desp
	 * @param msg1
	 * @param msg2
	 */
	static void reportException(String desp,String msg1 , String msg2){
		reportWithScreenshot(desp, msg1,msg2);
	}

	private static void reportWithoutScreenShot(String msg1,String msg2,String msg3){
		try{
			ATUReports.add(msg1, msg2,msg3,false);
		}catch(Exception e){
			// TODO
		}
	}

	private static void reportWithScreenshot(String msg1 , String msg2 , String msg3){
		WebDriver driver = DriverManager.getDriver();
		if(driver==null){
			reportWithoutScreenShot(msg1, msg2, msg3);
		}else{

			try{
			ATUReports.setWebDriver(driver);
			ATUReports.add(msg1, msg2,msg3,true);
			}catch(Exception e){
				logger.debug("Screen Shot not captured : "+msg2);
			}
		}
	}

}
