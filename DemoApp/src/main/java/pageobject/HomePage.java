package pageobject;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.seltest.core.Config;
import org.seltest.core.PageObject;

public class HomePage extends PageObject {

	public static final String URL = Config.baseUrl.getValue()+"mercurywelcome.php";
	@FindBy(linkText="SIGN-ON")
	private WebElement lnkSignOn;
	@FindBy(linkText="REGISTER")
	private WebElement lnkRegister;
	
	public HomePage(WebDriver driver) {
		super(driver);
	}
	
	public SignOnPage clickSignOn(){
		lnkSignOn.click();
		return PageFactory.initElements(driver, SignOnPage.class);
	}

}
