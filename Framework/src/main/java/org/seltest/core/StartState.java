package org.seltest.core;

/**
 * Interface to help determine if the test are in correct page <br/>
 * First Launch Page common for all test cases should implement this inteface .
 * 
 * @author adishi
 * 
 */
public interface StartState {

	public Boolean isStartState();

	public void goToStartPage();
}
