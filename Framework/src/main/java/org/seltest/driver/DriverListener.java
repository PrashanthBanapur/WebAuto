/**
 * 
 */
package org.seltest.driver;

import org.openqa.selenium.WebDriver;
import org.seltest.core.StepUtil;
import org.seltest.test.ReportUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ISuite;
import org.testng.ISuiteListener;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.TestListenerAdapter;

import atu.testng.reports.ATUReports;

/**
 * Class for logging TestNg Events
 * @author adityas
 *
 */
public class DriverListener  extends TestListenerAdapter implements ISuiteListener{

	private static ThreadLocal<String> testName = new ThreadLocal<String>();
	private Logger test = LoggerFactory.getLogger("TEST");
	private Logger conf = LoggerFactory.getLogger("CONFIG");
	private static String parallelMode;
	private static Boolean suiteCalled = false; //TODO To avoid calling suite listeners twice


	public static String getTestName() {
		return testName.get();
	}

	public static void setTestName(String name) {
		testName.set(name);
	}

	@Override
	public void onTestStart(ITestResult result) {
		test.info("	(START)	-> Test Case : {} ",result.getName());
		setTestName(result.getName());
		super.onTestStart(result);
	}

	@Override
	public void onTestSuccess(ITestResult result) {
		test.info("	(SUCCESS)	-> Test Case : {} ",result.getName());
		ReportUtil.reportResult("SUCCESS", result.getName(), "");
		super.onTestSuccess(result);
	}

	@Override
	public void onTestFailure(ITestResult result) {
		test.info("	(FAIL)	-> Test Case : {} ",result.getName());
		ReportUtil.reportResult("FAIL", result.getName(), "");
		super.onTestFailure(result);

	}

	@Override
	public void onTestSkipped(ITestResult result) {
		test.info("	(SKIPPED)	-> Test Case : {} ",result.getName());
		ReportUtil.reportResult("SKIP", result.getName(), "");
		super.onTestSkipped(result);

	}

	@Override
	public void beforeConfiguration(ITestResult result) {
		conf.info("	(START)	-> Config Name : {} ",result.getName());
		super.onConfigurationFailure(result);
	}

	@Override
	public void onConfigurationFailure(ITestResult result){
		conf.info("	(FAIL)	-> Config Name : {} ",result.getName());
		super.onConfigurationFailure(result);
	}

	@Override
	public void onConfigurationSkip(ITestResult result){
		conf.info("	(SKIPPED)	-> Config Name : {} ",result.getName());
		super.onConfigurationSkip(result);
	}
	@Override
	public void onConfigurationSuccess(ITestResult result){
		conf.info("	(SUCCESS)	-> Config Name : {} ",result.getName());
		super.onConfigurationSuccess(result);
	}

	@Override
	public void onStart(ITestContext context) {
		conf.info("	(START)	 -> Tests Name : {} ",context.getName()); 
		if(parallelMode.equals("tests")){
			createWebDriver();
		}
	}

	@Override
	public void onFinish(ITestContext context) {
		conf.info("	(FINISHED)	 -> Tests Name : {} ",context.getName()); 
		if(parallelMode.equals("tests")){
			quitWebDriver();
		}

	}
	@Override
	public void onStart(ISuite suite) {
		
		parallelMode=suite.getParallel().toLowerCase();// Get parallel mode
		
		if(!suiteCalled){
			conf.info("");
			conf.info("	******* STARTED TEST SUITE ******");
			conf.info("	");
			if(!parallelMode.equals("tests")){ // Only Tests supported
				createWebDriver();
			}
			suiteCalled=true;
		}
	}

	@Override
	public void onFinish(ISuite suite) {
		if(suiteCalled){
			conf.info("	");
			conf.info("	****** FINISHED TEST SUITE ******");
			conf.info("	");
			if(!parallelMode.equals("tests")){
				quitWebDriver();
			}
			suiteCalled=false;
		}
	}

	private synchronized void createWebDriver(){
		WebDriver driver = DriverFactory.getDriver();
		DriverManager.setWebDriver(driver);	
		ATUReports.setWebDriver(DriverManager.getDriver());
	}

	private synchronized void quitWebDriver(){
		WebDriver driver = DriverManager.getDriver();
		StepUtil.waitImplicit(driver ,5);
		if (driver != null) {
			driver.quit();
		}

	}

}
