package pages;

import cn.easyproject.easyocr.EasyOCR;
import cn.easyproject.easyocr.ImageType;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import static java.lang.Math.random;

/**
 * Created by Fox on 01.12.2016.
 */
public class AutoQACreateAccount {
    private final String strImageType = "png";
    private final String strScreenShotFileName = "1" + "." + strImageType;
    private final String strCAPTCHAFileName = "2" + "." + strImageType;

    private final WebDriver driver;

    private final By regUrl = By.id("PH_regLink");
    private final By classFirstName = By.xpath("//label[text()='Имя']");
    private final By classLastName = By.xpath("//label[text()='Фамилия']");
    private final By selectDay = By.xpath("//select[contains(@class,'qc-select-day')]");
    private final By selectMonth = By.xpath("//select[contains(@class,'qc-select-month')]");
    private final By selectYear = By.xpath("//select[contains(@class,'qc-select-year')]");
    private final By classLogin = By.xpath("//label[text()='Почтовый ящик']");
    private final By classLoginFild = By.id("loginField");
    private final By classLoginSuccess = By.xpath("//span[@class,'success')]");

    private final By classPasswd = By.xpath("//label[text()='Пароль']");
    private final By classConfPasswd = By.xpath("//label[text()='Повторите пароль']");
    private final By confirmButton = By.xpath("//button[contains(@value,'Зарегистрироваться')]");
    private final By getSelectGender = By.xpath("//input[contains(@type,'radio')]");

    private final By captchaInput = By.xpath("//input[contains(@name,'code')]");
    private final By captchaImg = By.xpath("//img[contains(@class,'js-captchaImage')]");
    private final By confCapButton = By.xpath("//button[contains(@class,'confirm-ok')]");
    private final By confErrorMessage = By.xpath("//div[contains(@class,'js-error form__message_error')]");


    //Method waiting to page loadin and visibily element
    //In data: instance of WebDriver; and WebElement to wait
    //return: none
    private void waitToPageLoad(WebDriver driver, By element) {
        (new WebDriverWait(driver, 10)).until(ExpectedConditions.visibilityOfElementLocated(element));
    }


    //Method create screenshot and crop captcha on it, save images to temp directory
    //In data: type String contains image type (png, jpg etc.) screenshot image file name
    // and captcha image filename
    //return: none
    private void createCapImageFromScreenShot(String strImageType,
                                              String strScreenShotFileName,
                                              String strCAPTCHAFileName) {

        String strTempDir = System.getProperty("java.io.tmpdir");
        byte[] imgBase64Png = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
        try {
            FileOutputStream imageOutFile = new FileOutputStream(strTempDir + File.separator + strScreenShotFileName);

            imageOutFile.write(imgBase64Png);

            imageOutFile.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        Point start = driver.findElement(captchaImg).getLocation();
        Dimension section = driver.findElement(captchaImg).getSize();

        try {
            BufferedImage image = ImageIO.read(new File(strTempDir + File.separator + strScreenShotFileName));
            image = image.getSubimage(start.x, start.y, section.width, section.height);
            ImageIO.write(image, strImageType, new File(strTempDir + File.separator + strCAPTCHAFileName));

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //This method return text from security image
    //In data: type String contains File name
    //return: type String contains recognized text
    private String reCaptcha(String strFilename) {
        System.setProperty("java.io.tmpdir", "D:\\Temp");

        createCapImageFromScreenShot(strImageType, strScreenShotFileName, strCAPTCHAFileName);

        EasyOCR ocr = new EasyOCR();
        return ocr.discernAndAutoCleanImage(System.getProperty("java.io.tmpdir") + File.separator + strFilename, ImageType.CAPTCHA_WHITE_CHAR);
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
    }

    // Public section of methods
    //Constructor
    //In data: instance of WebDriver
    //return: none
    public AutoQACreateAccount(WebDriver driver) {
        this.driver = driver;
    }

    //Method set user name (first and last) on account form
    //In data: type String contains first and last name
    //return: none
    private void setUserName(String srtFirstName, String strLastName) {
        By elFirstName = By.id(driver.findElement(this.classFirstName).getAttribute("for").toString());
        By elLastName = By.id(driver.findElement(this.classLastName).getAttribute("for").toString());
        driver.findElement(elFirstName).sendKeys(srtFirstName);
        driver.findElement(elLastName).sendKeys(strLastName);
    }

    //Method set password on account form
    //In data: type String contains password
    //return: none
    private void setPasswd(String strPasswd) {
        By elPasswd = By.id(driver.findElement(this.classPasswd).getAttribute("for").toString());
        By elConfPasswd = By.id(driver.findElement(this.classConfPasswd).getAttribute("for").toString());
        driver.findElement(elPasswd).sendKeys(strPasswd);
        driver.findElement(elConfPasswd).sendKeys(strPasswd);
    }

    //Method set user login on account form
    //In data: type String contains login
    //return: none
    private void setLogin(String strLogin) {
        By elLogin = By.id(driver.findElement(this.classLogin).getAttribute("for").toString());

        driver.findElement(elLogin).sendKeys(strLogin);

//        if (this.driver.findElement(By.id("loginField")).getAttribute("class").contains("sig-success-on")))
    }

    //Method select day, month, year on account form
    //In data: type String contains day, month,year
    //return: none
    private void setBirthDay(String birthDay, String birthMonth, String birthYear) {
        Select selectDay = new Select(driver.findElement(this.selectDay));
        selectDay.selectByValue(birthDay);
        Select selectMonth = new Select(driver.findElement(this.selectMonth));
        selectMonth.selectByIndex(Integer.parseInt(birthMonth));
        Select selectYear = new Select(driver.findElement(this.selectYear));
        selectYear.selectByValue(birthYear);
    }

    //Method set male gender on account form
    //In data: none
    //return: none
    private void setGenderMale() {
        driver.findElement(this.getSelectGender).click();
    }

    //Method submitting data on account form
    //In data: none
    //return: none
    private void clickSubmitButton() {
        driver.findElement(this.confirmButton).click();
    }

    //Method set CAPTCHA text on account form
    //In data: none
    //return: none

    private void captchaForm() {
        int count = 10;
        do {

            count--;
            waitToPageLoad(this.driver, captchaImg);

            driver.findElement(captchaInput).sendKeys(reCaptcha(strCAPTCHAFileName));
            driver.findElement(confCapButton).click();
        }
        while ((!driver.findElement(confErrorMessage).getText().equals(""))||count>0);
    }

    public void createAutoQAAccount(String firstName, String lastName, String passwd,
                                    String[] strBirthDay) {
//        WebDriverWait wdWait = new WebDriverWait(driver,5);

        this.driver.findElement(regUrl).click(); //Search registration URL and click on it

        waitToPageLoad(this.driver, confirmButton); //Wait for page load

        this.setUserName(firstName, lastName);//Set user name

        this.setBirthDay(strBirthDay[0], strBirthDay[1], strBirthDay[2]);//Select birthday

        this.setGenderMale();//Select gender

        this.setLogin(firstName + "." + lastName + "." + "male" + "." + strBirthDay[2]);//Set user name login
//        wdWait.until(ExpectedConditions.visibilityOf(driver.findElement(classLoginFild).findElement(classLoginSuccess)));

        this.setPasswd(passwd); // Set password

        clickSubmitButton();//Submitting
        captchaForm();//Generate and submitting CAPTCHA text
    }

}
