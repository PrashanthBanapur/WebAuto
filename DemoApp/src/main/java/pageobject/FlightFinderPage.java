package pageobject;


import java.util.List;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.seltest.core.PageObject;

public class FlightFinderPage extends PageObject {

	@FindBy(name="tripType")
	private List<WebElement> rBtnTripType;
	@FindBy(name="passCount")
	private WebElement ddPassCount;
	@FindBy(name="fromPort")
	private WebElement ddFromPort;
	@FindBy(name="fromMonth")
	private WebElement ddFromMonth;
	@FindBy(name="fromDay")
	private WebElement ddFromDay;
	@FindBy(name="toPort")
	private WebElement ddToPort;
	@FindBy(name="toMonth")
	private WebElement ddToMonth;
	@FindBy(name="toDay")
	private WebElement ddToDay;
	@FindBy(name="servClass")
	private List<WebElement>rBtnServiceClass;
	@FindBy(name="airline")
	private WebElement ddAirlinePref;
	@FindBy(name="findFlights")
	private WebElement bntContinue;

	public void selectTripType(String val){
		element.clickRadioButton(rBtnTripType, val);
	}
	public void selectPassCount(String val){
		element.select(ddPassCount, val);
	}
	public void selectFromPort(String val){
		element.select(ddFromPort, val);
	}
	public void selectFromMonth(String val){
		element.select(ddFromMonth, val);
	}
	public void selectFromDay(String val){
		element.select(ddFromDay, val);
	}
	public void selectToPort(String val){
		element.select(ddToPort, val);
	}
	public void selectToMonth(String val){
		element.select(ddToMonth, val);
	}
	public void selectToDay(String val){
		element.select(ddToDay, val);
	}
	public void selectClass(String val){
		element.clickRadioButton(rBtnServiceClass, val);
	}
	public void selectAirlinePref(String val){
		element.select(ddAirlinePref, val);
	}
	public void clickContineu(){
		element.click(bntContinue);
	}
	
}
