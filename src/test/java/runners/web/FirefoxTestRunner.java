package runners.web;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(tags = "@firefox", features = {"src/test/java/features/web/Dashboard Layout and Functionality.feature"}, glue = "org.psh.steps.web" , plugin = {"rerun:target/rerun.txt"})

public class FirefoxTestRunner extends AbstractTestNGCucumberTests {
}