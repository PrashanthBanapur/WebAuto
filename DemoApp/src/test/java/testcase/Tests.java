package testcase;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.seltest.core.StartState;
import org.seltest.driver.DriverManager;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import pageobject.HomePage;

public class Tests {
	protected WebDriver driver;
	protected HomePage home;
	
	@BeforeMethod
	public void Open(){
		driver = DriverManager.getDriver();
		driver.get(HomePage.URL);
		home = PageFactory.initElements(driver, HomePage.class);
		StartState state = home;
		if(!state.isStartState()){
			state.goToStartPage();
		}
		
	}

	@AfterMethod
	public void Close(){
		home.clickSignOff();
	}
}
