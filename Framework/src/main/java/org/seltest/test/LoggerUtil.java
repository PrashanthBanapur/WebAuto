package org.seltest.test;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.openqa.selenium.WebDriver;
import org.seltest.core.TestCase;
import org.seltest.driver.DriverManager;
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



	public void web(String msg){
		seltest.info(" |T={}:		|[D={}]	 |-<{}>	-{}",Thread.currentThread().getId(),DriverManager.getDriver().hashCode(),getTestName(),msg);
	}

	public void test(String msg ) {
		WebDriver driver = DriverManager.getDriver();
		if(driver==null){
			seltest.info(" |T={}: {} {}",Thread.currentThread().getId(),msg,getTestName());
		}else{
			seltest.info(" |T={}:		|[D={}]	 |-<{}>	-{}",Thread.currentThread().getId(),driver.hashCode(),getTestName(),msg);
		}
	}

	public void test(String msg , String arg){
		seltest.info(" |T={}: {} {}",Thread.currentThread().getId(),msg,arg);
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
			seltest.info(" |T={}:	{} :- EXPECTED =  '{}' , ACTUAL = '{}' ",Thread.currentThread().getId(),status,expected,actual);
			ReportUtil.reportAssert("ASSERT "+status.toLowerCase(), expected, actual);
		}else if(msg!=null){
			seltest.info(" |T={}:	{} :- Message : {}  ",Thread.currentThread().getId(),status,msg);
			ReportUtil.reportAssert("ASSERT "+status.toUpperCase(),msg, "");
		}else {
			seltest.info(" |T={}:	{} ",Thread.currentThread().getId(),status);
			ReportUtil.reportAssert("ASSERT "+status.toUpperCase(), "","");
		}

	}

	private String getTestName(){
		String name = TestCase.getTestName();
		if(name!=null)
			return name;
		else
			return "config";
	}

	public void info(String format , Object ... arguments ){
		format = "|T="+Thread.currentThread().getId()+" :	"+format;
		seltest.info(format, arguments);
	}
	public void debug(String format , Object ... arguments){
		format = "|T="+Thread.currentThread().getId()+" :	"+format;
		seltest.debug(format,arguments);
	}
	
	public void trace(String format , Object ... arguments){
		format = "|T="+Thread.currentThread().getId()+" :	"+format;
		seltest.trace(format,arguments);
	}
	
	public void warn(String format , Object ... arguments){
		format = "|T="+Thread.currentThread().getId()+" :	"+format;
		seltest.warn(format, arguments);
	}

}