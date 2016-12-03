package pages;

import cn.easyproject.easyocr.EasyOCR;
import cn.easyproject.easyocr.ImageType;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Fox on 01.12.2016.
 */
public class AutoQACreateAccount {
    private String strTempDir = System.getProperty("java.io.tmpdir");
    private String strImageType = "png";
    private String strScreenShotFileName = "1" + "." + strImageType;
    private String strCAPTCHAFileName = "2" + "." + strImageType;

    private WebDriver driver;

    private By regUrl = By.id("PH_regLink");
    private By inPutClass = By.xpath("//label[text()='Имя']");
    private By classFirstName = By.xpath("//label[text()='Имя']");
    private By classLastName = By.xpath("//label[text()='Фамилия']");
    private By selectDay = By.xpath("//select[contains(@class,'qc-select-day')]");
    private By selectMonth = By.xpath("//select[contains(@class,'qc-select-month')]");
    private By selectYear = By.xpath("//select[contains(@class,'qc-select-year')]");
    private By classLogin = By.xpath("//label[text()='Почтовый ящик']");
    private By classPasswd = By.xpath("//label[text()='Пароль']");
    private By classConfPasswd = By.xpath("//label[text()='Повторите пароль']");
    private By confirmButton = By.xpath("//button[contains(@value,'Зарегистрироваться')]");
    private By getSelectGender = By.xpath("//input[contains(@type,'radio')]");

    private By captchaInput = By.xpath("//input[contains(@name,'code')]");
    private By captchaImg = By.xpath("//img[contains(@class,'js-captchaImage')]");
    private By confCapButton = By.xpath("//button[contains(@class,'confirm-ok')]");

    //Method waiting to page loadin and visibily element
    //In data: instance of WebDriver; and WebElement to wait
    //return: none
    private void waitToPageLoad(WebDriver driver, By element) {
        (new WebDriverWait(driver, 10)).until(ExpectedConditions.visibilityOfElementLocated(element));
    }

    // Private section of methods
    //Method create screenshot and crop captcha on it, save images to temp directory
    //In data: type String contains image type (png, jpg etc.) screenshot image file name
        // and captcha image filename
    //return: none
    private void createCapImageFromScreenShot(String strImageType,
                                              String strScreenShotFileName,
                                              String strCAPTCHAFileName) {
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
            BufferedImage image = ImageIO.read(new File(strScreenShotFileName));
            image = image.getSubimage(start.x, start.y, section.width, section.height);
            ImageIO.write(image, strImageType, new File(strCAPTCHAFileName));

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //This method return text from security image
    //In data: type String contains File name
    //return: type String contains recognized text
    private String reCaptcha(String strFilename) {
        EasyOCR ocr = new EasyOCR();
        return ocr.discernAndAutoCleanImage(strFilename, ImageType.CAPTCHA_HOLLOW_CHAR);
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
    public void setUserName(String srtFirstName, String strLastName) {
        By elFirstName = By.id(driver.findElement(this.classFirstName).getAttribute("for").toString());
        By elLastName = By.id(driver.findElement(this.classLastName).getAttribute("for").toString());
        driver.findElement(elFirstName).sendKeys(srtFirstName);
        driver.findElement(elLastName).sendKeys(strLastName);
    }

    //Method set password on account form
    //In data: type String contains password
    //return: none
    public void setPasswd(String strPasswd) {
        By elPasswd = By.id(driver.findElement(this.classPasswd).getAttribute("for").toString());
        By elConfPasswd = By.id(driver.findElement(this.classConfPasswd).getAttribute("for").toString());
        driver.findElement(elPasswd).sendKeys(strPasswd);
        driver.findElement(elConfPasswd).sendKeys(strPasswd);
    }

    //Method set user login on account form
    //In data: type String contains login
    //return: none
    public void setLogin(String strLogin) {
        By elLogin = By.id(driver.findElement(this.classLogin).getAttribute("for").toString());
        driver.findElement(elLogin).sendKeys(strLogin);
    }

    //Method select day, month, year on account form
    //In data: type String contains day, month,year
    //return: none
    public void setBirthDay(String birthDay, String birthMonth, String birthYear) {
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
    public void setGenderMale() {
        driver.findElement(this.getSelectGender).click();
    }

    //Method submitting data on account form
    //In data: none
    //return: none
    public void clickSubmitButton() {
        driver.findElement(this.confirmButton).click();
    }

    //Method set CAPTCHA text on account form
    //In data: none
    //return: none

    public void captchaForm() {

        waitToPageLoad(this.driver, captchaImg);

        createCapImageFromScreenShot(strImageType,strScreenShotFileName,strCAPTCHAFileName);

        driver.findElement(captchaInput).sendKeys(reCaptcha(strCAPTCHAFileName));
//        driver.findElement(confCapButton).click();

    }
    //Constructor
    public void createAutoQAAccount(String firstName, String lastName, String passwd,
                                    String[] strBirthDay) {
        this.driver.findElement(regUrl).click(); //Search registration URL and click on it

        waitToPageLoad(this.driver, confirmButton); //Wait for page load

        this.setUserName(firstName, lastName);//Set user name

        this.setBirthDay(strBirthDay[0], strBirthDay[1], strBirthDay[2]);//Select birthday

        this.setGenderMale();//Select gender

        this.setLogin(firstName + "." + lastName + "." + "male" + "." + strBirthDay[2]);//Set user name login

        this.setPasswd(passwd); // Set password

        clickSubmitButton();//Submitting
        captchaForm();//Generate and submitting CAPTCHA text
    }

}
