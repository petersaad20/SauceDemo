package automationFramwork.api.steps;

import automationFramwork.api.basesclasses.CommonFunctions;
import automationFramwork.api.exceptions.InvalidBDDStepException;
import automationFramwork.api.exceptions.TestDataHandlingException;
import automationFramwork.api.setup.ApiSetup;
import com.fasterxml.jackson.core.JsonProcessingException;
import exceptions.InvalidRESTMethod;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.json.JSONObject;
import org.testng.Assert;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Random;

import static org.junit.Assert.assertTrue;

public class RestApiSteps extends ApiSetup {

    private static String templatesFolderPath;

    public RestApiSteps() throws IOException {
        super();
        commonFunctions = new CommonFunctions();
    }


    @When("build the request")
    public void build_the_request() throws Exception {
        commonFunctions.buildRequest();
    }

    @When("set param key {string} to param value {string}")
    public void set_body_param(String key, String value) throws Exception {
        commonFunctions.setRequestParameters(key, value);
    }

    @When("set json body with param key {string} to param value {string}")
    public void set_Json_Request_Body(String key, String value) throws Exception {
        commonFunctions.setJsonRequestBody(key, value);
    }

    @When("set param name {string} to param value {string}")
    public void set_body_path_param(String pathName, String pathValue) throws Exception {
        commonFunctions.setRequestPathParameters(pathName, pathValue);
    }

    @And("send {string} request {string}")
    public void send_request(String methodType, String requestedUrl) throws InvalidRESTMethod {
        commonFunctions.sendRequest(methodType, request, requestedUrl);
    }

    @And("send {string} request with endpoint {string} and variable {string}")
    public void send_request_with_path_parameter(String methodType, String requestedUrl, String variableName) throws InvalidRESTMethod {
        commonFunctions.sendRequestWithPathParam(methodType, request, requestedUrl, variableName);
    }

    @When("send {string} request with endpoint {string} and variable {string} and value {string}")
    public void sendRequestWithEndpointAndVariableAndValue(String methodType, String requestedUrl, String variableName, String Value) throws InvalidRESTMethod {
        commonFunctions.sendRequestWithPathParameter(methodType, request, requestedUrl, variableName, Value);
    }


    @Then("the status code should be {int}")
    public void the_status_code_should_be(int expectedStatusCode) {
        Assert.assertEquals(commonFunctions.getStatusCode(), expectedStatusCode);
    }


    @And("From Response Body , save value in this response path {string} to variable {string}")
    public void fromResponseBodySaveValueInThisResponsePathToVariable(String jsonPath, String variableName) {
        commonFunctions.saveVariableValue(jsonPath, variableName);
    }

    @And("From Response Body , save response body in this variable {string}")
    public void saveResponseAsJsonBody(String variableName) {
        commonFunctions.saveVariableValueAsString(variableName);
    }

    @And("get save value in variable {string}")
    public void getMapValue(String variableName) {
        variables.get(variableName);
    }

    @And("Assert {string} with {string}")
    public void assertWith(String expectedValue, String actualValue) {
        Assert.assertEquals(variables.get(expectedValue), variables.get(actualValue));
    }

    @And("check that response contain {string}")
    public void checkThatResponseContain(String value) {
        try {
            if (!variables.isEmpty()) {
                assertTrue("Response body should contain the expected string: " + variables.get(value),
//                        validateResponse.extract().body().jsonPath().get().toString().contains(variables.get(value)));
                        validateResponse.extract().body().asString().contains(variables.get(value)));
                System.out.println(variables.get(value));
            }
        } catch (Exception e) {
            assertTrue("Response body should contain the expected string: " + value,
                    validateResponse.extract().body().jsonPath().get().toString().contains(value));
        }
    }


    @And("check that country status is changed to disabled {string}")
    public void assertThatCountryStatusIsChangedToDisabled(String status) {
        Assert.assertEquals(validateResponse.extract().body().jsonPath().getString("data.status")
                , status);
    }

    @And("verify that actual response is equal to expected response")
    public void verifyThatActualResponseIsEqualToExpectedResponse() {

    }


    @And("validate response body equal to expected json {string}")
    public void validateResponseBody(String fileName) throws IOException {
        commonFunctions.validateResponseBody(fileName);
    }

    @And("get option values from response {string}")
    public void extractOptions(String value) throws IOException {
        commonFunctions.extractOptions(variables.get(value));
    }

//    @And("test sub menu from response {string} with url {string} , method type {string} and status code {int} scenario ")
//    public void testSubMenus(String value,String url,String methodType,int status,Scenario scenario) throws Exception {
//        commonFunctions.testSubMenus("",commonFunctions.extractOptions(variables.get(value)),variables.get(value),methodType, scenario);
//    }
@When("test sub menu from response {string} with url {string} , method type {string} and status code {int}")
public void test_sub_menu_from_response_with_url_method_type_and_status_code_scenario_scenario(String value, String url, String methodType, Integer status) throws Exception {
    // Write code here that turns the phrase above into concrete actions
     commonFunctions.testSubMenus("",commonFunctions.extractOptions(variables.get(value)),variables.get(value),methodType);
}




    @And("verify that response contain {string}")
    public void verifyThatResponseContain(String message) {
        if (!Objects.equals(message, "")) {
            Assert.assertEquals(validateResponse.extract().body().jsonPath().getString("message"), message);
        }
    }

    @And("verify that response key {string} contain this value {string}")
    public void verifyThatResponseContain(String key,String value) {
        if (!Objects.equals(key, "")||(!Objects.equals(value, ""))) {
            Assert.assertEquals(validateResponse.extract().body().jsonPath().getString(key), value);
        }
    }

    @And("Assert that all response has {string} equal to {string}")
    public void assertThatAllResponseHasEqualTo(String paramKey, String paramValue) {
        if (!validateResponse.extract().body().jsonPath().getList("data." + paramKey + "").isEmpty()) {
            for (Object status : validateResponse.extract().body().jsonPath().getList("data." + paramKey + "")) {
                Assert.assertEquals(status, paramValue);
                System.out.println(status);
            }
        } else {
            Assert.fail("Response Body is Empty or doesn't have your parameter");
        }

    }
    @Given("Request Body Templates are placed in folder location {string}")
    public void request_body_templates_are_placed_in_folder_location(String templatesFolderPath) {
        RestApiSteps.templatesFolderPath = templatesFolderPath;
    }

    @And("Load Request Body Template from file {string}")
    public void load_request_body_template_from_file(String templateFileName)
            throws TestDataHandlingException, InvalidBDDStepException, IOException {
        if (templatesFolderPath == null) {
            throw new InvalidBDDStepException("Request Body Template folder location is not set.\n" +
                    "Use 'Request Body Templates are placed in folder location <>' to setup the same");
        }
        String jsonTemplateFileFullPath =
                templatesFolderPath + templateFileName;
        requestBodyJson = getFileContents(jsonTemplateFileFullPath);
        System.out.println(requestBodyJson);
        requestBody=new JSONObject(requestBodyJson);
        System.out.println(requestBodyJson);

    }
    public static String getFileContents(String fileFullPath) throws TestDataHandlingException {
        StringBuilder contentBuilder = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(fileFullPath)))
        {

            String currentLine;
            while ((currentLine = br.readLine()) != null)
            {
                contentBuilder.append(currentLine).append("\n");
            }
        }
        catch (IOException e)
        {
            throw new TestDataHandlingException
                    ("Error reading contents from " + fileFullPath+ "\n" + e.getMessage());
        }
        return contentBuilder.toString();
    }
    @And("In Request Body, {string} is set to value of variable {string}")
    public void set_request_body_from_variable(String requestparam, String variable) throws JsonProcessingException {
        String requestbodyValue = variables.get(variable).toString();
        System.out.println("*********" + requestbodyValue);
        initializeRequestBody();
        requestBody=requestBody.put(requestparam, requestbodyValue);
//        if(requestBodyJson!=null) {
        // FinalRequestBody = requestBody.toString() + requestBodyJson;

//             mapper = new ObjectMapper();
//            Map<String, Object> map1 = mapper.readValue(requestBody.toString(), Map.class);
//            Map<String, Object> map2 = mapper.readValue(requestBodyJson, Map.class);
//            merged= new HashMap<String, Object>(map2);
//            merged.putAll(map1);
//             jsonConc=mapper.writeValueAsString(merged);
        System.out.println(requestBody);
//        }
//        else{
//            mapper = new ObjectMapper();
//            merged = mapper.readValue(requestBody.toString(), Map.class);
//             jsonConc=mapper.writeValueAsString(merged);
//            System.out.println(jsonConc);
//
//        }

    }

    private static void initializeRequestBody() {
        if (requestBody == null) {
            requestBody = new JSONObject();
        }
    }


}

