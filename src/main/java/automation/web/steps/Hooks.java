package automation.web.steps;

import automation.web.common.BrowserFactory;
import automation.web.setup.BaseTest;
import automation.web.webutils.Utils;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import io.cucumber.java.*;
import org.testng.TestNG;
import org.testng.collections.Lists;
import org.testng.xml.XmlSuite;
import org.testng.xml.XmlTest;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class Hooks extends BaseTest {
    public Hooks() throws IOException {
        super();
    }

    @BeforeAll
    public static void initiate() {
        deleteFile(generalCofigsProps.getProperty("SnapchatAndVideosPathCucumber"));
        deleteFile(generalCofigsProps.getProperty("SnapchatAndVideosPath"));
        if (reportingType.equalsIgnoreCase("extend report")) {
            htmlReporter = new ExtentHtmlReporter(generalCofigsProps.getProperty("ResultsReportPath"));
            htmlReporter.config().setEncoding("utf-8");
            htmlReporter.config().setDocumentTitle("Automation Report");
            htmlReporter.config().setReportName("Automation Test Results");
            htmlReporter.config().setTheme(Theme.DARK);
            extent = new ExtentReports();
            extent.attachReporter(htmlReporter);
            extent.setSystemInfo("OS", "Windows");
            extent.setSystemInfo("Owner", "Peter Saad");
            extent.setSystemInfo("Test Name", "AutomationFramework");
            extent.setSystemInfo("Tools", "Selenium with JAVA");
            extent.setSystemInfo("Framework Design", "Maven , TestNG and Cucumber");
        }

    }

//    @Before("@edge or @chrome or @firefox")
//    public static void setUpBrowser(Scenario scenario) throws IOException {
//        currentScenario = scenario;
//        String browser = getBrowserFromTag(scenario);
//        initialization(browser);
//        if (reportingType.equalsIgnoreCase("extend report")) {
//            logger = extent.createTest(scenario.getName(), "");
//        }
//    }

    @AfterAll
    public static void turnOff() {
        if (reportingType.equalsIgnoreCase("extend report")) {
            extent.flush();
            softAssert.assertAll();
        }
    }

    private static String getBrowserFromTag(Scenario scenario) {
        if (scenario.getSourceTagNames().contains("@edge")) {
            return "edge";
        } else if (scenario.getSourceTagNames().contains("@chrome")) {
            return "chrome";
        } else {
            return "firefox"; // Default browser
        }
    }

    public static void initialization(String browser) {
        BrowserFactory.createDriver(browser);
    }

    public static void deleteFile(String fileDir){
        File screenshotsDir = new File(fileDir);
        if (screenshotsDir.exists() && screenshotsDir.isDirectory()) {
            File[] files = screenshotsDir.listFiles();
            if (files != null) {
                for (File file : files) {
                    // Delete each file
                    if (file.isFile()) {
                        file.delete();
                    }
                }
            }
        } else {
            System.out.println("Screenshots directory does not exist or is not a directory.");
        }
    }

    @Before("@edge or @chrome or @firefox")
    public void setUpBrowser(Scenario scenario) throws IOException {
        currentScenario = scenario;
        if (reportingType.equalsIgnoreCase("extend report")) {
            logger = extent.createTest(scenario.getName(), "");
        }
    }

    @After("@firefox or @chrome or @edge")
    public static void tearDown(Scenario scenario) {
        if (reportingType.equalsIgnoreCase("extend report")) {
            Utils.takeVisiableAreaSnapshot(scenario.getName());
            if (scenario.isFailed()) {
                logger.log(Status.FAIL, scenario.getName() + " Scenario Failed");
                logger.log(Status.INFO, "<a href='" + scenario.getName() + ".png" + "'><span class='label info'>Download Snapshot</span></a>");
            } else {
                logger.log(Status.PASS, scenario.getName() + " Scenario Passed");
                logger.log(Status.INFO, "<a href='" + scenario.getName() + ".png" + "'><span class='label info'>Download Snapshot</span></a>");
            }
        }
        driver.quit();
    }

//    @After("@firefox or @chrome or @edge")
    public void afterScenario(Scenario scenario) {
       int retryCount = 0;
        final int MAX_RETRY_COUNT = 3;
        if (scenario.isFailed() && retryCount < MAX_RETRY_COUNT) {
            retryCount++;
            System.out.println("Retrying failed scenario: " + scenario.getName() + " - Attempt " + retryCount);
            runScenario(scenario); // Retry the failed scenario
        } else if (scenario.isFailed()) {
            System.out.println("Scenario failed after " + MAX_RETRY_COUNT + " attempts: " + scenario.getName());
        }
    }

    // Method to manually rerun the failed scenario
    private void runScenario(Scenario scenario) {
        // Create a new instance of the TestNG runner
        TestNG testng = new TestNG();

        // Create a test suite and test to rerun the failed scenario
        List<XmlSuite> suites = Lists.newArrayList();
        XmlSuite suite = new XmlSuite();
        suite.setName("RetrySuite");
        XmlTest test = new XmlTest();
        test.setName(scenario.getName()); // Name it after the failed scenario
        suite.getTests().add(test);

        suites.add(suite);
        testng.setXmlSuites(suites);

        // Run the test suite with retry logic
        testng.run();
    }
}