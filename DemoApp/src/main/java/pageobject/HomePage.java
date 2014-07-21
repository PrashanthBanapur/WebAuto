package pageobject;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.seltest.core.Config;
import org.seltest.core.PageObject;
import org.seltest.core.StartState;

public class HomePage extends PageObject implements StartState{

	public static final String URL = Config.baseUrl.getValue()+"mercurywelcome.php";
	@FindBy(linkText="SIGN-ON")
	private WebElement lnkSignOn;
	@FindBy(linkText="SIGN-OFF")
	private WebElement lnkSignOff;
	@FindBy(linkText="REGISTER")
	private WebElement lnkRegister;
	
	public HomePage(WebDriver driver) {
		super(driver);
	}
	
	public SignOnPage clickSignOn(){
		element.click(lnkSignOn);
		return PageFactory.initElements(driver, SignOnPage.class);
	}

	public void clickSignOff() {
		element.click(lnkSignOff);
		
	}

	@Override
	public Boolean isStartState() {
		if(element.isDisplayed(lnkSignOn)){
			return true;
		}else{
			return false;
		}
		
	}

	@Override
	public void goToStartPage() {
		element.click(lnkSignOff);
	}

}
