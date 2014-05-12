package pageobject;

import org.openqa.selenium.WebDriver;
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
	
	public SignOnPage(WebDriver driver) {
		super(driver);
	}
	
	public void defaultLogin(){
		setUserName(USERNAME);
		setPassword(PASSWORD);
		clickLogin();
	}

	public String getUserName() {
		return txtUserName.getText();
	}

	public void setUserName(String val) {
		txtUserName.sendKeys(val);;
	}

	public String getPassword() {
		return txtPassword.getText();
	}

	public void setPassword(String val) {
		txtPassword.sendKeys(val);
	}

	public void clickLogin() {
		imgLogin.click();;
	}

}
