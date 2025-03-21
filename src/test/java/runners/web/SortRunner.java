package runners.web;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(
        features = {"src/test/resources/features/web/Sort.feature"},
        glue = "automation.web.steps" ,
        plugin = {
                "pretty",
                "html:target/cucumber-reports-SortRunner/cucumber.html",
                "json:target/cucumber-reports-SortRunner/cucumber.json"
        }
)

public class SortRunner extends AbstractTestNGCucumberTests {
}