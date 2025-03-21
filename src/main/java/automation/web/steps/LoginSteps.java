package automation.web.steps;

import automation.web.common.BrowserFactory;
import automation.web.common.CommonFunctions;
import automation.web.setup.BaseTest;
import automation.web.webpages.LoginPage;
import automation.web.webutils.Utils;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriverException;
import org.testng.Assert;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.Assert.assertTrue;

public class LoginSteps extends BaseTest {
    LoginPage loginPage = new LoginPage();
    String oldImagePath;
    String image;
    String newImagePath;
    Utils utils;

    @Given("User Navigated to URL")
    public void user_navigated_to_url() throws InterruptedException {
        commonFunctions = new CommonFunctions();
        String browser = System.getProperty("BROWSER");
        if (System.getProperty("os.name").toLowerCase().contains("win")) {
            BrowserFactory.createDriver(browser);
            System.out.println("Chrome started from Windows");
        } else {
            BrowserFactory.createDriver(browser);
            System.out.println("Chrome started from Linux");
        }
        if (reportingType.equalsIgnoreCase("extend report")) {
            logger = extent.createTest(currentScenario.getName(), "");
        }
        if (generalCofigsProps.getProperty("env").equalsIgnoreCase("stg")) {
            commonFunctions.navigateToURL(System.getProperty("URL"));
        } else if (generalCofigsProps.getProperty("env").equalsIgnoreCase("dev")) {
            commonFunctions.navigateToURL(generalCofigsProps.getProperty("DevURL"));
        } else {
            commonFunctions.navigateToURL(generalCofigsProps.getProperty("ProdURL"));
        }
    }

    @Given("User Navigated to URL {string}")
    public void user_navigated_to_url(String url) throws InterruptedException {
        commonFunctions = new CommonFunctions();
        String browser = System.getProperty("BROWSER");
        if (System.getProperty("os.name").toLowerCase().contains("win")) {
            BrowserFactory.createDriver(browser);
            System.out.println("Chrome started from Windows");
        } else {
            BrowserFactory.createDriver(browser);
            System.out.println("Chrome started from Linux");
        }
        if (reportingType.equalsIgnoreCase("extend report")) {
            logger = extent.createTest(currentScenario.getName(), "");
        }
        commonFunctions.navigateToURL(url);
    }


    @When("The Customer Logs in With Username {string} Password {string}")
    public void theCustomerLogsIn(String userName, String password) {
        loginPage.theCustomerLogsIn(userName, password);
    }

    @Then("Then User Should See {string}")
    public void thenUserShouldSee(String expectedResult) {
        loginPage.validateErrorMsg(expectedResult);
    }


    @And("I have a baseline image of the product listing page")
    public void iHaveABaselineImage() {
         utils = new Utils();
        oldImagePath =utils.takeVisiableAreaSnapshot(screenshotPath,"take baseline Screen Shot");
    }
    @And("I load the product listing page again")
    public void loadProductListingPageAgain() {
        driver.navigate().refresh();
    }

    @Then("I should compare the current screenshot with the baseline image")
    public void compareCurrentScreenshot() {
        try {
            File baselineImageFile = new File(oldImagePath);
            System.out.println("SSSS"+baselineImageFile);
            BufferedImage baselineImage = ImageIO.read(baselineImageFile);

            newImagePath=utils.takeVisiableAreaSnapshot(screenshotPath,"take new Screen Shot");

            File newScreenshotFile = new File(newImagePath);
            BufferedImage newScreenshotImage = ImageIO.read(newScreenshotFile);

            // Compare the two images pixel by pixel
            if (baselineImage.getWidth() != newScreenshotImage.getWidth() || baselineImage.getHeight() != newScreenshotImage.getHeight()) {
                System.out.println("The images have different dimensions.");
                Assert.fail("Visual regression detected: The images have different dimensions.");
            }

            for (int x = 0; x < baselineImage.getWidth(); x++) {
                for (int y = 0; y < baselineImage.getHeight(); y++) {
                    if (baselineImage.getRGB(x, y) != newScreenshotImage.getRGB(x, y)) {
                        System.out.println("The images have different pixels at (" + x + "," + y + ")");
                        Assert.fail("Visual regression detected: Pixel mismatch at (" + x + "," + y + ")");
                    }
                }
            }

            System.out.println("The images match. No visual regression detected.");
        } catch (IOException e) {
            e.printStackTrace();
            Assert.fail("Error during image comparison.");
        }
    }


    @And("Check Page Load Time below {int}")
    public void checkPageLoadTime(int time) {
        boolean timeFlag =commonFunctions.checkPageLoadTime(time);
        System.out.println("timeFlag"+timeFlag);
        if(!timeFlag){
            for (int retryCount =0; retryCount< 3;retryCount++){
                driver.navigate().refresh();
            }
        }
    }


}







