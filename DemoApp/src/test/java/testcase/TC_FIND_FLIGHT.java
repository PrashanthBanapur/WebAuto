package testcase;

import org.testng.annotations.Test;

import pageobject.FlightFinderPage;
import pageobject.SignOnPage;

public class TC_FIND_FLIGHT extends Tests{
  @Test
  public void findFlight() {
	  SignOnPage signon= home.clickSignOn();
	  FlightFinderPage flightFinder=signon.defaultLogin();
	  flightFinder.selectPassCount("3");
	  flightFinder.selectFromPort("London");
	  flightFinder.selectTripType("oneway");
	  flightFinder.selectFromMonth("May");
	  flightFinder.selectFromDay("25");
	  flightFinder.selectToPort("Paris");
	  flightFinder.selectToMonth("May");
	  flightFinder.selectToDay("28");
	  flightFinder.selectClass("First");
	  flightFinder.selectAirlinePref("Unified Airlines");
	  flightFinder.clickContineu();
  }
}
