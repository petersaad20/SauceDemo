package runners.web;

import io.cucumber.testng.CucumberOptions;
import io.cucumber.testng.AbstractTestNGCucumberTests;

@CucumberOptions(
        features = {"src/test/resources/features/web/Visual & Performance Testing.feature"},
        glue = "automation.web.steps",
        plugin = {
                "pretty",
                "html:target/cucumber-reports-PerformanceTestingRunner/cucumber.html",
                "json:target/cucumber-reports-PerformanceTestingRunner/cucumber.json",
                "rerun:target/rerun.txt"
        }
)
public class PerformanceTestingRunner extends AbstractTestNGCucumberTests {

}
