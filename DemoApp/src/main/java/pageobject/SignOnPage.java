package pageobject;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.seltest.core.PageObject;

public class SignOnPage extends PageObject {
	
	private final String USERNAME="admin";
	private final String PASSWORD ="admin";

	@FindBy(name="userName")
	private WebElement txtUserName;
	@FindBy(name="password")
	private WebElement txtPassword;
	@FindBy(name="login")
	private WebElement imgLogin;
	
	public FlightFinderPage defaultLogin(){
		setUserName(USERNAME);
		setPassword(PASSWORD);
		clickLogin();
		return browser.createPage(FlightFinderPage.class);
	}

	public String getUserName() {
		return element.getText(txtUserName);
	}

	public void setUserName(String val) {
		element.sendKeys(txtUserName, val);
	}

	public String getPassword() {
		return element.getText(txtPassword);
	}

	public void setPassword(String val) {
		element.sendKeys(txtPassword,val);
	}

	public void clickLogin() {
		element.click(imgLogin);;
	}

}
