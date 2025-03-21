package automation.web.steps;

import automation.web.common.BrowserFactory;
import automation.web.common.CommonFunctions;
import automation.web.setup.BaseTest;
import automation.web.webpages.LoginPage;
import backendservices.ServiceDelegate;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.testng.Assert;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SharedSteps extends BaseTest {
    public static HashMap<String, String> variables;


    @Given("connect with db")
    public void connect() {
        ServiceDelegate service = new ServiceDelegate();
        service.changeStatusOfClient();
    }





}

