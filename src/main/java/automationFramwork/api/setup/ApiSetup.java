package automationFramwork.api.setup;

import automationFramwork.api.basesclasses.CommonFunctions;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

public class ApiSetup {
    public static ExtentTest logger;
    public static ExtentHtmlReporter htmlReporter;
    public static ExtentReports extent;
    public static CommonFunctions commonFunctions;
    public static String resourceName;
    public static String requestHeaderTemplate;
    public static String requestBodyJson;
    public static RequestSpecification request;
    public static JSONObject requestBody;
    public static String headerTemplatesFolderPath;
    public static String bodyTemplatesFolderPath;
    public static String jsonResponses;
    public static String getProductResource;
    public static Map<String, String> headers;
    public static Map<String, String> parameters;
    public static Map<String, String> pathParameters;
    public static JSONObject requestBodyJsonObj;
    public static String requestBodyString;
    public static String url;
    public static String auth;
    public static RequestSpecification requestSpecification;
    public static ValidatableResponse validateResponse;
    public static Properties prop;
    public static String company1Trader1;
    public static String company1Trader1Password;
    public static String company1Trader2;
    public static String company1Trader2Password;
    public static String company2Trader1;
    public static String company2Trader1Password;
    public static String company2Trader2;
    public static String company2Trader2Password;
    public static String seniorReviewer;
    public static String seniorReviewerPassword;
    public static String Reviewer;
    public static String reviewerPassword;
    public static String Admin;
    public static String adminPassword;
    public static Map<String, String> variables;
    public static final Map<String, Set<String>> visitedMenus = new HashMap<>();




    public ApiSetup() throws IOException {
        prop = new Properties();
        FileInputStream fis = new FileInputStream(".//src//main//resources//configFiles//APIConfiguration.properties");
        prop.load(fis);
    }

    public static void initializeApi() {
        headerTemplatesFolderPath = prop.getProperty("headerTemplatesFolderPath");
        bodyTemplatesFolderPath = prop.getProperty("bodyTemplatesFolderPath");
        jsonResponses = prop.getProperty("jsonResponses");
        getProductResource = prop.getProperty("getProductResource");
        url = prop.getProperty("baseURL");
        auth = prop.getProperty("Auth");

        company1Trader1 = prop.getProperty("");
        company1Trader1Password = prop.getProperty("");

        company1Trader2 = prop.getProperty("");
        company1Trader2Password = prop.getProperty("");

        company2Trader1 = prop.getProperty("");
        company2Trader1Password = prop.getProperty("");

        company2Trader2 = prop.getProperty("");
        company2Trader2Password = prop.getProperty("");

        seniorReviewer = prop.getProperty("");
        seniorReviewerPassword = prop.getProperty("");

        Reviewer = prop.getProperty("");
        reviewerPassword = prop.getProperty("");

        Admin = prop.getProperty("");
        adminPassword = prop.getProperty("");


    }


}
