package net.skyscanner;

import org.openqa.selenium.Platform;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.events.EventFiringWebDriver;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import static org.openqa.selenium.remote.CapabilityType.*;

@SuppressWarnings("UnusedDeclaration")
public class SharedRemoteWebDriver extends EventFiringWebDriver {
    public static RemoteWebDriver SHARED_REMOTE_WEBDRIVER;
    private static DesiredCapabilities capabilities;

    private Platform currentPlatform = Platform.getCurrent();

    private String userPath = System.getProperty("user.home");
    private File rootPath = new File(userPath);

    private String device = System.getProperty("device");
    private String browser = System.getProperty("browser");
    private String version = System.getProperty("version");

    public SharedRemoteWebDriver() {
        super(SHARED_REMOTE_WEBDRIVER);
        manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
    }

    private static final Thread shutDownThread = new Thread() {
        @Override
        public void run() {
            SHARED_REMOTE_WEBDRIVER.close();
        }
    };

    private String getCurrentPlatform() {
        String platform;

        if (Platform.MAC.is(currentPlatform))
            platform = "MAC";
        else if (Platform.LINUX.is(currentPlatform) || Platform.UNIX.is(currentPlatform))
            platform = "LINUX";
        else
            platform = "WIN";

        return platform;
    }

    private void setAppiumCapabilities() {
        capabilities.setCapability("device", device);
        capabilities.setCapability(BROWSER_NAME, browser);
        capabilities.setCapability(VERSION, version);
        capabilities.setCapability(PLATFORM, getCurrentPlatform());
    }

    private void setCommandTimeout(String timeout) {
        capabilities.setCapability("newCommandTimeout", timeout);
    }

    private void setSharedRemoteWebDriverToAndroid() throws MalformedURLException {
        File appPath = new File(rootPath, "");
        File app = new File(appPath, "");

        capabilities = DesiredCapabilities.android();
        setAppiumCapabilities();

        capabilities.setCapability("app-package", "");
        capabilities.setCapability("app-activity", "");
        capabilities.setCapability("app", app.getAbsolutePath());

        if (getCurrentPlatform().equalsIgnoreCase("win"))  {
            capabilities.setCapability("no-reset", true);
            capabilities.setCapability("full-reset", false);
        }

        setCommandTimeout("60");

        String url = "http://127.0.0.1:4723/wd/hub";
        SHARED_REMOTE_WEBDRIVER = new SwipeableWebDriver(new URL(url), capabilities);
    }
}
