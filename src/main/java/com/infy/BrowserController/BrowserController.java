package com.infy.BrowserController;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import com.infy.base.DriverSingleton;

public class BrowserController {
    private static BrowserController instance = null;
    private static final Map<String, Supplier<WebDriver>> driverMap = new HashMap<>();

    private BrowserController() {
		driverMap.put("chrome", new ChromeDriverController());
		driverMap.put("firefox", new FirefoxDriverController());
		driverMap.put("remote-chrome", new RemoteChromeDriverController());
		driverMap.put("remote-firefox", new RemoteFireFoxDriverController());
       // driverMap.put("remote-chrome", () -> getRemoteDriver("chrome"));
        //driverMap.put("remote-firefox", () -> getRemoteDriver("firefox"));
        // Add more browsers as needed
    }

    public static BrowserController getInstance() {
        if (instance == null) {
            synchronized (BrowserController.class) {
                if (instance == null) {
                    instance = new BrowserController();
                }
            }
        }
        return instance;
    }

    public WebDriver initializeDriver(String browser) {
        Supplier<WebDriver> driverSupplier = driverMap.get(browser.toLowerCase());
        if (driverSupplier != null) {
            WebDriver driver = driverSupplier.get();
            DriverSingleton.getInstance().setDriver(driver);
            return driver;
        }
        throw new IllegalArgumentException("Browser not in the list: " + browser);
    }

    private WebDriver getRemoteDriver(String browser) {
        WebDriver driver = null;
        try {
            MutableCapabilities options = new MutableCapabilities();
            switch (browser) {
                case "chrome":
                    ChromeOptions chromeOptions = new ChromeOptions();
                    options.merge(chromeOptions);
                    break;
                case "firefox":
                    FirefoxOptions firefoxOptions = new FirefoxOptions();
                    options.merge(firefoxOptions);
                    break;
                default:
                    throw new UnsupportedOperationException("Unsupported browser: " + browser);
            }
            options.setCapability("browserName", browser);
            driver = new RemoteWebDriver(new URL("http://localhost:4444"), options);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return driver;
    }
}