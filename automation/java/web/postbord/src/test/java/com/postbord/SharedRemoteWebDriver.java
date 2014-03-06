package com.postbord;

import cucumber.api.java.Before;
import org.openqa.selenium.Platform;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriverLogLevel;
import org.openqa.selenium.ie.InternetExplorerDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.service.DriverService;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;

import static org.openqa.selenium.firefox.FirefoxDriver.BINARY;
import static org.openqa.selenium.ie.InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS;

public class SharedRemoteWebDriver extends EventFiringWebDriver {
    private static RemoteWebDriver SHARED_DRIVER;
    private static DesiredCapabilities capabilities;
    private static Logger logger = LoggerFactory.getLogger(SharedRemoteWebDriver.class);
  
    private static final Thread CLOSE_THREAD = new Thread() {
        @Override
        public void run() {
          SHARED_DRIVER.close();
        }
    };

    static {
        Runtime.getRuntime().addShutdownHook(CLOSE_THREAD);
    }

    public SharedRemoteWebDriver() {
        super(SHARED_DRIVER);
    }
  
    @Override
    public void close() {
        if(Thread.currentThread() != CLOSE_THREAD)
          throw new UnsupportedOperationException("You shouldn't close this WebDriver. It's shared and will close when the JVM exits.");

          super.close();
    }
  
    @Before
    public void deleteAllCookies() {
        manage().deleteAllCookies();
    }
  
    private RemoteWebDriver setSharedDriverToChrome() throws MalformedURLException, RuntimeException {
        String chromeDriverExecutable = getDriverExecutable("chrome");

        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.setBinary("");

        DriverService driverService = new ChromeDriverService.Builder()
            .usingDriverExecutable(new File(chromeDriverExecutable))
            .usingAnyFreePort()
            .withLogFile(new File("chrome.log"))
            .withVerbose(true)
            .build();

        try {
            driverService.start();
        } catch(IOException e) {
            logger.error("", e.getMessage());
            e.printStackTrace();
        }

        capabilities = DesiredCapabilities.chrome();
        setDesiredCapabilities();

        return new RemoteWebDriver(driverService.getUrl(), capabilities);
    }
    private RemoteWebDriver setSharedDriverToInternetExplorer() throws MalformedURLException, RuntimeException {
        String ieDriverExecutable = getDriverExecutable("iexplorer");

        DriverService driverService = new InternetExplorerDriverService.Builder()
                .usingDriverExecutable(new File(ieDriverExecutable))
                .usingAnyFreePort()
                .withLogFile(new File("iexplorer.log"))
                .withLogLevel(InternetExplorerDriverLogLevel.ERROR)
                .build();

        try {
            driverService.start();
        } catch (IOException e) {
            logger.error("", e.getMessage());
            e.printStackTrace();
        }

        capabilities = DesiredCapabilities.internetExplorer();
        capabilities.setCapability(INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
        setDesiredCapabilities();

        return new RemoteWebDriver(driverService.getUrl(), capabilities);
    }
    
    private RemoteWebDriver setSharedDriverToPhantomJS() throws MalformedURLException, RuntimeException {
        String phantomjsDriverExecutable = getDriverExecutable("phantomjs");
        
        DriverService driverService = new ChromeDriverService.Builder()
            .usingPhantomJSExecutable(new File(chromeDriverExecutable))
            .usingAnyFreePort()
            .withLogFile(new File("phantomjs.log"))
            .usingCommandLineArguments(String[] "")
            .build();
            
        try {
            driverService.start();
        } catch (IOException e) {
            logger.error("", e.getMessage());
            e.printStackTrace();
        }

        capabilities = DesiredCapabilities.internetExplorer();
        setDesiredCapabilities();

        return new RemoteWebDriver(driverService.getUrl(), capabilities);
    }

    private RemoteWebDriver setSharedDriverToFirefox() throws MalformedURLException, RuntimeException {
        capabilities = DesiredCapabilities.firefox();
        capabilities.setCapability(BINARY, "webdriver.firefox.bin");
        capabilities.setCapability("webdriver.log.file", new File("firefox.log"));
        setDesiredCapabilities();

        return new RemoteWebDriver(capabilities);
    }

    private String getDriverExecutable(String driver) {
        String driverExecutable = null;

        if (driver.equalsIgnoreCase("chrome")) {
            driverExecutable = setChromeDriverExecutable();
        } else if (driver.equalsIgnoreCase("ie")) {

        } else {

        }

        return driverExecutable;
    }

    private String getCurrentPlatform() {
        String platform = "WIN";

        if (Platform.getCurrent().is(Platform.MAC))
            platform = "MAC";
        else if (Platform.getCurrent().is(Platform.LINUX) || Platform.getCurrent().is(Platform.UNIX))
            platform = "LINUX";

        return platform;
    }

    private String setChromeDriverExecutable() {
        String chromeDriverExecutable = "";

        if (getCurrentPlatform().equalsIgnoreCase("MAC")) {
            chromeDriverExecutable = "";
        } else if (getCurrentPlatform().equalsIgnoreCase("LINUX"))
            chromeDriverExecutable = "";

        return chromeDriverExecutable;
    }
    
    private static void setDesiredCapabilities() {
        capabilities.setJavascriptEnabled(true);
        capabilities.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
        capabilities.setCapability(CapabilityType.TAKES_SCREENSHOT, true);
    }
}
