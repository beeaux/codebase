package net.skyscanner.utils;


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
