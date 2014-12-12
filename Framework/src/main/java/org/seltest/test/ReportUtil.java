package org.seltest.test;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.seltest.core.Config;
import org.seltest.driver.DriverManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import atu.testng.reports.ATUReports;
import atu.testng.reports.logging.LogAs;
import atu.testng.selenium.reports.CaptureScreen;
import atu.testng.selenium.reports.CaptureScreen.ScreenshotOf;

public class ReportUtil {

	public static ReportUtil report = new ReportUtil();
	private  final String screenShot = Config.captureScreenshot.getValue();
	private  final String browser = Config.browser.getValue();
	private  final Logger log = LoggerFactory.getLogger(ReportUtil.class);

	// Avoid creating objects
	private ReportUtil(){
	}
	/**
	 * Report result of test case
	 * 
	 * @param desp
	 * @param msg1
	 * @param msg2
	 */
	public void reportResult(String desp, String msg1, String msg2) {

		if (screenShot.equals("all") || screenShot.equals("result")) {
			reportWithScreenshot(desp, msg1, msg2, LogAs.INFO);
		} else {
			reportWithoutScreenShot(desp, msg1, msg2, LogAs.INFO);
		}

	}

	/**
	 * Report Web Event
	 * 
	 * @param desp
	 * @param msg1
	 * @param msg2
	 */
	void reportWebStep(WebElement element, String desp, String msg1,
			String msg2) {

		if (screenShot.equals("all") && (element != null)) {
			WebDriver driver = DriverManager.getDriver();

			if (browser.equals("firefox")) {
				String border = highlightElement(driver, element);
				reportWithScreenshot(desp, msg1, msg2, LogAs.PASSED);
				unhighlightElement(driver, element, border);
			} else {
				reportWithScreenshot(desp, msg1, msg2, LogAs.PASSED);
			}
		} else {
			reportWithoutScreenShot(desp, msg1, msg2, LogAs.PASSED);
		}

	}

	/**
	 * Report Test case Assertions
	 * 
	 * @param desp
	 * @param actual
	 * @param exp
	 */
	public void reportAssert(String desp, String expected, String actual) {
		if (expected == null) {
			expected = "";
		}
		if (desp == null) {
			desp = "";
		}
		if (actual == null) {
			actual = "";
		}
		if (screenShot.equals("assertion") || screenShot.equals("all")
				|| screenShot.equals("result")) {
			reportWithScreenshot(desp, expected, actual, LogAs.INFO);
		} else {
			reportWithoutScreenShot(desp, expected, actual, LogAs.INFO);
		}
	}

	/**
	 * Report Exceptions at Runtime
	 * 
	 * @param desp
	 * @param msg1
	 * @param msg2
	 */
	void reportException(String desp, String msg1, String msg2) {
		// reportWithoutScreenshot(desp, msg1,msg2 , LogAs.FAILED);
		reportWithoutScreenShot(desp, msg1, msg2, LogAs.WARNING);
	}

	private  void reportWithoutScreenShot(String msg1, String msg2,
			String msg3, LogAs logType) {
		try {
			ATUReports.add(msg1, msg2, msg3, logType,new CaptureScreen(ScreenshotOf.BROWSER_PAGE));
		} catch (Exception e) {
			// TODO
		}
	}

	private  synchronized void reportWithScreenshot(String msg1,
			String msg2, String msg3, LogAs logType) {
		WebDriver driver = DriverManager.getDriver();
		if (driver == null) {
			reportWithoutScreenShot(msg1, msg2, msg3, logType);
		} else {

			try {
				ATUReports.setWebDriver(driver);
				ATUReports.add(msg1, msg2, msg3, logType, new CaptureScreen(
						ScreenshotOf.BROWSER_PAGE));
			} catch ( Exception e) {
				log.trace("Screen Shot not captured : " + msg2);
				log.trace(e.toString());
			}
		}
	}

	/**
	 * Draws a red border around the found element
	 * 
	 * @param driver
	 * @param element
	 * @return current border
	 */
	private String highlightElement(WebDriver driver, WebElement element) {
		// draw a border around the found element
		String border = null;
		if (driver instanceof JavascriptExecutor) {
			border = (String) ((JavascriptExecutor) driver).executeScript(
					SCRIPT_GET_ELEMENT_BORDER, element);
			((JavascriptExecutor) driver).executeScript(
					"arguments[0].style.border='3px solid red'", element);
		}
		return border;

	}

	/**
	 * Un Highlight Already Highlighted Element
	 * 
	 * @param driver
	 * @return
	 */
	private WebElement unhighlightElement(WebDriver driver,
			WebElement element, String border) {
		if (driver instanceof JavascriptExecutor) {
			try {
				((JavascriptExecutor) driver).executeScript(
						SCRIPT_UNHIGHLIGHT_ELEMENT, element, border);
			} catch (StaleElementReferenceException ignored) {
				// the page got reloaded, the element isn't there
			}
		}
		return element;
	}

	private final String SCRIPT_GET_ELEMENT_BORDER = " var elem = arguments[0]; "
			+ " if (elem.currentStyle) { "
			+ "   var style = elem.currentStyle; "
			+ "   var border = style['borderTopWidth'] "
			+ "           + ' ' + style['borderTopStyle'] "
			+ "          + ' ' + style['borderTopColor'] "
			+ "          + ';' + style['borderRightWidth'] "
			+ "          + ' ' + style['borderRightStyle'] "
			+ "          + ' ' + style['borderRightColor'] "
			+ "          + ';' + style['borderBottomWidth'] "
			+ "          + ' ' + style['borderBottomStyle'] "
			+ "          + ' ' + style['borderBottomColor'] "
			+ "          + ';' + style['borderLeftWidth'] "
			+ "          + ' ' + style['borderLeftStyle'] "
			+ "          + ' ' + style['borderLeftColor']; "
			+ "	} else if (window.getComputedStyle) { "
			+ "  var style = document.defaultView.getComputedStyle(elem); "
			+ "   var border = style.getPropertyValue('border-top-width') "
			+ "          + ' ' + style.getPropertyValue('border-top-style') "
			+ "           + ' ' + style.getPropertyValue('border-top-color') "
			+ "           + ';' + style.getPropertyValue('border-right-width') "
			+ "           + ' ' + style.getPropertyValue('border-right-style') "
			+ "           + ' ' + style.getPropertyValue('border-right-color') "
			+ "           + ';' + style.getPropertyValue('border-bottom-width') "
			+ "           + ' ' + style.getPropertyValue('border-bottom-style') "
			+ "           + ' ' + style.getPropertyValue('border-bottom-color') "
			+ "           + ';' + style.getPropertyValue('border-left-width') "
			+ "           + ' ' + style.getPropertyValue('border-left-style') "
			+ "           + ' ' + style.getPropertyValue('border-left-color'); "
			+ "	} " + "return border;";

	private final String SCRIPT_UNHIGHLIGHT_ELEMENT = "	var elem = arguments[0]; "
			+ "var borders = arguments[1].split(';');"
			+ "elem.style.borderTop = borders[0];"
			+ "elem.style.borderRight = borders[1];"
			+ "elem.style.borderBottom = borders[2];"
			+ "elem.style.borderLeft = borders[3];";
}
