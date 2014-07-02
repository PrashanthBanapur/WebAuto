/**
 * 
 */
package org.seltest.test;

import org.openqa.selenium.WebDriver;
import org.seltest.core.Config;
import org.seltest.core.StepUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.asserts.Assertion;
import org.testng.asserts.IAssert;


/**
 * @author adityas
 *
 */
public class HardAssertion extends Assertion {

	private final LoggerUtil logger = LoggerUtil.getLogger();
	private final Logger log = LoggerFactory.getLogger(HardAssertion.class);
	private final int MAX_RETRY = Integer.parseInt(Config.exceptionRetry.getValue());

	@Override
	public void onAssertFailure(IAssert assertCommand, AssertionError ex) {
		logger.assertion(" (ASSERT FAILED) ",assertCommand);
		throw new AssertionError(ex);
	}

	@Override
	public void onAssertSuccess(IAssert assertCommand) {
		logger.assertion("	(ASSERT SUCCESS) ", assertCommand);
	}

	public void assertTitle(WebDriver driver ,String expectedTitle){
		int retry =0;
		StepUtil.waitForPageLoaded(driver);
		String actual=null;
		while(retry<MAX_RETRY){
			actual =driver.getTitle();
			try{
				assert(actual.equals(expectedTitle));
				break;
			}catch(AssertionError e){
				StepUtil.defaultWait();
				log.info(LoggerUtil.webFormat()+"( HANDLED EXCEPTION) 	-> Message : {} ",e.getClass());
			}finally{
				retry++;
			}
		}
		assertEquals(actual, expectedTitle);

	}

	public void assertUrl(WebDriver driver , String expectedUrl){
		int retry =0;
		StepUtil.waitForPageLoaded(driver);
		String actual = null;
		while(retry<MAX_RETRY){
			actual =driver.getCurrentUrl().split("\\?")[0];// Remove parameters
			try{
				assert(actual.equals(expectedUrl));
				break;
			}catch(AssertionError e){
				StepUtil.defaultWait();
				log.info(LoggerUtil.webFormat()+"( HANDLED EXCEPTION) 	-> Message : {} ",e.getClass());
			}finally{
				retry++;
			}
		}
		assertEquals(actual, expectedUrl);
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
