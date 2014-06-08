package org.seltest.test;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.seltest.core.TestCase;
import org.seltest.driver.DriverManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.asserts.IAssert;


public class LoggerUtil {
	

	private static LoggerUtil logger = new LoggerUtil();
	private final Logger step = LoggerFactory.getLogger("STEP");
	private final Logger test = LoggerFactory.getLogger("TEST");
	private final Logger log = LoggerFactory.getLogger("LOG");
	
	// Cannot Create instance
	private LoggerUtil(){
	}
	
	public static LoggerUtil getLogger(){
		return logger;
	}
	

	
	public void web(String msg){
		step.info(" |T={}:		|[D={}]	 |-<{}>	-{}",Thread.currentThread().getId(),DriverManager.getDriver().hashCode(),getTestName(),msg);
	}
	
	public void test(String msg ) {
		test.info(" |T={}: {} {}",Thread.currentThread().getId(),msg,getTestName());
	}
	
	public void test(String msg , String arg){
		test.info(" |T={}: {} {}",Thread.currentThread().getId(),msg,arg);
	}
	
	public void exception(Throwable throwable){
		step.error("(EXCEPTION) 	-> Message = "+throwable.getLocalizedMessage()+" ");
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		throwable.printStackTrace(pw);
		step.info(sw.toString()); // stack trace as a string
	}
	
	public void assertion(String status ,IAssert assertCommand){
		String expected = assertCommand.getExpected().toString();
		String actual = assertCommand.getActual().toString();
		String msg = assertCommand.getMessage();
		
		if(expected!=null){
			test.info(" |T={}:	{} :- EXPECTED =  '{}' , ACTUAL = '{}' ",Thread.currentThread().getId(),status,expected,actual);
			ReportUtil.reportAssert("ASSERT "+status.toLowerCase(), expected, actual);
		}else if(msg!=null){
			test.info(" |T={}:	{} :- Message : {}  ",Thread.currentThread().getId(),status,msg);
			ReportUtil.reportAssert("ASSERT "+status.toUpperCase(),msg, "");
		}else {
			test.info(" |T={}:	{} ",Thread.currentThread().getId(),status);
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
		log.info(format, arguments);
	}
	public void debug(String format , Object ... arguments){
		format = "|T="+Thread.currentThread().getId()+" :	"+format;
		log.debug(format,arguments);
	}
	public void warn(String format , Object ... arguments){
		format = "|T="+Thread.currentThread().getId()+" :	"+format;
		log.warn(format, arguments);
	}
	
}