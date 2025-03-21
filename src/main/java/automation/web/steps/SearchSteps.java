package automation.web.steps;

import automation.web.common.BrowserFactory;
import automation.web.common.CommonFunctions;
import automation.web.setup.BaseTest;
import automation.web.webpages.LoginPage;
import automation.web.webpages.SearchPage;
import automationFramwork.api.steps.ApiSteps;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.io.IOException;

public class SearchSteps extends BaseTest {
     SearchPage searchPage = new SearchPage();
     ApiSteps apiSteps = new ApiSteps();

    public SearchSteps() throws IOException {
    }

    @And("Search for the user by their email in the UI")
    public void searchUserInUI() {
        CommonFunctions comm = new CommonFunctions();
        comm.sendText(searchPage.SEARCH_FIELD,apiSteps.userNameFromAPI,"Type Username in search field");
        comm.clickOnElement(searchPage.SUBMIT_BUTTON,"Click On Submit Button");
        comm.isElementDisplayed(searchPage.SEARCH_RESULTS,"Check If Element is Displayed");
        String userNameFromUI = comm.getText(searchPage.USER_NAME,"get user name ");
        String userEmailFromUI = comm.getText(searchPage.USER_EMAIL,"get email ");
        comm.verifyTextEquality(userNameFromUI,apiSteps.userNameFromAPI);
        comm.verifyTextEquality(userEmailFromUI,apiSteps.userEmailFromAPI);
    }

}


