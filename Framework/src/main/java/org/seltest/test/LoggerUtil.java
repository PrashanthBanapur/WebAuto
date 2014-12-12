package org.seltest.test;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.openqa.selenium.WebElement;
import org.seltest.core.TestCaseDetail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.asserts.IAssert;

public class LoggerUtil {

	private static LoggerUtil logger = new LoggerUtil();
	private final Logger seltest = LoggerFactory.getLogger(LoggerUtil.class);
	ReportUtil report = ReportUtil.report;

	// Cannot Create instance
	private LoggerUtil() {
	}

	public static LoggerUtil getLogger() {
		return logger;
	}

	public void exception(Throwable throwable) {
		seltest.error("(EXCEPTION) 	-> Message = "
				+ throwable.getLocalizedMessage() + " ");
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		throwable.printStackTrace(pw);
		seltest.info(sw.toString()); // stack trace as a string
	}

	public void assertion(String status, IAssert assertCommand) {
		String expected = assertCommand.getExpected().toString();
		String actual = assertCommand.getActual().toString();
		String msg = assertCommand.getMessage();

		if (expected != null) {
			seltest.info("|-{} :- EXPECTED =  '{}' , ACTUAL = '{}' ", status,
					expected, actual);
			report.reportAssert("ASSERT " + status.toLowerCase(), expected,actual);
		} else if (msg != null) {
			seltest.info("|-{} :- Message : {}  ", status, msg);
			report.reportAssert("ASSERT " + status.toUpperCase(), msg, "");
		} else {
			seltest.info("|-{} ", status);
			report.reportAssert("ASSERT " + status.toUpperCase(), "", "");
		}

	}

	public static String webFormat() {
		return "	 |-<" + getTestName() + ">	- ";
	}

	public static String testFormat() {
		return "|-<" + getTestName() + ">	- ";
	}

	private static String getTestName() {
		String name = TestCaseDetail.getTestName();
		if (name != null)
			return name;
		else
			return "config";
	}
	public static String getElementIdentity(WebElement element) {

		String elemId = element.getAttribute("id");
		String elemClass = element.getAttribute("class");
		String elemName = element.getAttribute("name");
		String elemText = element.getText();
		String elementIdentity = "";

		if ((elemId != null) && (!elemId.isEmpty())) {
			elementIdentity = elemId;
		} else if ((elemClass != null) && (!elemClass.isEmpty())) {
			elementIdentity = elemClass;
		} else if ((elemName != null) && (!elemName.isEmpty())) {
			elementIdentity = elemName;
		} else if ((elemText != null) && (!elemText.isEmpty())) {
			elementIdentity = elemText;
		}

		return elementIdentity;

	}

}