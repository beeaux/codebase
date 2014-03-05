package net.skyscanner.utils;

public class AndroidAppDriver {
    private String appPackage;
    private String appActivity;
    
    public AndroidApp(String appPackage) {
      this.appPackage = appPackage;
    }
    
    public static void clearAndroidCache() {
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

    @After
    public static void stopAndroidApp() {
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

    public static void killAndroidApp() {
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

    public static void launchAndroidApp() {
        String command = "adb shell am start -n " + appPackage + "/" + appPackage + ".activity.InitialiseActivity";
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
