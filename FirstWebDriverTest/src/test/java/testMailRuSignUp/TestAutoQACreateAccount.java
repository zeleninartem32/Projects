package testMailRuSignUp;

import org.junit.Assert;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.*;
import pages.*;

import java.util.concurrent.TimeUnit;

/**
 * Created by Fox on 01.12.2016.
 */
public class TestAutoQACreateAccount {
    private WebDriver driver;

    private final String strTarjetURL = "https://mail.ru";
    private final String strFirstName = "robot";
    private final String strLastName = "mailers";
    private final String strBirthday = "12.02.1998";

    private String[] genBirt(){

        return strBirthday.split("[.]");
    }

    //This method generate password mixing user name (first/last) and birthday
    //In data: none
    //return: type String contains generated password
    private String genPasswd(){
        String strPass1=(strFirstName+strLastName).toLowerCase();
        String strPass2=genBirt()[0]+genBirt()[1]+genBirt()[2];

        strPass1 = strPass1.substring(0,1).toUpperCase()+strPass1.substring(1);
        strPass1 = strPass1.substring(0,strPass1.length()-1)+strPass1.substring(strPass1.length()-1).toUpperCase();

        String strPasswd ="";
        System.out.println(strPass1);
        System.out.println(strPass2);
        for (int i=0,ii=0,jj=0;i<(strPass1+strPass2).length();i++){
            if (ii<strPass1.length()){
                strPasswd+=strPass1.substring(ii,ii+1);
                ii++;
            }
            if (jj<strPass2.length()){
                strPasswd+=strPass2.substring(jj,jj+1);
                jj++;
            }
        }
        return strPasswd;
    }


    @BeforeTest // initializing system
                // create instance
    public void init() {
        System.setProperty("webdriver.gecko.driver","C:\\Program Files\\Mozilla Firefox\\wires.exe");
        driver = new RemoteWebDriver(DesiredCapabilities.firefox());
        driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
        driver.get(strTarjetURL);
    }

    @Test
    public void testMailRuAccountCreate() {
        String[] strBirth = genBirt();
        String strTarjetString = strFirstName + "." + strLastName + "." + "male" + "." + strBirth[2]+"@mail.ru";

        AutoQACreateAccount objCreateAccount = new AutoQACreateAccount(driver);
        objCreateAccount.createAutoQAAccount(strFirstName,strLastName,genPasswd(),strBirth);

        AutoQAHomePage objHomePage = new AutoQAHomePage(this.driver);
        Assert.assertTrue(objHomePage.getLoginUserNameFromDashboard().contains(strTarjetString));
    }

    @AfterTest //close browser and destroy driver
    public void unInit() {
        // Close all browser windows and safely end the session
//        driver.quit();
    }
}
