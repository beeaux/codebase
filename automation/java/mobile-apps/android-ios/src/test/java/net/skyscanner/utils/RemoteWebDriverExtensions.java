package net.skyscanner.utils;

import net.skyscanner.SharedRemoteWebDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.Wait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;

public class RemoteWebDriverExtensions {
    private static RemoteWebDriver sharedRemoteWebDriver;
    //private static Actions actions = new Actions();
    private static Wait<WebDriver> wait;
    private static Logger logger = LoggerFactory.getLogger(RemoteWebDriverExtensions.class);

    static {
       wait = new FluentWait<WebDriver>(sharedRemoteWebDriver())
               .withTimeout(15, TimeUnit.SECONDS)
               .pollingEvery(1, TimeUnit.SECONDS)
               .ignoring(NoSuchElementException.class);
    }

    public static WebDriver sharedRemoteWebDriver() {
        return sharedRemoteWebDriver;
    }

    /*

     */
    public WebElement findElementById(String id) throws RuntimeException {
        try {
            WebElement element = sharedRemoteWebDriver().findElement(By.id(id));
            if(!isElementDisplayed(element)) return null;

            return element;
        } catch (NoSuchElementException e) {
            logger.error("", e.getMessage());
            throw new NoSuchElementException(e.getMessage());
        }
    }
    public WebElement findElementByName(String name) throws RuntimeException {
        try {
            WebElement element = sharedRemoteWebDriver().findElement(By.name(name));
            if(!isElementDisplayed(element)) return null;

            return element;
        } catch (NoSuchElementException e) {
            logger.error("", e.getMessage());
            throw new NoSuchElementException(e.getMessage());
        }
    }
    public WebElement findElementByTagName(String tagName) throws RuntimeException {
        try {
            WebElement element = sharedRemoteWebDriver().findElement(By.tagName(tagName));
            if(!isElementDisplayed(element)) return null;

            return element;
        } catch (NoSuchElementException e) {
            logger.error("", e.getMessage());
            throw new NoSuchElementException(e.getMessage());
        }
    }
    public WebElement findElementByPartialLinkText(String partialLinkText) throws RuntimeException {
        try {
            WebElement element = sharedRemoteWebDriver().findElement(By.partialLinkText(partialLinkText));
            if(!isElementDisplayed(element)) return null;

            return element;
        } catch (NoSuchElementException e) {
            logger.error("", e.getMessage());
            throw new NoSuchElementException(e.getMessage());
        }
    }
    public List<WebElement> findElementsByTagName(String tagName) throws RuntimeException {
        try {
            return sharedRemoteWebDriver().findElements(By.tagName(tagName));
        } catch (NoSuchElementException e) {
            logger.error("", e.getMessage());
            throw new NoSuchElementException(e.getMessage());
        }
    }

    /*

     */
    public void clickOn(WebElement element) throws RuntimeException {
        element.click();
    }
    public void typeText(WebElement element, String text) throws RuntimeException {
        element.sendKeys(text);
    }
    public void select(WebElement element, String visibleText) {
        Select select = new Select(element);
        select.selectByVisibleText(visibleText);
    }

    /*

     */
    private static boolean isElementDisplayed(WebElement element) {
        boolean isDisplayed = true;

        if (!element.isDisplayed())
            isDisplayed = false;

        return isDisplayed;
    }

    /*
        fluent conditional waits
     */

}
