package testcase;

import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import org.seltest.driver.DriverListener;

import pageobject.SignOnPage;

@Listeners({DriverListener.class})
public class TC_SIGN_ON extends Tests{
  @Test
  public void SignOn() {
	 SignOnPage signon= home.clickSignOn();
	 signon.defaultLogin();
  }
}
