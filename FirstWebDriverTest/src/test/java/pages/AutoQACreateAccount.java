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

    private void waitToPageLoad(WebDriver driver, By element) {
        (new WebDriverWait(driver, 10)).until(ExpectedConditions.visibilityOfElementLocated(element));
    }

    // Private section of methods
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

    private String reCaptcha(String strFilename) {
        EasyOCR ocr = new EasyOCR();
        return ocr.discernAndAutoCleanImage(strFilename, ImageType.CAPTCHA_HOLLOW_CHAR);
    }

    // Public section of methods

    public AutoQACreateAccount(WebDriver driver) {
        this.driver = driver;
    }

    public void setUserName(String srtFirstName, String strLastName) {
        By elFirstName = By.id(driver.findElement(this.classFirstName).getAttribute("for").toString());
        By elLastName = By.id(driver.findElement(this.classLastName).getAttribute("for").toString());
        driver.findElement(elFirstName).sendKeys(srtFirstName);
        driver.findElement(elLastName).sendKeys(strLastName);
    }

    public void setPasswd(String strPasswd) {
        By elPasswd = By.id(driver.findElement(this.classPasswd).getAttribute("for").toString());
        By elConfPasswd = By.id(driver.findElement(this.classConfPasswd).getAttribute("for").toString());
        driver.findElement(elPasswd).sendKeys(strPasswd);
        driver.findElement(elConfPasswd).sendKeys(strPasswd);
    }

    public void setLogin(String strLogin) {
        By elLogin = By.id(driver.findElement(this.classLogin).getAttribute("for").toString());
        driver.findElement(elLogin).sendKeys(strLogin);
    }

    public void setBirthDay(String birthDay, String birthMonth, String birthYear) {
        Select selectDay = new Select(driver.findElement(this.selectDay));
        selectDay.selectByValue(birthDay);
        Select selectMonth = new Select(driver.findElement(this.selectMonth));
        selectMonth.selectByIndex(Integer.parseInt(birthMonth));
        Select selectYear = new Select(driver.findElement(this.selectYear));
        selectYear.selectByValue(birthYear);
    }

    public void setGenderMale() {
        driver.findElement(this.getSelectGender).click();
    }

    public void clickSubmitButton() {
        driver.findElement(this.confirmButton).click();
    }


    public void captchaForm() {

        waitToPageLoad(this.driver, captchaImg);

        createCapImageFromScreenShot(strImageType,strScreenShotFileName,strCAPTCHAFileName);

        driver.findElement(captchaInput).sendKeys(reCaptcha(strCAPTCHAFileName));
//        driver.findElement(confCapButton).click();

    }

    public void createAutoQAAccount(String firstName, String lastName, String passwd,
                                    String[] strBirthDay) {
        this.driver.findElement(regUrl).click();

        waitToPageLoad(this.driver, confirmButton);

        this.setUserName(firstName, lastName);

        this.setBirthDay(strBirthDay[0], strBirthDay[1], strBirthDay[2]);

        this.setGenderMale();

        this.setLogin(firstName + "." + lastName + "." + "male" + "." + strBirthDay[2]);

        this.setPasswd(passwd);

        clickSubmitButton();
        captchaForm();
    }

}
