package testMailRuSignUp;

import cn.easyproject.easyocr.EasyOCR;
import cn.easyproject.easyocr.ImageType;
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
    WebDriver driver;
    AutoQACreateAccount objCreateAccount;

    String strFirstName = "robot";
    String strLastName = "mailers";
    String strBirthday = "12.02.1998";


    private String[] genBirt(){

        return strBirthday.split("[.]");
    }

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

    private String reCaptcha(String strFilename){
        EasyOCR ocr = new EasyOCR();
        String result=ocr.discernAndAutoCleanImage(strFilename, ImageType.CAPTCHA_HOLLOW_CHAR);
/*        File imageFile = new File(strFilename);
        Tesseract instance = Tesseract.getInstance(); // JNA Interface Mapping
        instance.setDatapath("d:\\temp\\tessdata");
        String result="";
        try {
            result = instance.doOCR(imageFile);
            System.out.println(result);
        } catch (TesseractException e) {
            System.err.println(e.getMessage());
        }
*/
        return result;
    }

//    @BeforeTest // initializing system
    public void init() {
        System.setProperty("webdriver.gecko.driver","C:\\Program Files\\Mozilla Firefox\\wires.exe");
        driver = new RemoteWebDriver(DesiredCapabilities.firefox());
        driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
        driver.get("https://mail.ru");
    }


    @Test
    public void testMailRuAccountCreate() {
//        System.setProperty("java.io.tmpdir","D:\\Temp");
//        System.out.println(System.getProperty("java.io.tmpdir"));
//        System.out.println(reCaptcha("C:\\Users\\Fox\\Pictures\\2.png"));
        objCreateAccount = new AutoQACreateAccount(driver);
        objCreateAccount.createAutoQAAccount(strFirstName,strLastName,genPasswd(),genBirt());

    }

    @AfterTest //close browser and destroy driver
    public void unInit() {
        // Close all browser windows and safely end the session
//        driver.quit();
    }
}
