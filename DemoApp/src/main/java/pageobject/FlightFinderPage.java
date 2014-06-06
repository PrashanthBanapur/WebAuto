package pageobject;


import java.util.List;

import org.openqa.selenium.WebDriver;
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

	public FlightFinderPage(WebDriver driver) {
		super(driver);
	}
	public void selectTripType(String val){
		step.clickRadioButton(rBtnTripType, val);
	}
	public void selectPassCount(String val){
		step.select(ddPassCount, val);
	}
	public void selectFromPort(String val){
		step.select(ddFromPort, val);
	}
	public void selectFromMonth(String val){
		step.select(ddFromMonth, val);
	}
	public void selectFromDay(String val){
		step.select(ddFromDay, val);
	}
	public void selectToPort(String val){
		step.select(ddToPort, val);
	}
	public void selectToMonth(String val){
		step.select(ddToMonth, val);
	}
	public void selectToDay(String val){
		step.select(ddToDay, val);
	}
	public void selectClass(String val){
		step.clickRadioButton(rBtnServiceClass, val);
	}
	public void selectAirlinePref(String val){
		step.select(ddAirlinePref, val);
	}
	public void clickContineu(){
		step.click(bntContinue);
	}
	
}
