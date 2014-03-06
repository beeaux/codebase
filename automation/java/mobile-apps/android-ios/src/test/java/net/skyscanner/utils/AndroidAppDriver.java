package net.skyscanner.utils;

import cucumber.api.java.After;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static net.skyscanner.SharedRemoteWebDriver.getCurrentPlatform;
import static net.skyscanner.utils.CommandLineExecutor.executeCommand;

public class AndroidAppDriver {
    private static Logger logger = LoggerFactory.getLogger(AndroidAppDriver.class);
    private static String appPackage;
    private static String appActivity;
    
    /*public AndroidApp(String appPackage) {
      this.appPackage = appPackage;
    }*/
    
    public void clearAndroidCache(String appPackage) {
        String command = "adb shell pm clear " + appPackage;
        if (getCurrentPlatform().equalsIgnoreCase("mac")) {
            command = System.getenv("ANDROID_HOME") + "/platform-tools/" + command;
        }
        try {
            executeCommand(command);
        } catch (Exception e) {
            logger.error("Unable to clear cache for " + appPackage, e.getMessage());
            e.printStackTrace();
        }
    }

    public void stopAndroidApp(String appPackage) {
        String command = "adb shell am force-stop " + appPackage;
        if (getCurrentPlatform().equalsIgnoreCase("mac")) {
            command = System.getenv("ANDROID_HOME") + "/platform-tools/" + command;
        }
        try {
            executeCommand(command);
        } catch (Exception e) {
            logger.error("Unable to stop app " + appPackage, e.getMessage());
            e.printStackTrace();
        }
    }

    public void killAndroidApp(String appPackage) {
        String command = "adb shell am kill " + appPackage;
        if (getCurrentPlatform().equalsIgnoreCase("mac")) {
            command = System.getenv("ANDROID_HOME") + "/platform-tools/" + command;
        }
        try {
            executeCommand(command);
        } catch (Exception e) {
            logger.error("Unable to stop app " + appPackage, e.getMessage());
            e.printStackTrace();
        }
    }

    public void launchAndroidApp(String appPackage, String appActivity) {
        String command = "adb shell am start -n " + appPackage + "/" + appPackage + appActivity;      // ".activity.InitialiseActivity"
        if (getCurrentPlatform().equalsIgnoreCase("mac")) {
            command = System.getenv("ANDROID_HOME") + "/platform-tools/" + command;
        }
        try {
            executeCommand(command);
        } catch (Exception e) {
            logger.error("Unable to launch app " + appPackage, e.getMessage());
            e.printStackTrace();
        }
    }
}
