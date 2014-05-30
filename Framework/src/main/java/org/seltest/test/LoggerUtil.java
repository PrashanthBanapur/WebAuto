package org.seltest.test;

import org.seltest.core.TestCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggerUtil {

	private final Logger step = LoggerFactory.getLogger("STEP");
	private final Logger test = LoggerFactory.getLogger("TEST");
	public void webLooger(String msg){
		step.info(" |T={}:		|<{}>	-{}",Thread.currentThread().getId(),getTestName(),msg);
	}
	
	public void testLogger(String msg ) {
		test.info(" |T={}: {} {}",Thread.currentThread().getId(),msg,getTestName());
	}
	
	public void testLogger(String msg , String arg){
		test.info(" |T={}: {} {}",Thread.currentThread().getId(),msg,arg);
	}
	
	private String getTestName(){
		String name = TestCase.getTestName();
		if(name!=null)
			return name;
		else
			return "config";
	}

}