package runners.api;
import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;


@CucumberOptions(
        features = "src/test/resources/features/api/API.feature",
        glue = {"automationFramwork.api.steps"},
        plugin = {
                "pretty",
                "html:target/cucumber_report_API/api_report.html",  // Static report file for simplicity
                "json:target/cucumber_report_API/api_report.json"  // Static report file for simplicity
        }
)
public class API extends AbstractTestNGCucumberTests {

}

