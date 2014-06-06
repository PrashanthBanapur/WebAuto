/**
 * 
 */
package org.seltest.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.asserts.Assertion;
import org.testng.asserts.IAssert;


/**
 * @author adityas
 *
 */
public class HardAssertion extends Assertion {

	private final Logger log = LoggerFactory.getLogger("STEP");

	@Override
	public void onAssertFailure(IAssert assertCommand, AssertionError ex) {
		String expected = assertCommand.getExpected().toString();
		String actual = assertCommand.getActual().toString();
		String msg = ex.getLocalizedMessage();
		
		log.info("	(ASSERT FAILED) :- EXPECTED =  '{}' , ACTUAL = '{}' ",expected,actual);
		ReportUtil.reportAssert("ASSERT FAIL", expected, actual);
		log.info("	(ASSERT FAILED :- MESSAGE = {} ",msg);
		throw new AssertionError(ex);
	}

	@Override
	public void onAssertSuccess(IAssert assertCommand) {
		String expected = assertCommand.getExpected().toString();
		String actual = assertCommand.getActual().toString();
		String msg = assertCommand.getMessage();
		
		if(expected!=null){
			log.info("	(ASSERT SUCCESS) :- EXPECTED =  '{}' , ACTUAL = '{}' ",expected,actual);
			ReportUtil.reportAssert("ASSERT SUCCESS", expected, actual);
		}else if(msg!=null){
			log.info("	(ASSERT SUCCESS) :- Message : {}  ",msg);
			ReportUtil.reportAssert("ASSERT SUCCESS",msg, "");
		}else {
			log.info("	(ASSERT SUCCESS) ");
			ReportUtil.reportAssert("ASSERT SUCCESS", "","");
		}
	}

	@Override
	public void onAfterAssert(IAssert assertCommand) {
		//	Not Implemented
	}

	@Override
	public void onBeforeAssert(IAssert assertCommand) {
		//	Not Implemented
	}

}
