package com.infy.driverFactory;

import java.util.Optional;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DriverManager {
    private static final Logger logger = LoggerFactory.getLogger(DriverManager.class);
    private static DriverManager instance;
    private static final ThreadLocal<WebDriver> driver = new ThreadLocal<>();
    private static BrowserType browserType;

    private DriverManager() {
        initializeBrowserType();
    }

    // Synchronized lazy initialization for thread safety
    public static synchronized DriverManager getInstance() {
        if (instance == null) {
            instance = new DriverManager();
        }
        return instance;
    }

    // Initialize browser type from system properties or config file
    private void initializeBrowserType() {
        // Check if the browser type is set via system property
        String browserFromTestNG = System.getProperty("browser");
        if (browserFromTestNG != null) {
            browserType = BrowserType.valueOf(browserFromTestNG.toUpperCase());
        } else {
            loadBrowserTypeFromConfig();
        }
    }

    // Load browser type from the configuration using ConfigLoaderUtility
    private void loadBrowserTypeFromConfig() {
        Optional<String> browserProperty = ConfigLoaderUtility.getProperty("browser");
        if (browserProperty.isPresent()) {
            browserType = BrowserType.valueOf(browserProperty.get().toUpperCase());
        } else {
            throw new RuntimeException("Browser type is not set in the configuration.");
        }
    }

    // Get the WebDriver instance, initializing if necessary
    public WebDriver getDriver() {
        if (driver.get() == null) {
            if (browserType == null) {
                throw new RuntimeException("Browser type is not set. Check your configuration.");
            }
            driver.set(createDriver());
            logger.info("WebDriver created for browser: {}", browserType);
        }
        return driver.get();
    }

    // Create the WebDriver based on the browser type
    private WebDriver createDriver() {
        boolean isHeadless = getBrowserOptions();
        switch (browserType) {
            case CHROME:
                return new ChromeDriverCreator("CHROME").create(isHeadless);
            case FIREFOX:
                return new FirefoxDriverCreator("FIREFOX").create(isHeadless);
            case EDGE:
                return new EdgeDriverCreator("EDGE").create(isHeadless);
            default:
                throw new UnsupportedOperationException("Unsupported browser type: " + browserType);
        }
    }

    // Quit the WebDriver instance
    public void quitDriver() {
        WebDriver currentDriver = driver.get();
        if (currentDriver != null) {
            currentDriver.quit();
            logger.info("WebDriver for browser {} has been quit", browserType);
            driver.remove();
        }
    }

    // Retrieve headless option from properties
    private boolean getBrowserOptions() {
        Optional<String> headlessProperty = ConfigLoaderUtility.getProperty("headless");
        boolean isHeadless = headlessProperty.map(Boolean::parseBoolean).orElse(false);
        logger.info("Headless mode is set to: {}", isHeadless);
        return isHeadless;
    }
}
