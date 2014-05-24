package org.seltest.driver;

import java.io.File;
import java.lang.annotation.Annotation;

import org.openqa.selenium.WebDriver;
import org.seltest.core.StepUtil;
import org.seltest.core.TestCase;
import org.seltest.core.TestInfo;
import org.seltest.test.ReportUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ISuite;
import org.testng.ITestContext;
import org.testng.ITestResult;

import atu.testng.reports.ATUReports;

/**
 * Class contains all the implementation in driver listener . So as to avoid rebuilding jar 
 * after every change in DriverListener class
 * @author adityas
 *
 */
public class ListenerHelper {
	
	private Logger test = LoggerFactory.getLogger("TEST");
	private Logger conf = LoggerFactory.getLogger("CONFIG");
	private static String parallelMode;
	private static Boolean suiteCalled = false; //TODO To avoid calling suite listeners twice

	// restricting to Package Access
	 ListenerHelper(){
		
	}
	
	public void onTestStart(ITestResult result) {
		test.info("	(START)	-> Test Case : {} ",result.getName());
		TestCase.setTestName(result.getName());
		
	
		Class<?> testClass = result.getTestClass().getRealClass();//TODO Change ?
		
		if(testClass.isAnnotationPresent(TestInfo.class)){
			Annotation annotation =testClass.getAnnotation(TestInfo.class);
			TestInfo testInfo = (TestInfo) annotation;
	 		
			TestCase.setAuthor(testInfo.author());
			TestCase.setDate(testInfo.lastModified());
			TestCase.setVersion(testInfo.version());
		}
	}

	
	public void onTestSuccess(ITestResult result) {
		test.info("	(SUCCESS)	-> Test Case : {} ",result.getName());
		ReportUtil.reportResult("SUCCESS", result.getName(), "");
		setTestInfo();
	}

	
	public void onTestFailure(ITestResult result) {
		test.info("	(FAIL)	-> Test Case : {} ",result.getName());
		ReportUtil.reportResult("FAIL", result.getName(), "");
		setTestInfo();

	}

	
	public void onTestSkipped(ITestResult result) {
		test.info("	(SKIPPED)	-> Test Case : {} ",result.getName());
		ReportUtil.reportResult("SKIP", result.getName(), "");
		setTestInfo();

	}

	
	public void beforeConfiguration(ITestResult result) {
		conf.info("	(START)	-> Config Name : {} ",result.getName());
	}

	
	public void onConfigurationFailure(ITestResult result){
		conf.info("	(FAIL)	-> Config Name : {} ",result.getName());
	}

	
	public void onConfigurationSkip(ITestResult result){
		conf.info("	(SKIPPED)	-> Config Name : {} ",result.getName());
	}
	
	public void onConfigurationSuccess(ITestResult result){
		conf.info("	(SUCCESS)	-> Config Name : {} ",result.getName());
	}

	
	public void onStart(ITestContext context) {
		conf.info("	(START)	 -> Tests Name : {} ",context.getName()); 
		if(parallelMode.equals("tests")){
			createWebDriver();
		}
	}

	
	public void onFinish(ITestContext context) {
		conf.info("	(FINISHED)	 -> Tests Name : {} ",context.getName()); 
		if(parallelMode.equals("tests")){
			quitWebDriver();
		}

	}
	
	public void onStart(ISuite suite) {
		
		parallelMode=suite.getParallel().toLowerCase();// Get parallel mode
		
		if(!suiteCalled){
			conf.info("");
			conf.info("	******* STARTED {} ******",suite.getName().toUpperCase());
			conf.info("	");
			String path = new File("./","src/main/resources/atu.properties").getAbsolutePath();
			System.setProperty("atu.reporter.config", path);
			conf.debug(" ATU Report Set ");
			if(!parallelMode.equals("tests")){ // Only Tests supported
				createWebDriver();
			}
			suiteCalled=true;
		}
	}

	
	public void onFinish(ISuite suite) {
		if(suiteCalled){
			conf.info("	");
			conf.info("	****** FINISHED {}  ******",suite.getName().toUpperCase());
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
		StepUtil.waitImplicit(5);
		if (driver != null) {
			driver.quit();
		}

	}
	
	private synchronized void setTestInfo(){
		
		if(TestCase.getAuthor()!=null){
		ATUReports.setAuthorInfo(TestCase.getAuthor(), TestCase.getDate(), TestCase.getVersion());
		}
	}

}
