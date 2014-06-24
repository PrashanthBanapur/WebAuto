package org.seltest.test;

import org.openqa.selenium.WebDriver;
import org.seltest.core.Config;
import org.seltest.driver.DriverManager;

import atu.testng.reports.ATUReports;
import atu.testng.reports.logging.LogAs;
import atu.testng.selenium.reports.CaptureScreen;
import atu.testng.selenium.reports.CaptureScreen.ScreenshotOf;

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
			reportWithScreenshot(desp, msg1,msg2,LogAs.INFO);
		}else {
			reportWithoutScreenShot(desp, msg1,msg2,LogAs.INFO);
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
			reportWithScreenshot(desp, msg1,msg2,LogAs.PASSED);
		}else {
			reportWithoutScreenShot(desp, msg1,msg2,LogAs.PASSED);
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
			reportWithScreenshot(desp, expected,actual ,LogAs.INFO);
		}else {
			reportWithoutScreenShot(desp, expected, actual,LogAs.INFO);
		}
	}

	/**
	 * Report Exceptions at Runtime
	 * @param desp
	 * @param msg1
	 * @param msg2
	 */
	static void reportException(String desp,String msg1 , String msg2){
		reportWithScreenshot(desp, msg1,msg2 , LogAs.FAILED);
	}

	private static void reportWithoutScreenShot(String msg1,String msg2,String msg3,LogAs logType){
		try{
			ATUReports.add(msg1,msg2, logType, new CaptureScreen(
					ScreenshotOf.DESKTOP));
		}catch(Exception e){
			// TODO
		}
	}

	private static synchronized void reportWithScreenshot(String msg1 , String msg2 , String msg3 ,LogAs logType){
		WebDriver driver = DriverManager.getDriver();
		if(driver==null){
			reportWithoutScreenShot(msg1, msg2, msg3 ,logType);
		}else{

			try{
			ATUReports.setWebDriver(driver);
			ATUReports.add(msg1,msg2,logType, new CaptureScreen(
					ScreenshotOf.BROWSER_PAGE));
			}catch(Exception e){
				logger.trace("Screen Shot not captured : "+msg2);
				logger.trace(e.toString());
			}
		}
	}

}
