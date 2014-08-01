/**
 * 
 */
package org.seltest.driver;

import org.testng.IConfigurationListener;
import org.testng.IConfigurationListener2;
import org.testng.ISuite;
import org.testng.ISuiteListener;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

/**
 * Class for controlling the Driver creation and logging results
 * 
 * @author adityas
 * 
 */
public class DriverListener implements ISuiteListener, ITestListener,
		IConfigurationListener, IConfigurationListener2 {

	private ListenerHelper helper = new ListenerHelper();

	@Override
	public void onTestStart(ITestResult result) {
		helper.onTestStart(result);
	}

	@Override
	public void onTestSuccess(ITestResult result) {
		helper.onTestSuccess(result);
	}

	@Override
	public void onTestFailure(ITestResult result) {
		helper.onTestFailure(result);
	}

	@Override
	public void onTestSkipped(ITestResult result) {
		helper.onTestSkipped(result);
	}

	@Override
	public void beforeConfiguration(ITestResult result) {
		helper.beforeConfiguration(result);
	}

	@Override
	public void onConfigurationFailure(ITestResult result) {
		helper.onConfigurationFailure(result);
	}

	@Override
	public void onConfigurationSkip(ITestResult result) {
		helper.onConfigurationSkip(result);
	}

	@Override
	public void onConfigurationSuccess(ITestResult result) {
		helper.onConfigurationSuccess(result);
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

	@Override
	public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
		helper.beforeConfiguration(result);

	}

}
