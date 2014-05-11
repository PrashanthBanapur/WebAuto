package testcase;

import org.testng.annotations.Test;

import pageobject.SignOnPage;

public class TC_SIGN_ON extends Tests{
  @Test
  public void SignOn() {
	 SignOnPage signon= home.clickSignOn();
	 signon.defaultLogin();
  }
}
