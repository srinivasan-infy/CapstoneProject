package com.infy.driverFactory;


import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.openqa.selenium.WebDriver;

import com.infy.base.BrowserType;
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
        String browserFromTestNG = System.getProperty("browser");
        if (browserFromTestNG != null) {
            try {
                browserType = BrowserType.valueOf(browserFromTestNG.toUpperCase());
                logger.info("Browser type set from TestNG: {}", browserType);
            } catch (IllegalArgumentException e) {
                logger.warn("Invalid browser specified: {}. Loading from config.", browserFromTestNG);
                loadBrowserTypeFromConfig();
            }
        } else {
            loadBrowserTypeFromConfig();
        }
    }

    // Load browser type from the configuration file
    private void loadBrowserTypeFromConfig() {
        Properties properties = new Properties();
        try (InputStream inputStream = DriverManager.class.getClassLoader().getResourceAsStream("AutomationConfig.properties")) {
            if (inputStream == null) {
                throw new RuntimeException("Configuration file not found");
            }
            properties.load(inputStream);
            String browserProperty = properties.getProperty("browser");
            if (browserProperty == null) {
                throw new RuntimeException("Browser property is not specified in the configuration file");
            }
            browserType = BrowserType.valueOf(browserProperty.toUpperCase());
            logger.info("Browser type set from config: {}", browserType);
        } catch (IOException ex) {
            throw new RuntimeException("Failed to load configuration file", ex);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Unsupported browser type specified in configuration file", e);
        }
    }

    // Get the WebDriver instance, initialize if necessary
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
        switch (browserType) {
            case CHROME:
                return new ChromeDriverCreator().create();
            case FIREFOX:
                return new FirefoxDriverCreator().create();
            case EDGE:
                return new EdgeDriverCreator().create();
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
}
