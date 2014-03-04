package net.skyscanner;

import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.Augmenter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static net.skyscanner.SharedRemoteWebDriver.SHARED_REMOTE_WEBDRIVER;

public class ScenarioExtensions {
    public static Scenario scenario;
    private Logger logger = LoggerFactory.getLogger(ScenarioExtensions.class);

    @Before
    public void before(Scenario scenario) {
        ScenarioExtensions.scenario = scenario;
    }

    @SuppressWarnings("UnusedDeclaration")
    public void scenarioWriter(String text) {
        scenario.write(text);
    }

    @After
    public void embedScreenshot(Scenario scenario) {
        WebDriver driver = new Augmenter().augment(SHARED_REMOTE_WEBDRIVER);
        try {
            byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
            scenario.embed(screenshot, "image/png");
        } catch (RuntimeException e) {
            logger.error("Screenshot not supported warning: " + e.getMessage());
            throw e;
        }
    }
}
