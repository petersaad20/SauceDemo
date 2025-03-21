package automationFramwork.api.steps;

import automationFramwork.api.setup.ApiSetup;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import io.cucumber.java.*;
import io.restassured.RestAssured;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ApiHooks extends ApiSetup {
    public ApiHooks() throws IOException {
        super();
    }
    private static Scenario currentScenario;

    @Before
    public void setUpApis(Scenario scenario) {
        initializeApi();
        logger = extent.createTest(scenario.getName(), "");
        parameters=null;
        request = RestAssured.given();
        requestBody = null;
        headers = null;
        parameters = null;
        requestBodyJson = null;
        pathParameters=null;
        requestBodyJsonObj=null;
        currentScenario = scenario;
    }
    public static Scenario getCurrentScenario() {
        return currentScenario;
    }

    @After
    public static void tearDown(Scenario scenario) throws IOException {

        if (scenario.isFailed()) {
            logger.log(Status.FAIL, scenario.getName() + " Scenario Failed");
            logger.log(Status.FAIL , "<a href='file:/C:/Users/lenovo/food-safety/qa/psh/request.txt/request.txt'>Download Request</a>");
            logger.log(Status.FAIL , "<a href='file:/C:/Users/lenovo/food-safety/qa/psh/response.txt'>Download Response</a>");
        }
        else {
            logger.log(Status.PASS, scenario.getName() + " Scenario Passed");
            logger.log(Status.PASS , "<a href='file:/C:/Users/lenovo/food-safety/qa/psh/request.txt'>Download Request</a>");
            logger.log(Status.PASS , "<a href='file:/C:/Users/lenovo/food-safety/qa/psh/response.txt'>Download Response</a>");
        }

    }
    @After
    public static void tearDownTest(Scenario scenario) {
        String details = "Request & Response Details\n";
        if (!(headers == null)) {
            details = details + "Request Headers: \n" + new JSONObject(headers).toString(1) + "\n";
        } else {
            details = details + "Request Headers: None\n";
        }

        if (!(parameters == null)) {
            details = details + "Request Parameters: \n" + new JSONObject(parameters).toString(1) + "\n";
        } else {
            details = details + "Request Parameters: None\n";
        }
        if (!(pathParameters == null)) {
            details = details + "Request Path Parameters: \n" + new JSONObject(pathParameters).toString(1) + "\n";
        } else {
            details = details + "Request Path Parameters: None\n";
        }
        if (!(requestBody == null)) {
            details = details + "Request Body: \n" + requestBody.toString(1) + "\n";
        } else {
            details = details + "Request Body: None\n";
        }
        details = details + "\n";

        if (!(url == null || resourceName == null)) {
            details = details + "Base URL: " + url + "\n";
            details = details + "ResourceName: " + resourceName + "\n";
        }
        details = details + "\n";

        if (!(requestBodyJsonObj == null)) {
            details = details + "Request Body: \n" + requestBodyJsonObj.toString(1) + "\n";
        } else {
            details = details + "Request Body: None\n";
        }

        details = details + "\n";
        if (!(validateResponse == null)) {
            details = details +
                    "Response Status: " + validateResponse.extract().statusLine() + "\n";
            details = details + "Response Headers: \n" + validateResponse.extract().headers().toString() + "\n";
            details = details + "Response Body: \n" + validateResponse.extract().body().asPrettyString() + "\n";
        } else {
            details = details + "Response Headers: None\n";
        }

        scenario.attach(details, "text/plain", "API Request And Response Details");
    }


    @BeforeAll
    public static void initiate() throws IOException {
        prop = new Properties();
        FileInputStream fis = new FileInputStream(System.getProperty("user.dir") + "/src/main/resources/configFiles/APIConfiguration.properties");
        prop.load(fis);
        //excelData = Utils.getExcelData(prop.getProperty("ExcelSheetName"));
        htmlReporter = new ExtentHtmlReporter(prop.getProperty("ApiReportPath"));
        extent = new ExtentReports();
        extent.attachReporter(htmlReporter);
        extent.setSystemInfo("OS", "Windows");
        extent.setSystemInfo("Owner", "DM_Qpros");
        extent.setSystemInfo("Test Name", "AutomationFramework");
        extent.setSystemInfo("Tools", "RestAssured with JAVA");
        extent.setSystemInfo("Framework Design", "Maven , TestNG and Cucumber");

    }

    @AfterAll
    public static void turnOff() {
        extent.flush();
    }





}
