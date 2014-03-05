package net.skyscanner.utils;

import net.skyscanner.SharedRemoteWebDriver;

import java.io.IOException;

public class CommandLineExecutor {

    public static void executeCommand(String command) throws IOException, InterruptedException {
        String cmd = "";
        SharedRemoteWebDriver driver = new SharedRemoteWebDriver();

        if (driver.getCurrentPlatform().equalsIgnoreCase("win"))
            cmd = "cmd /c start, ";
        else if(driver.getCurrentPlatform().equalsIgnoreCase("linux"))
            cmd = "gnome-terminal -x";

        processor(cmd + command);
    }

    private static Process processor(String command) throws IOException, RuntimeException, InterruptedException {
        return processor(command);
    }
}
