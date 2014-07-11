package com.postbord;

import cucumber.api.java.Before;
import org.openqa.selenium.Platform;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.ie.InternetExplorerDriverLogLevel;
import org.openqa.selenium.ie.InternetExplorerDriverService;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
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
import static org.openqa.selenium.remote.CapabilityType.ACCEPT_SSL_CERTS;
import static org.openqa.selenium.remote.CapabilityType.TAKES_SCREENSHOT;

public class SharedDriver extends EventFiringWebDriver {
    private static RemoteWebDriver SHARED_DRIVER;
    private static DesiredCapabilities capabilities;
    private static Logger logger = LoggerFactory.getLogger(SharedDriver.class);
  
    private static final Thread CLOSE_THREAD = new Thread() {
        @Override
        public void run() {
          SHARED_DRIVER.close();
        }
    };
    
    static {
        String browser = System.getProperty("test.browser.target");
        
        if (browser.equalsIgnoreCase("chrome")) {
            try {
                SHARED_DRIVER = setSharedDriverToChrome();
            } catch (MalformedURLException e) {
                logger.error("Executable path to " + browser + " cannot be located, ensure path is configured in SYS VARIABLE.", e.getMessage());
                e.printStackTrace();
            }
        } else if (browser.equalsIgnoreCase("firefox")) {
            try {
                SHARED_DRIVER = setSharedDriverToFirefox();
            } catch (MalformedURLException e) {
                logger.error("Executable path to " + browser + " cannot be located, ensure path is configured in SYS VARIABLE.", e.getMessage());
                e.printStackTrace();
            }
        } else if (browser.equalsIgnoreCase("safari")) {
            try {
                SHARED_DRIVER = setSharedDriverToSafari();
            } catch (MalformedURLException e) {
                logger.error("Executable path to " + browser + " cannot be located, ensure path is configured in SYS VARIABLE.", e.getMessage());
                e.printStackTrace();
            }
        } else if (browser.equalsIgnoreCase("ie")) {
            try {
                if (!getCurrentPlatform().equalsIgnoreCase("WIN"))
                    throw new IllegalStateException(browser + " is not supported on " + getCurrentPlatform());
                
                SHARED_DRIVER = setSharedDriverToInternetExplorer();
            } catch (MalformedURLException e) {
                logger.error("Executable path to " + browser + " cannot be located, ensure path is configured in SYS VARIABLE.", e.getMessage());
                e.printStackTrace();
            }
        }  else {
            try {
                SHARED_DRIVER = setSharedDriverToPhantomJS();
            } catch (MalformedURLException e) {
                logger.error("Executable path to " + browser + " cannot be located, ensure path is configured in SYS VARIABLE.", e.getMessage());
                e.printStackTrace();
            }
        }
        
        Runtime.getRuntime().addShutdownHook(CLOSE_THREAD);
    }

    private static RemoteWebDriver setSharedDriverToSafari() throws MalformedURLException, RuntimeException {
        capabilities = DesiredCapabilities.safari();
        capabilities.setCapability("", "");
        setDesiredCapabilities();
        
        return new RemoteWebDriver(capabilities);
    }

    public SharedDriver() {
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
  
    private static RemoteWebDriver setSharedDriverToChrome() throws MalformedURLException, RuntimeException {
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
    private static RemoteWebDriver setSharedDriverToInternetExplorer() throws MalformedURLException, RuntimeException {
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
    
    private static RemoteWebDriver setSharedDriverToPhantomJS() throws MalformedURLException, RuntimeException {
        String phantomjsDriverExecutable = getDriverExecutable("phantomjs");
        String[] args = {""};
        
        DriverService driverService = new PhantomJSDriverService.Builder()
            .usingPhantomJSExecutable(new File(phantomjsDriverExecutable))
            .usingAnyFreePort()
            .withLogFile(new File("phantomjs.log"))
            .usingCommandLineArguments(args)
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

    private static RemoteWebDriver setSharedDriverToFirefox() throws MalformedURLException, RuntimeException {
        capabilities = DesiredCapabilities.firefox();
        capabilities.setCapability(BINARY, "webdriver.firefox.bin");
        capabilities.setCapability("webdriver.log.file", new File("firefox.log"));
        setDesiredCapabilities();

        return new RemoteWebDriver(capabilities);
    }

    private static String getDriverExecutable(String driver) {
        String driverExecutable;

        if (driver.equalsIgnoreCase("chrome")) {
            driverExecutable = setChromeDriverExecutable();
        } else if (driver.equalsIgnoreCase("ie")) {
            driverExecutable = setIEDriverExecutable();
        } else {
            driverExecutable = setPhantomJSDriverExecutable();
        }

        return driverExecutable;
    }

    private static String setIEDriverExecutable() {
        String ieDriverExecutable = "";

        if (getCurrentPlatform().equalsIgnoreCase("MAC"))
            ieDriverExecutable = "";
        else if (getCurrentPlatform().equalsIgnoreCase("LINUX"))
            ieDriverExecutable = "";

        return ieDriverExecutable;
    }

    private static String setPhantomJSDriverExecutable() {
        String phantomjsDriverExecutable = "";

        if (getCurrentPlatform().equalsIgnoreCase("MAC"))
            phantomjsDriverExecutable = "";
        else if (getCurrentPlatform().equalsIgnoreCase("LINUX"))
            phantomjsDriverExecutable = "";

        return phantomjsDriverExecutable;
    }

    private static String getCurrentPlatform() {
        String platform = "WIN";

        if (Platform.getCurrent().is(Platform.MAC))
            platform = "MAC";
        else if (Platform.getCurrent().is(Platform.LINUX) || Platform.getCurrent().is(Platform.UNIX))
            platform = "LINUX";

        return platform;
    }

    private static String setChromeDriverExecutable() {
        String chromeDriverExecutable = "";

        if (getCurrentPlatform().equalsIgnoreCase("MAC")) {
            chromeDriverExecutable = "";
        } else if (getCurrentPlatform().equalsIgnoreCase("LINUX"))
            chromeDriverExecutable = "";

        return chromeDriverExecutable;
    }
    
    private static void setDesiredCapabilities() {
        capabilities.setJavascriptEnabled(true);
        capabilities.setCapability(ACCEPT_SSL_CERTS, true);
        capabilities.setCapability(TAKES_SCREENSHOT, true);
    }
}
