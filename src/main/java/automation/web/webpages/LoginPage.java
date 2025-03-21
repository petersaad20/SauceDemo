package automation.web.webpages;

import automation.web.setup.BaseTest;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

import java.net.HttpURLConnection;
import java.net.URL;

import static org.junit.Assert.assertFalse;

public class LoginPage extends BaseTest {
    long startTime;
    long endTime;


    public By USER_NAME = By.xpath("//*[@id=\"user-name\"]");
    public By PASSWORD = By.xpath("//*[@id=\"password\"]");
    public By LOGIN_BUTTON = By.xpath("//*[@id=\"login-button\"]");
    public By ERROR_MSG = By.xpath("//*[@id=\"login_button_container\"]/div/form/h3");
    public By PRODUCT_LABEL = By.xpath("//*[@id=\"inventory_filter_container\"]/div");
    public By IMG_TAG = By.tagName("img");


    public void theCustomerLogsIn(String userName, String password) {
        commonFunctions.sendText(USER_NAME, userName, "Enter User Name");
        commonFunctions.sendText(PASSWORD, password, "Enter Password");
        commonFunctions.clickOnElement(LOGIN_BUTTON, "Click on Login Button");
        startTime = System.currentTimeMillis();
    }

    public void validateErrorMsg(String expectedResult) {
        commonFunctions.checkPageLoadTime(5000);
        if (expectedResult.equalsIgnoreCase("Sucess")) {
            commonFunctions.checkURL("https://www.saucedemo.com/v1/inventory.html");
            commonFunctions.isElementDisplayed(PRODUCT_LABEL, "Check If Products Element is displayed");
        } else if (expectedResult.contains("Sorry")) {
            commonFunctions.verifyTextEquality(commonFunctions.getText(ERROR_MSG, "Get Error Message"), expectedResult);
        } else if (expectedResult.contains("Validate if images are broken after login ")) {
            commonFunctions.checkIfImageIsBroken(IMG_TAG, "Check Broken Image");
        }
    }




}
