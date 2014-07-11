package com.postbord;

import cucumber.api.Scenario;
import cucumber.api.java.Before;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.remote.Augmenter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ScenarioExtension {
    private Scenario scenario;
    private SharedDriver driver;
    private Logger logger = LoggerFactory.getLogger(ScenarioExtension.class);

    @Before
    private void before(Scenario scenario, SharedDriver driver) {
        this.scenario = scenario;
        this.driver = driver;
    }

    public void printOutScenarioInfo(String info) {
        scenario.write(info);
    }

    public void takeAndEmbedScreenshot(Scenario scenario) {
        WebDriver wd = new Augmenter().augment(driver);

        try {
            byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
            scenario.embed(screenshot, "image/png");
        } catch (WebDriverException e) {
            logger.error("", e.getMessage());
            throw e;
        }
    }
}
