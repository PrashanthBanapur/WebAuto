package org.seltest.core;

import org.seltest.test.HardAssertion;
import org.seltest.test.SoftAssertion;
import org.testng.SkipException;

/**
 * Super Class for all Test Cases
 * 
 * @author adityas
 * 
 */
public class Tests {

	protected Browser browser = new Browser();
	protected HardAssertion hassert = new HardAssertion();
	protected SoftAssertion verify = new SoftAssertion();

	protected void SkipTest(String msg) {
		throw new SkipException(msg);
	}

}
