package com.postbord;


@RunWith(Cucumber.class)
@CucumberOptions(format = {"html:target/reports/cucumber/html", "json:target/reports/cucumber/result.json"})
public class RunTest {
}
