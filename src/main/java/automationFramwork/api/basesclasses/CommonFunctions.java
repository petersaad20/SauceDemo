package automationFramwork.api.basesclasses;

import automationFramwork.api.setup.ApiSetup;

import automationFramwork.api.steps.ApiHooks;
import com.aventstack.extentreports.Status;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import core.JSON;
import io.cucumber.java.Scenario;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.bson.Document;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.junit.Test;

import org.testng.Assert;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static automationFramwork.api.steps.ApiHooks.getCurrentScenario;


public class CommonFunctions extends ApiSetup {
    private Supplier<RequestSpecBuilder> reqSpecSupplier;
    private Consumer<RequestSpecBuilder> reqSpecCustomizer;

    public CommonFunctions() throws IOException {
        super();
    }



    public RequestSpecification requestSpecification() throws IOException {
        try {
            PrintStream logRequest = new PrintStream(new FileOutputStream("request.txt"));
            PrintStream logResponse = new PrintStream(new FileOutputStream("response.txt"));
            request = RestAssured.given()
                    .baseUri(url)
                    .header("Authorization", "Bearer " + auth)
                    .filter(RequestLoggingFilter.logRequestTo(logRequest))
                    .filter(ResponseLoggingFilter.logResponseTo(logResponse))
                    .contentType(ContentType.JSON)
                    .log().all();
        } catch (FileNotFoundException e) {
            throw new IOException("Failed to create log files: " + e.getMessage(), e);
        }
        return request;
    }


    public void buildRequest() throws IOException, JSONException {
        request = requestSpecification();
        if (headers != null) {
            logRequestInfo("Request Headers", headers.toString());
            for (String headerParameterName : headers.keySet()) {
                request.header(headerParameterName, headers.get(headerParameterName));
                logger.info("Request Headers " + request);
            }
        }
        if (parameters != null) {
            logRequestInfo("Request Parameters", parameters.toString());
            for (String parameterName : parameters.keySet()) {
                request.param(parameterName, parameters.get(parameterName));
            }
        }
        if (pathParameters != null) {
            logRequestInfo("Request Parameters", pathParameters.toString());
            for (String parameterName : pathParameters.keySet()) {
                request.pathParam(parameterName, pathParameters.get(parameterName));
            }
        }
        if (requestBodyString != null) {
            logRequestInfo("Request Body", requestBodyString);
            try {
                JSONObject requestBodyJsonObj = new JSONObject(requestBodyString);
                request.body(requestBodyJsonObj);
            } catch (JSONException e) {
                throw new IOException("Failed to parse JSON request body: " + e.getMessage(), e);
            }
        } else if (requestBodyJsonObj != null) {
            logRequestInfo("Request Body", requestBodyJsonObj.toString());
            try {
                request.body(requestBodyJsonObj.toString());
            } catch (JSONException e) {
                throw new IOException("Failed to parse JSON request body: " + e.getMessage(), e);
            }
        }
        // If no headers, parameters, or request body is provided, use the existing request specification
        if (headers == null && parameters == null && requestBodyString == null && requestBodyJsonObj == null) {
            logRequestInfo("request has no request body  & headers ", request.toString());
            request = requestSpecification().filter(new RequestLoggingFilter());
        }
    }

    public void setRequestHeaders(String fileName) {
        try {
            headers = new ObjectMapper().readValue(
                    getFileContents(System.getProperty("user.dir") + headerTemplatesFolderPath + fileName), HashMap.class);
            logger.log(Status.PASS, "headers " + headers.toString() + " is set successfully");
        } catch (Exception e1) {
            logger.log(Status.FAIL, "Request Header Template folder location  " + headerTemplatesFolderPath + " iis not set properly");
        }
    }

    public void changeRequestHeaderValue(String headerName, String headerValue) throws IOException {
        requestHeaderTemplate = requestHeaderTemplate.replace(headerName, headerValue);
        try {
            headers = new ObjectMapper().readValue(requestHeaderTemplate, HashMap.class);
        } catch (JsonProcessingException e) {
            throw new IOException("Request Header Template data is incorrect.");
        }
    }

    public void setRequestBody(String fileName)
            throws IOException {
        if (bodyTemplatesFolderPath == null) {
            throw new IOException("Request Body Template folder location is not set.\n" +
                    "Use 'Request Body Templates are placed in folder location <>' to setup the same");
        }
        requestBodyJson = getFileContents(bodyTemplatesFolderPath + fileName);
        requestBody = new JSONObject(requestBodyJson);
    }

    public void setJsonRequestBody(String parameterName, String parameterValue)
            throws IOException {
        try {
            if (requestBodyJsonObj == null) {
                requestBodyJsonObj = new JSONObject();
            }
            requestBodyJsonObj.put(parameterName, parameterValue);
            System.out.println(requestBodyJsonObj);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    public void setRequestBodyParam(String requestParam, String variable) throws JsonProcessingException {
        try {
            requestBodyJsonObj = requestBodyJsonObj.put(requestParam, variable);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

    }

    public void setRequestParameters(String parameterName, String parameterValue) throws JsonProcessingException {
        try {
            if (parameters == null) {
                parameters = new HashMap<String, String>();
            }
            parameters.put(parameterName, parameterValue);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void connectDB() {

        // Connect to MongoDB
        MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017");
        MongoDatabase database = mongoClient.getDatabase("hub");

        // Access a collection
        MongoCollection<Document> collection = database.getCollection("ingredients");

        // Example query
        Document query = new Document("productType","FOOD");

        for (Document doc : collection.find(query)) {
            System.out.println(doc.toJson().toString());
        }
//        // Convert query to JSON
//        String jsonQuery = query.toJson();

        mongoClient.close();

    }

    public void setRequestPathParameters(String parameterName, String parameterValue) throws JsonProcessingException {
        try {
            if (pathParameters == null) {
                pathParameters = new HashMap<>();
            }
            pathParameters.put(parameterName, parameterValue);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }


    public JSONObject changeRequestBodyValue(String requestParam, String variable) throws IOException, JSONException {

        requestBodyString = requestBodyString.replace(requestParam, variable);
        requestBodyJsonObj = new JSONObject(requestBodyString);
        return requestBodyJsonObj;
    }


    public <T> T getJson(Class className) throws IOException {
        JsonElement jsonElement;
        try (Reader reader = Files.newBufferedReader(Paths.get(System.getProperty("user.dit") +
                        bodyTemplatesFolderPath + className.getName().
                        substring(className.getName().lastIndexOf(".") + 1) + ".json"),
                StandardCharsets.UTF_8)) {
            jsonElement = JsonParser.parseReader(reader);
        }
        return (T) new JSON(false).getGson().fromJson(jsonElement, className);
    }


    private void logRequestInfo(String infoType, String info) {
        logger.log(Status.INFO, infoType + ": " + info);
    }


    public void sendRequest(String method, RequestSpecification request, String requestedUrl)  {
        resourceName = requestedUrl;
        switch (method) {
            case "GET":
                try {
                    validateResponse = request.when().get(requestedUrl).then().log().all();
                    logger.log(Status.PASS, "GET request with Url " + url + requestedUrl + " has been sent successfully");
                } catch (Exception e) {
                    logger.log(Status.FAIL, "GET request with Url " + url + requestedUrl + " has failed");
                }
                break;
            case "POST":
                try {
                    validateResponse = request.when().post(requestedUrl).then().log().all();
                    logger.log(Status.PASS, "POST request with Url " + url + requestedUrl + " has been sent successfully");
                } catch (Exception e) {
                    logger.log(Status.FAIL, "POST request with Url " + url + requestedUrl + " has failed");
                }
                break;
            case "PUT":
                try {
                    validateResponse = request.when().put(requestedUrl).then().log().all();
                    logger.log(Status.PASS, "PUT request with Url " + url + requestedUrl + " has been sent successfully");
                } catch (Exception e) {
                    logger.log(Status.FAIL, "Put request with Url " + url + requestedUrl + " has failed");
                }
                break;
            case "DELETE":
                try {
                    validateResponse = request.when().delete(requestedUrl).then().log().all();
                    logger.log(Status.PASS, "DELETE request with Url " + url + requestedUrl + " has been sent successfully");
                } catch (Exception e) {
                    logger.log(Status.FAIL, "DELETE request with Url " + url + requestedUrl + " has failed");
                }
                break;
            case "PATCH":
                try {
                    validateResponse = request.when().patch(requestedUrl).then().log().all();
                    logger.log(Status.PASS, "PATCH request with Url " + url + requestedUrl + " has been sent successfully");
                } catch (Exception e) {
                    logger.log(Status.FAIL, "PATCH request with Url " + url + requestedUrl + " has failed");
                }
                break;
            case "OPTIONS":
                try {
                    validateResponse = request.when().options(requestedUrl).then().log().all();
                    logger.log(Status.PASS, "OPTION request with Url " + url + requestedUrl + " has been sent successfully");
                } catch (Exception e) {
                    logger.log(Status.FAIL, "OPTION request with Url " + url + requestedUrl + " has failed");
                }
                break;
            default:
//                logger.log(Status.FAIL, "check the message  " + new InvalidRESTMethod(method));
//                throw new invalidRESTMethod(method);
        }

    }

    public void sendRequestWithPathParam(String method, RequestSpecification request, String requestedUrl, String variableName) {
        switch (method) {
            case "GET":
                try {
                    validateResponse = request.when().get(requestedUrl + variables.get(variableName)).then().log().all();
                    logger.log(Status.PASS, "GET request with Url " + url + requestedUrl + variables.get(variableName) + " has been sent successfully");
                } catch (Exception e) {
                    logger.log(Status.FAIL, "GET request with Url " + url + requestedUrl + variables.get(variableName) + " has failed");
                }
                break;
            case "POST":
                try {
                    validateResponse = request.when().post(requestedUrl + variables.get(variableName)).then().log().all();
                    logger.log(Status.PASS, "POST request with Url " + url + requestedUrl + variables.get(variableName) + " has been sent successfully");
                } catch (Exception e) {
                    logger.log(Status.FAIL, "POST request with Url " + url + requestedUrl + variables.get(variableName) + " has failed");
                }
                break;
            case "PUT":
                try {
                    validateResponse = request.when().put(requestedUrl + variables.get(variableName)).then().log().all();
                    logger.log(Status.PASS, "PUT request with Url " + url + requestedUrl + variables.get(variableName) + " has been sent successfully");
                } catch (Exception e) {
                    logger.log(Status.FAIL, "Put request with Url " + url + requestedUrl + variables.get(variableName) + " has failed");
                }
                break;
            case "DELETE":
                try {
                    validateResponse = request.when().delete(requestedUrl + variables.get(variableName)).then().log().all();
                    logger.log(Status.PASS, "DELETE request with Url " + url + requestedUrl + variables.get(variableName) + " has been sent successfully");
                } catch (Exception e) {
                    logger.log(Status.FAIL, "DELETE request with Url " + url + requestedUrl + variables.get(variableName) + " has failed");
                }
                break;
            case "PATCH":
                try {
                    validateResponse = request.when().patch(requestedUrl + variables.get(variableName)).then().log().all();
                    logger.log(Status.PASS, "PATCH request with Url " + url + requestedUrl + variables.get(variableName) + " has been sent successfully");
                } catch (Exception e) {
                    logger.log(Status.FAIL, "PATCH request with Url " + url + requestedUrl + variables.get(variableName) + " has failed");
                }
                break;
            case "OPTIONS":
                try {
                    validateResponse = request.when().options(requestedUrl + variables.get(variableName)).then().log().all();
                    logger.log(Status.PASS, "OPTION request with Url " + url + requestedUrl + variables.get(variableName) + " has been sent successfully");
                } catch (Exception e) {
                    logger.log(Status.FAIL, "OPTION request with Url " + url + requestedUrl + variables.get(variableName) + " has failed");
                }
                break;
            default:
//                logger.log(Status.FAIL, "check the message  " + new invalidRESTMethod(method));
//                throw new invalidRESTMethod(method);
        }

    }

    public void sendRequestWithPathParameter(String method, RequestSpecification request, String requestedUrl, String variableName, String value) {
        switch (method) {
            case "GET":
                try {
                    validateResponse = request.pathParam(variableName, value).when().get(requestedUrl + "/{" + variableName + "}").then().log().all();
                    logger.log(Status.PASS, "GET request with Url " + url + requestedUrl + " has been sent successfully");
                } catch (Exception e) {
                    logger.log(Status.FAIL, "GET request with Url " + url + requestedUrl + " has failed");
                }
                break;
            case "POST":
                try {
                    validateResponse = request.when().post(requestedUrl).then().log().all();
                    logger.log(Status.PASS, "POST request with Url " + url + requestedUrl + " has been sent successfully");
                } catch (Exception e) {
                    logger.log(Status.FAIL, "POST request with Url " + url + requestedUrl + " has failed");
                }
                break;
            case "PUT":
                try {
                    validateResponse = request.when().put(requestedUrl + variables.get(variableName)).then().log().all();
                    logger.log(Status.PASS, "PUT request with Url " + url + requestedUrl + " has been sent successfully");
                } catch (Exception e) {
                    logger.log(Status.FAIL, "Put request with Url " + url + requestedUrl + " has failed");
                }
                break;
            case "DELETE":
                try {
                    validateResponse = request.when().delete(requestedUrl).then().log().all();
                    logger.log(Status.PASS, "DELETE request with Url " + url + requestedUrl + " has been sent successfully");
                } catch (Exception e) {
                    logger.log(Status.FAIL, "DELETE request with Url " + url + requestedUrl + " has failed");
                }
                break;
            case "PATCH":
                try {
                    validateResponse = request.when().patch(requestedUrl).then().log().all();
                    logger.log(Status.PASS, "PATCH request with Url " + url + requestedUrl + " has been sent successfully");
                } catch (Exception e) {
                    logger.log(Status.FAIL, "PATCH request with Url " + url + requestedUrl + " has failed");
                }
                break;
            case "OPTIONS":
                try {
                    validateResponse = request.when().options(requestedUrl).then().log().all();
                    logger.log(Status.PASS, "OPTION request with Url " + url + requestedUrl + " has been sent successfully");
                } catch (Exception e) {
                    logger.log(Status.FAIL, "OPTION request with Url " + url + requestedUrl + " has failed");
                }
                break;
            default:
//                logger.log(Status.FAIL, "check the message  " + new invalidRESTMethod(method));
//                throw new invalidRESTMethod(method);
        }

    }

    public int getStatusCode() {
        try {
            logger.log(Status.PASS, "Status code received is " + validateResponse.extract().statusCode());
            return validateResponse.extract().statusCode();
        } catch (Exception e) {
            logger.log(Status.FAIL, "Status code received is " + validateResponse.extract().statusCode() + " " + e.getMessage());
            return 0;
        }
    }


    public String getFileContents(String fileFullPath) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(fileFullPath))) {

            String currentLine;
            while ((currentLine = br.readLine()) != null) {
                contentBuilder.append(currentLine).append("\n");
            }
        } catch (IOException e) {
            throw new IOException
                    ("Error reading contents from " + fileFullPath + "\n" + e.getMessage());
        }
        return contentBuilder.toString();
    }

    public JSONArray getJsonFileContents(String filename, String condition) throws IOException {
        JSONArray jsonArray = new JSONObject(new JSONTokener(new FileReader(filename))).getJSONArray(condition);
        return jsonArray;
    }


    public void saveVariableValue(String jsonPath, String variableName) {
        if (variables == null) {
            variables = new HashMap<String, String>();
        }
        if (variables.containsKey(variableName)) {
            variables.remove(variableName);
        }
        variables.put(variableName, validateResponse.extract().body().jsonPath().get(jsonPath));
    }

    public void saveVariableValueAsString(String variableName) {
        if (variables == null) {
            variables = new HashMap<String, String>();
        }
        if (variables.containsKey(variableName)) {
            variables.remove(variableName);
        }
        variables.put(variableName, validateResponse.extract().body().asString());
        System.out.println("poop" + variables.get(variableName));
        System.out.println(variables);
    }

    public void validateResponseBody(String name) throws IOException {
        if (name.matches(".*[\\s&].*")) {
            String condition = name.split("[\\s&]+")[0];
            System.out.println("condition" + condition);
            String fileName = System.getProperty("user.dir") + jsonResponses + name.split("[\\s&]+")[1];
            System.out.println("fileName" + fileName);
            Assert.assertEquals(commonFunctions.getJsonFileContents(fileName, condition), validateResponse.extract().body().jsonPath().getList("data"));
        } else {
            System.out.println("The filename does not contain the specified delimiter.");
            Assert.fail("The filename does not contain the specified delimiter.");
        }
    }
    public List<String> extractOptions(String body) {
        List<String> options = new ArrayList<>();

        String regex = "<option>(.*?)</option>";

        Pattern pattern = Pattern.compile(regex);

        Matcher matcher = pattern.matcher(body);

        while (matcher.find()) {

            String optionText = matcher.group(1).trim();

            if (!optionText.contains("Menu") && !optionText.contains("previous menu")) {
                options.add(optionText);
            }
        }
        System.out.println("that is options " + options);
        return options;
    }


    public void testSubMenus(String parentMenu, List<String> options, String variableName, String methodType) throws Exception {

        for (String option : options) {

            if (visitedMenus.containsKey(parentMenu) && visitedMenus.get(parentMenu).contains(option)) {
                continue;
            }
            visitedMenus.putIfAbsent(parentMenu, new HashSet<>());
            visitedMenus.get(parentMenu).add(option);
            System.out.println("Testing submenu: " + option + " under parent menu: " + (parentMenu.isEmpty() ? "Main Menu" : parentMenu));
            setRequestParameters("text",option);
            buildRequest();
            sendRequest(methodType, request, option);

//            Assert.assertEquals(commonFunctions.getStatusCode(), 200);

            saveVariableValueAsString(variableName);

            ApiHooks.tearDownTest(getCurrentScenario());
            if (variables.get(variableName).contains("error") || variables.get(variableName).contains("sorry"))
               return ;

            List<String> nextLevelOptions = extractOptions(variables.get(variableName));

            if (!nextLevelOptions.isEmpty()) {
                testSubMenus(option, nextLevelOptions, variableName, url);
            }

        }
    }



    public void processApiRequests(String url, String methodType, Scenario scenario) throws Exception {
        // Send initial request to get the options
        commonFunctions.buildRequest();
        sendRequest("GET", request, url );  // Send initial request

        // Extract the options from the response
        String responseBody = variables.get("response");  // Assuming response is saved in this variable
        List<String> options = extractOptions(responseBody);

        // Process each option by modifying the key parameter
        for (String option : options) {
            // Modify the key parameter for the next request
            String updatedKey =  url+ option;  // You can modify how you create the key depending on the API logic

            // Send request for the option
            System.out.println("Sending request for option: " + option);
            commonFunctions.buildRequest();
            sendRequest(methodType, request, url + updatedKey);  // Send request with the updated key parameter

            // Log request and response details
            commonFunctions.tearDownTest1(scenario);  // This logs both request and response details

            // Validate the response
            commonFunctions.saveVariableValueAsString("response");

            // Handle error or success in the response
            if (variables.get("response").contains("error") || variables.get("response").contains("sorry")) {
                throw new Exception("Invalid response for option: " + option);
            }

            // Recursively process the next level options (if any)
            List<String> nextLevelOptions = extractOptions1(variables.get("response"));
            if (!nextLevelOptions.isEmpty()) {
                processApiRequests(url, methodType, scenario);  // Recursive call for nested options
            }
        }
    }

    public List<String> extractOptions1(String body) {
        List<String> options = new ArrayList<>();
        String regex = "<option>(.*?)</option>";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(body);

        while (matcher.find()) {
            String optionText = matcher.group(1).trim();
            if (!optionText.contains("Menu") && !optionText.contains("previous menu")) {
                options.add(optionText);
            }
        }
        System.out.println("Extracted options: " + options);
        return options;
    }
    public void tearDownTest1(Scenario scenario) {
        String details = "Request & Response Details\n";

        if (headers != null) {
            details += "Request Headers: \n" + new JSONObject(headers).toString(1) + "\n";
        } else {
            details += "Request Headers: None\n";
        }

        if (parameters != null) {
            details += "Request Parameters: \n" + new JSONObject(parameters).toString(1) + "\n";
        } else {
            details += "Request Parameters: None\n";
        }

        if (pathParameters != null) {
            details += "Request Path Parameters: \n" + new JSONObject(pathParameters).toString(1) + "\n";
        } else {
            details += "Request Path Parameters: None\n";
        }

        if (requestBody != null) {
            details += "Request Body: \n" + requestBody.toString(1) + "\n";
        } else {
            details += "Request Body: None\n";
        }

        if (url != null) {
            details += "Base URL: " + url + "\n";
        }

        if (requestBodyJsonObj != null) {
            details += "Request Body (JSON): \n" + requestBodyJsonObj.toString(1) + "\n";
        }

        if (validateResponse != null) {
            details += "Response Status: " + validateResponse.extract().statusLine() + "\n";
            details += "Response Headers: \n" + validateResponse.extract().headers().toString() + "\n";
            details += "Response Body: \n" + validateResponse.extract().body().asPrettyString() + "\n";
        }

        scenario.attach(details, "text/plain", "API Request And Response Details");
    }


}
