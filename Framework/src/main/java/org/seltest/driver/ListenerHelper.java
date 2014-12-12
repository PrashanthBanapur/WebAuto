package org.seltest.driver;

import java.io.File;
import java.lang.annotation.Annotation;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import org.seltest.core.Config;
import org.seltest.core.SelTestException;
import org.seltest.core.TestCaseDetail;
import org.seltest.core.TestInfo;
import org.seltest.test.LoggerUtil;
import org.seltest.test.ReportUtil;
import org.seltest.test.WebEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ISuite;
import org.testng.ITestContext;
import org.testng.ITestNGMethod;
import org.testng.ITestResult;

import atu.testng.reports.ATUReports;

/**
 * Class contains all the implementation in driver listener . So as to avoid
 * rebuilding jar after every change in DriverListener class
 * 
 * @author adityas
 * 
 */
public class ListenerHelper {

	private Logger log = LoggerFactory.getLogger(ListenerHelper.class);
	private ReportUtil report = ReportUtil.report;
	private static String parallelMode;
	private static String browser = Config.browser.getValue();
	private static Boolean suiteCalled = false; // TODO To avoid calling suite
												// listeners twice
	private static Boolean eventFiring;
	private static Boolean fullscreen;
	private static final int MAX_WAIT = Integer.parseInt(Config.waitMaxTimeout
			.getValue());

	// restricting to Package Access
	ListenerHelper() {

	}

	static {
		eventFiring = Boolean.parseBoolean(Config.eventfiring.getValue());
		fullscreen = Boolean.parseBoolean(Config.fullscreen.getValue());
	}

	public void onTestStart(ITestResult result) {
		TestCaseDetail.setTestName(result.getName());
		log.info(LoggerUtil.testFormat() + "(START)	-> Test Case : {} ", result
				.getMethod().getMethodName());
		processAnnotation(result);
		if(parallelMode.equals("methods")){
			createWebDriver();
		}

	}

	public void onTestSuccess(ITestResult result) {
		log.info(LoggerUtil.testFormat() + "(SUCCESS)	-> Test Case : {} ",
				result.getMethod().getMethodName());
		setTestInfo();
		report.reportResult("SUCCESS", result.getName(), "");

		if(parallelMode.equals("methods")){
			quitWebDriver();
		}
	}

	public void onTestFailure(ITestResult result) {
		log.info(LoggerUtil.testFormat() + "(FAIL)	-> Test Case : {} ", result
				.getMethod().getMethodName());
		setTestInfo();
		report.reportResult("FAIL", result.getName(), "");
		if(parallelMode.equals("methods")){
			quitWebDriver();
		}

	}

	public void onTestSkipped(ITestResult result) {
		log.info(LoggerUtil.testFormat() + "(SKIPPED)	-> Test Case : {} ",
				result.getMethod().getMethodName());
		setTestInfo();
		report.reportResult("SKIP", result.getName(), "");
		if(parallelMode.equals("methods")){
			quitWebDriver();
		}

	}

	public synchronized void beforeConfiguration(ITestResult result) {

		ITestNGMethod method = result.getMethod();
		if (method.isBeforeMethodConfiguration()
				&& parallelMode.equals("classes")) {
			if (DriverManager.getDriver() == null)
				createWebDriver();
		}
		log.info(LoggerUtil.testFormat() + "(START)	-> Config Name : {} ",
				result.getMethod().getMethodName());

	}

	public synchronized void onConfigurationFailure(ITestResult result) {
		ITestNGMethod method = result.getMethod();
		if (method.isAfterMethodConfiguration()
				&& parallelMode.equals("classes")) {
			quitWebDriver();
		}
		log.info(LoggerUtil.testFormat() + "(FAIL)	-> Config Name : {} ",
				result.getMethod().getMethodName());
	}

	public synchronized void onConfigurationSkip(ITestResult result) {
		ITestNGMethod method = result.getMethod();
		if (method.isAfterMethodConfiguration()
				&& parallelMode.equals("classes")) {
			quitWebDriver();
		}
		log.info(LoggerUtil.testFormat() + "(SKIPPED)	-> Config Name : {} ",
				result.getMethod().getMethodName());
	}

	public synchronized void onConfigurationSuccess(ITestResult result) {
		ITestNGMethod method = result.getMethod();
		if (method.isAfterMethodConfiguration()
				&& parallelMode.equals("classes")) {
			quitWebDriver();
		}
		log.info(LoggerUtil.testFormat() + "(SUCCESS)	-> Config Name : {} ",
				result.getMethod().getMethodName());
	}

	public void onStart(ITestContext context) {
		log.info(LoggerUtil.testFormat() + "(START)	 -> Tests Name : {} ",
				context.getName());
		if (parallelMode.equals("tests")) {
			createWebDriver();
		}
	}

	public void onFinish(ITestContext context) {
		log.info(LoggerUtil.testFormat() + "(FINISHED)	 -> Tests Name : {} ",
				context.getName());
		if (parallelMode.equals("tests")) {
			quitWebDriver();
		}
	}

	public void onStart(ISuite suite) {

		parallelMode = suite.getParallel().toLowerCase();// Get parallel mode
															// and validate
		if (!(parallelMode.equals("false") || parallelMode.equals("tests") || parallelMode
				.equals("methods"))) {
			throw new SelTestException("Unknow Parallel Mode in Suite file !!");
		}

		if (browser.equals("ie") && !(parallelMode.equals("false"))) { // Validating
																		// IE
																		// parallel
																		// Mode
			log.warn(" IE Does Not Support Parallel Execution !! ");
			throw new SelTestException(
					"Parallel Not Support Change Suite Config 'parallel= false' "
							+ suite.getName());
		}

		if (!suiteCalled) {
			log.info("");
			log.info("	******* STARTED " + suite.getName().toUpperCase()
					+ " ******");
			log.info("");
			String path = new File("./", "src/main/resources/atu.properties")
					.getAbsolutePath();
			System.setProperty("atu.reporter.config", path);
			if (parallelMode.equals("false")) { // Only Parallel Mode supported
												// : Tests
				createWebDriver();
			}
			suiteCalled = true;
		}
	}

	public void onFinish(ISuite suite) {
		if (suiteCalled) {
			log.info("");
			log.info("	****** FINISHED " + suite.getName().toUpperCase()
					+ " ******");
			log.info("");
			if (parallelMode.equals("false")) {
				quitWebDriver();
			}
			suiteCalled = false;
		}
	}

	private synchronized void createWebDriver() {
		WebDriver driver = null;

		if (Config.runMode.getValue().equalsIgnoreCase("single")) {
			driver = DriverFactory.getDriver();
		} else { // Grid
			driver = RemoteDriverFactory.getDriver();
		}
		driver = configDriver(driver);
		log.debug("Driver Created : " + driver.hashCode());
		DriverManager.setWebDriver(driver);
	}

	private synchronized void quitWebDriver() {
		WebDriver driver = DriverManager.getDriver();
		if (driver != null) {
			log.debug(" Driver Going to Quit " + driver.hashCode());
			driver.quit();
			driver =null;
		}

	}

	public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
		// TODO
	}

	private void setTestInfo() {
		try {
			if (TestCaseDetail.getAuthor() != null) {
				ATUReports.setAuthorInfo(TestCaseDetail.getAuthor(),
						TestCaseDetail.getDate(), TestCaseDetail.getVersion());
			}
		} catch (Exception e) {
			log.trace("User Information Not Set !");
		}
	}

	private WebDriver configDriver(WebDriver driver) {

		// Adding Web Event Listener
		if (eventFiring) {
			EventFiringWebDriver efirDriver = new EventFiringWebDriver(driver);
			WebEventListener driverListner = new WebEventListener();
			driver = efirDriver.register(driverListner);
		} else {
			log.warn(
					"FrameWork Wont Work Properly : 'Event Firing' Set To : '{}'",
					eventFiring);
		}

		if (fullscreen) {
			driver.manage().window().maximize();
		}

		driver.manage().timeouts().pageLoadTimeout(MAX_WAIT, TimeUnit.SECONDS);
		driver.manage().timeouts().implicitlyWait(MAX_WAIT, TimeUnit.SECONDS);
		return driver;
	}

	private void processAnnotation(ITestResult result) {
		Class<?> testClass = result.getTestClass().getRealClass();// TODO Change
																	// ?

		if (testClass.isAnnotationPresent(TestInfo.class)) {
			Annotation annotation = testClass.getAnnotation(TestInfo.class);
			TestInfo testInfo = (TestInfo) annotation;

			TestCaseDetail.setAuthor(testInfo.author());
			TestCaseDetail.setDate(testInfo.lastModified());
			TestCaseDetail.setVersion(testInfo.version());
		}
	}
}
