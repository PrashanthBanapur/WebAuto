package org.seltest.test;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.seltest.core.TestCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.asserts.IAssert;


public class LoggerUtil {


	private static LoggerUtil logger = new LoggerUtil();
	private final Logger seltest = LoggerFactory.getLogger("SELTEST");

	// Cannot Create instance
	private LoggerUtil(){
	}

	public static LoggerUtil getLogger(){
		return logger;
	}

	public void exception(Throwable throwable){
		seltest.error("(EXCEPTION) 	-> Message = "+throwable.getLocalizedMessage()+" ");
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		throwable.printStackTrace(pw);
		seltest.info(sw.toString()); // stack trace as a string
	}

	public void assertion(String status ,IAssert assertCommand){
		String expected = assertCommand.getExpected().toString();
		String actual = assertCommand.getActual().toString();
		String msg = assertCommand.getMessage();

		if(expected!=null){
			seltest.info("|-{} :- EXPECTED =  '{}' , ACTUAL = '{}' ",status,expected,actual);
			ReportUtil.reportAssert("ASSERT "+status.toLowerCase(), expected, actual);
		}else if(msg!=null){
			seltest.info("|-{} :- Message : {}  ",status,msg);
			ReportUtil.reportAssert("ASSERT "+status.toUpperCase(),msg, "");
		}else {
			seltest.info("|-{} ",status);
			ReportUtil.reportAssert("ASSERT "+status.toUpperCase(), "","");
		}

	}

	public static String webFormat(){
		return "	 |-<"+getTestName()+">	- ";
	}
	public static String testFormat() {
		return "|-<"+getTestName()+">	- ";
	}
	private static String getTestName(){
		String name = TestCase.getTestName();
		if(name!=null)
			return name;
		else
			return "config";
	}


}