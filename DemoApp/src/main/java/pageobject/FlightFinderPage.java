package pageobject;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.seltest.core.PageObject;

public class FlightFinderPage extends PageObject {

	@FindBy(name="tripType")
	private WebElement rBtnTripType;
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
	@FindBy(name="ToDay")
	private WebElement ddToDay;
	@FindBy(name="servClass")
	private WebElement rBtnServiceClass;
	@FindBy(name="airline")
	private WebElement ddAirlinePref;
	@FindBy(name="findFlights")
	private WebElement bntContinue;

	public FlightFinderPage(WebDriver driver) {
		super(driver);
	}

	public void selectTripType(String val){
		rBtnTripType.sendKeys(val);
	}

	public void selectPassCount(String val){
		STEP.select(ddPassCount, val);
	}
	public void selectFromPort(String val){
		STEP.select(ddFromPort, val);
	}
	public void selectFromMonth(String val){
		STEP.select(ddFromMonth, val);
	}
	public void selectFromDay(String val){
		STEP.select(ddFromDay, val);
	}
	public void selectToPort(String val){
		STEP.select(ddToPort, val);
	}
	public void selectToMonth(String val){
		STEP.select(ddToMonth, val);
	}
	public void selectToDay(String val){
		STEP.select(ddToDay, val);
	}
	public void selectClass(String val){
		STEP.sendKeys(rBtnServiceClass, val);
	}
	public void selectAirlinePref(String val){
		STEP.select(ddAirlinePref, val);
	}
	public void clickContineu(){
		STEP.click(bntContinue);
	}
	
}
