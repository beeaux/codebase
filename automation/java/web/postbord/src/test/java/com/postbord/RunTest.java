package com.postbord;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(format = {"html:target/reports/cucumber/html", "json:target/reports/cucumber/result.json"})
public class RunTest {
}
