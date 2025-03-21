package runners.web;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(
        features = {"src/test/resources/features/web/CheckOut.feature"},
        glue = "automation.web.steps" ,
        plugin = {
                "pretty",
                "html:target/cucumber-reports-CheckOutRunner/cucumber.html",
                "json:target/cucumber-reports-CheckOutRunner/cucumber.json"
        }
)

public class CheckOutRunner extends AbstractTestNGCucumberTests {
}