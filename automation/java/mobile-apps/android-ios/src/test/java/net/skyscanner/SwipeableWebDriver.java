package net.skyscanner;

import org.openqa.selenium.Capabilities;
import org.openqa.selenium.interactions.HasTouchScreen;
import org.openqa.selenium.interactions.TouchScreen;
import org.openqa.selenium.remote.RemoteTouchScreen;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.URL;

public class SwipeableWebDriver extends RemoteWebDriver implements HasTouchScreen {
    private RemoteTouchScreen touchScreen;

    public SwipeableWebDriver(URL url, Capabilities capabilities) {
        super(url, capabilities);
        touchScreen = new RemoteTouchScreen(getExecuteMethod());
    }

    public TouchScreen getTouch() {
        return touchScreen;
    }
}
