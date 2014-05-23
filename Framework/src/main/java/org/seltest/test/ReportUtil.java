package org.seltest.test;

import org.seltest.core.Config;
import org.seltest.driver.DriverManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import atu.testng.reports.ATUReports;

public class ReportUtil {

	private static final String screenShot = Config.captureScreenshot.getValue();
	private static final Logger log = LoggerFactory.getLogger(ReportUtil.class);
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
			ATUReports.add(desp, msg1,msg2,false);
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
			ATUReports.add(desp, msg1,msg2,false);
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
			ATUReports.add(desp, expected,actual,false);
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

	private synchronized  static void reportWithScreenshot(String msg1 , String msg2 , String msg3){
		try{
			ATUReports.setWebDriver(DriverManager.getDriver());
			ATUReports.add(msg1, msg2,msg3,true);
		}catch(Exception e){
			log.debug("Could Not Capture Screen Shot for Step : {} {} {} ",msg1,msg2,msg3);
		}
	}

}
