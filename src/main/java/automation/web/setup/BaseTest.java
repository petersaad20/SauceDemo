package automation.web.setup;

import automation.web.common.CommonFunctions;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import constants.GeneralConstants;
import io.cucumber.java.Scenario;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;
import org.testng.asserts.SoftAssert;
import util.PropertiesFilesHandler;

import java.util.Properties;

public class BaseTest {
    public static WebDriver driver;
    public static ExtentTest logger;
    public static ExtentHtmlReporter htmlReporter;
    public static ExtentReports extent;
    public static Actions action;
    public static SoftAssert softAssert = new SoftAssert();
    public static Scenario currentScenario;
    static PropertiesFilesHandler propHandler = new PropertiesFilesHandler();
    public static Properties generalCofigsProps = propHandler.loadPropertiesFile(GeneralConstants.GENERAL_CONFIG_FILE_NAME);
    public static String reportingType = generalCofigsProps.getProperty("reportingType");
    public static String screenshotDictionary = generalCofigsProps.getProperty("SnapchatAndVideosPath");
    public String screenshotCucumberDictionary = generalCofigsProps.getProperty("SnapchatAndVideosPathCucumber");
    public String screenshotPath = generalCofigsProps.getProperty("SnapchatPath");
    public static CommonFunctions commonFunctions;
    public static final Logger log = LogManager.getLogger(CommonFunctions.class);

    public void getScenario(Scenario scenario){
        currentScenario=scenario;
    }


}
