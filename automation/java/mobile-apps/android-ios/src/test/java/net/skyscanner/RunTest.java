package net.skyscanner;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(format = {"html:target/reports/cucumber/html", "json:target/reports/cucumber/json/result.json","pretty"})
public class RunTest {
}
