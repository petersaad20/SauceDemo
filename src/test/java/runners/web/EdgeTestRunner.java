package runners.web;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(tags = "@edge", features = {"" }, glue = "steps" , plugin = {"rerun:target/rerun.txt"})
public class EdgeTestRunner extends AbstractTestNGCucumberTests {

}
