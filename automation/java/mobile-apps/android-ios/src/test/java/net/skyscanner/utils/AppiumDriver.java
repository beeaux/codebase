package net.skyscanner.utils;


import cucumber.api.java.Before;

import static net.skyscanner.SharedRemoteWebDriver.getCurrentPlatform;
import static net.skyscanner.utils.CommandLineExecutor.executeCommand;

public class AppiumDriver {

    @Before
    private void launchAppiumServer() {
        String appiumCommand = "appium --log appium.log --log-timestamp ";

        if (getCurrentPlatform().equalsIgnoreCase("mac")) {
            appiumCommand = "/opt/X11/bin/xterm -e  /usr/local/bin/appium --log-timestamp ";
        }

        try {
            executeCommand(appiumCommand);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
    
}
