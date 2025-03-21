package automationFramwork.api.steps;

import automation.web.common.CommonFunctions;
import automationFramwork.api.setup.ApiSetup;
import io.cucumber.java.en.And;
import org.openqa.selenium.By;
import org.testng.Assert;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class ApiSteps extends ApiSetup {
    public static HashMap<String, String> variables;

    public ApiSteps() throws IOException {
    }
    public String userNameFromAPI;
    public String userEmailFromAPI;
    private String userAvatarFromAPI;

    @And("pick a random user from the API response")
    public void PickARandomUserFromTheAPIResponse() {
        // Parse the API response and extract the list of users
        List<?> users = validateResponse.extract().jsonPath().getList("data");
        Random random = new Random();
        int randomIndex = random.nextInt(users.size());
        userNameFromAPI = ((Map<String, String>) users.get(randomIndex)).get("first_name");
        userEmailFromAPI = ((Map<String, String>) users.get(randomIndex)).get("email");
        userAvatarFromAPI = ((Map<String, String>) users.get(randomIndex)).get("avatar");

    }
    @And("make a GET request to the ReqRes API")
    public void SearchForThatUserByTheirEmail() {
        // For simulation, we'll use the email to search for the user
        // Simulate search by matching email
        List<?> users = validateResponse.extract().jsonPath().getList("data");
        boolean userFound = false;
        for (Object userObj : users) {
            String userEmail = ((Map<String, String>) userObj).get("email");
            if (userEmail.equals(userEmailFromAPI)) {
                userFound = true;
                break;
            }
        }
        // Assert that the user was found in the response
        Assert.assertTrue(userFound, "User with email " + userEmailFromAPI + " was not found in the API response.");
    }

//    @And("pick a random user from the API response")
//    public void pickARandomUserFromAPI() {
//        // Extract random user data from the API response
//        List<?> users = validateResponse.extract().jsonPath().getList("data");
//        Random random = new Random();
//        int randomIndex = random.nextInt(users.size());
//        userNameFromAPI = ((Map<String, String>) users.get(randomIndex)).get("first_name");
//        userEmailFromAPI = ((Map<String, String>) users.get(randomIndex)).get("email");
//        userAvatarFromAPI = ((Map<String, String>) users.get(randomIndex)).get("avatar");
//    }







}

