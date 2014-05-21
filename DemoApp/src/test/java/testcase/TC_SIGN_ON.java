package testcase;

import org.seltest.core.TestInfo;
import org.testng.annotations.Test;

import pageobject.SignOnPage;

@TestInfo(author="aditya",lastModified="21-May-2014",version="1.0")
public class TC_SIGN_ON extends Tests{
  @Test
  public void SignOn() {
	 SignOnPage signon= home.clickSignOn();
	 signon.defaultLogin();
  }
}
