package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * Created by Fox on 03.12.2016.
 */
public class AutoQAHomePage {
    private WebDriver driver;
    private final By strUserEmail = By.id("PH_user-email");

    public AutoQAHomePage(WebDriver driver) {
        this.driver = driver;
    }

    public String getLoginUserNameFromDashboard(){
        return driver.findElement(this.strUserEmail).getText();
    }
}
