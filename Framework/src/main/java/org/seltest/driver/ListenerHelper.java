package org.seltest.driver;

import java.io.File;
import java.lang.annotation.Annotation;

import org.openqa.selenium.WebDriver;
import org.seltest.core.SelTestException;
import org.seltest.core.TestCase;
import org.seltest.core.TestInfo;
import org.seltest.test.LoggerUtil;
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

	private Logger log = LoggerFactory.getLogger(ListenerHelper.class);
	private static String parallelMode;
	private static Boolean suiteCalled = false; //TODO To avoid calling suite listeners twice

	// restricting to Package Access
	ListenerHelper(){

	}

	public void onTestStart(ITestResult result) {
		TestCase.setTestName(result.getName());
		log.info(LoggerUtil.testFormat()+"(START)	-> Test Case : {} ", result.getMethod().getMethodName());
		processAnnotation(result);

	}

	public void onTestSuccess(ITestResult result) {
		log.info(LoggerUtil.testFormat()+"(SUCCESS)	-> Test Case : {} ", result.getMethod().getMethodName());
		setTestInfo();
		ReportUtil.reportResult("SUCCESS", result.getName(), "");
	}

	public void onTestFailure(ITestResult result) {
		log.info(LoggerUtil.testFormat()+"(FAIL)	-> Test Case : {} ", result.getMethod().getMethodName());
		setTestInfo();
		ReportUtil.reportResult("FAIL", result.getName(), "");

	}

	public void onTestSkipped(ITestResult result) {
		log.info(LoggerUtil.testFormat()+"(SKIPPED)	-> Test Case : {} ", result.getMethod().getMethodName());
		setTestInfo();
		ReportUtil.reportResult("SKIP", result.getName(), "");

	}

	public void beforeConfiguration(ITestResult result) {
		log.info(LoggerUtil.testFormat()+"(START)	-> Config Name : {} ", result.getMethod().getMethodName());
		
	}

	public void onConfigurationFailure(ITestResult result){
		log.info(LoggerUtil.testFormat()+"(FAIL)	-> Config Name : {} ", result.getMethod().getMethodName());
	}

	public void onConfigurationSkip(ITestResult result){
		log.info(LoggerUtil.testFormat()+"(SKIPPED)	-> Config Name : {} ", result.getMethod().getMethodName());
	}

	public void onConfigurationSuccess(ITestResult result){
		log.info(LoggerUtil.testFormat()+"(SUCCESS)	-> Config Name : {} ", result.getMethod().getMethodName());
	}

	public void onStart(ITestContext context) {
		log.info(LoggerUtil.testFormat()+"(START)	 -> Tests Name : {} ",context.getName()); 
		if(parallelMode.equals("tests")){
			createWebDriver();
		}
	}

	public void onFinish(ITestContext context) {
		log.info(LoggerUtil.testFormat()+"(FINISHED)	 -> Tests Name : {} ",context.getName()); 
		if(parallelMode.equals("tests")){
			quitWebDriver();
		}
	}

	public void onStart(ISuite suite) {
		
		parallelMode=suite.getParallel().toLowerCase();// Get parallel mode and validate
		if(! (parallelMode.equals("false")|| parallelMode.equals("tests") )){
			throw new SelTestException("Unknow Parallel Mode in Suite file !!");
		}

		if(!suiteCalled){
			log.info("");
			log.info("	******* STARTED "+suite.getName().toUpperCase()+" ******");
			log.info("");
			String path = new File("./","src/main/resources/atu.properties").getAbsolutePath();
			System.setProperty("atu.reporter.config", path);
			if(!parallelMode.equals("tests")){ // Only Tests supported
				createWebDriver();
			}
			suiteCalled=true;
		}
	}

	public void onFinish(ISuite suite) {
		if(suiteCalled){
			log.info("");
			log.info("	****** FINISHED "+suite.getName().toUpperCase()+" ******");
			log.info("");
			if(!parallelMode.equals("tests")){
				quitWebDriver();
			}
			suiteCalled=false;
		}
	}

	private synchronized void createWebDriver(){
		
		WebDriver driver = DriverFactory.getDriver();
		log.debug("Driver Created : "+driver.hashCode());
		DriverManager.setWebDriver(driver);		

	}

	private synchronized void quitWebDriver(){
		WebDriver driver = DriverManager.getDriver();
		if (driver != null) {
			log.debug(" Driver Going to Quit "+driver.hashCode());
			driver.quit();
		}

	}

	private void setTestInfo(){
		try{
		if(TestCase.getAuthor()!=null){
			ATUReports.setAuthorInfo(TestCase.getAuthor(), TestCase.getDate(), TestCase.getVersion());
		}}catch(Exception e){
			log.trace("User Information Not Set !");
		}
	}

	private void processAnnotation(ITestResult result) {
		Class<?> testClass = result.getTestClass().getRealClass();//TODO Change ?

		if(testClass.isAnnotationPresent(TestInfo.class)){
			Annotation annotation =testClass.getAnnotation(TestInfo.class);
			TestInfo testInfo = (TestInfo) annotation;

			TestCase.setAuthor(testInfo.author());
			TestCase.setDate(testInfo.lastModified());
			TestCase.setVersion(testInfo.version());
		}
	}
}
