package runners.web;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(
        features = {"src/test/resources/features/web/Login.feature"},
        glue = "automation.web.steps" ,
        plugin = {
                "pretty",
                "html:target/cucumber-reports-LoginRunner/cucumber.html",
                "json:target/cucumber-reports-LoginRunner/cucumber.json"
        }
)

public class LoginRunner extends AbstractTestNGCucumberTests {
}