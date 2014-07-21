package org.seltest.core;

import org.testng.SkipException;

/**
 * Super Class for all Test Cases 
 * @author adityas
 *
 */
public class Tests {
	
	protected Browser browser = new Browser();
	
	protected void SkipTest(String msg){
		throw new SkipException(msg);
	}

}
