/**
 * 
 */
package org.seltest.driver;

import org.testng.ISuite;
import org.testng.ISuiteListener;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.TestListenerAdapter;

/**
 * Class for controlling the Driver creation and logging results
 * @author adityas
 *
 */
public class DriverListener  extends TestListenerAdapter implements ISuiteListener{

	private ListenerHelper helper = new ListenerHelper();

	@Override
	public void onTestStart(ITestResult result) {
		helper.onTestStart(result);
		super.onTestStart(result);
	}

	@Override
	public void onTestSuccess(ITestResult result) {
		helper.onTestSuccess(result);
		super.onTestSuccess(result);
	}

	@Override
	public void onTestFailure(ITestResult result) {
		helper.onTestFailure(result);
		super.onTestFailure(result);

	}

	@Override
	public void onTestSkipped(ITestResult result) {
		helper.onTestSkipped(result);
		super.onTestSkipped(result);

	}

	@Override
	public void beforeConfiguration(ITestResult result) {
		helper.beforeConfiguration(result);
		super.onConfigurationFailure(result);
	}

	@Override
	public void onConfigurationFailure(ITestResult result){
		helper.onConfigurationFailure(result);
		super.onConfigurationFailure(result);
	}

	@Override
	public void onConfigurationSkip(ITestResult result){
		helper.onConfigurationSkip(result);
		super.onConfigurationSkip(result);
	}
	@Override
	public void onConfigurationSuccess(ITestResult result){
		helper.onConfigurationSuccess(result);
		super.onConfigurationSuccess(result);
	}

	@Override
	public void onStart(ITestContext context) {
		helper.onStart(context);
	}

	@Override
	public void onFinish(ITestContext context) {
		helper.onFinish(context);
	}
	@Override
	public void onStart(ISuite suite) {
		helper.onStart(suite);
	}

	@Override
	public void onFinish(ISuite suite) {
		helper.onFinish(suite);
	}


}
