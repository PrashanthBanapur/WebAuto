/**
 * 
 */
package org.seltest.test;

import org.testng.asserts.Assertion;
import org.testng.asserts.IAssert;


/**
 * @author adityas
 *
 */
public class HardAssertion extends Assertion {

	private final LoggerUtil logger = LoggerUtil.getLogger();

	@Override
	public void onAssertFailure(IAssert assertCommand, AssertionError ex) {
		logger.assertion(" ASSERT FAILED) ",assertCommand);
		throw new AssertionError(ex);
	}

	@Override
	public void onAssertSuccess(IAssert assertCommand) {
		logger.assertion("	(ASSERT SUCCESS) ", assertCommand);
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
