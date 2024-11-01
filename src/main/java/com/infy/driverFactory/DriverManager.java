package com.infy.driverFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DriverManager {
	private static final Logger logger = LoggerFactory.getLogger(DriverManager.class);
	private static DriverManager instance;
    private static final ThreadLocal<WebDriver> driver = new ThreadLocal<>();
    private static BrowserType browserType;
    private Properties properties;

    private DriverManager() {
    	properties = loadProperties();
        initializeBrowserType();
    }

    // Synchronized lazy initialization for thread safety
    public static synchronized DriverManager getInstance() {
        if (instance == null) {
            instance = new DriverManager();
        }
        return instance;
    }

    private Properties loadProperties() {
    	Properties props = new Properties();
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("AutomationConfig.properties")) {
            props.load(inputStream);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load properties", e);
        }
        return props;
    }
    
    // Initialize browser type from system properties or config file
    private void initializeBrowserType() {
    	 String browserFromTestNG = System.getProperty("browser");
         if (browserFromTestNG != null) {
             browserType = BrowserType.valueOf(browserFromTestNG.toUpperCase());
         } else {
             loadBrowserTypeFromConfig();
         }
    }

    // Load browser type from the configuration file
    private void loadBrowserTypeFromConfig() {
        String browserProperty = properties.getProperty("browser");
        browserType = BrowserType.valueOf(browserProperty.toUpperCase());
    }

    // Get the WebDriver instance, initialize if necessary
    public WebDriver getDriver() {
        if (driver.get() == null) {
            if (browserType == null) {
                throw new RuntimeException("Browser type is not set. Check your configuration.");
            }
            driver.set(createDriver());
            logger.info("WebDriver created for browser: {}", browserType);
           // setDriverTimeouts(driver.get());
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
    
    private boolean getBrowserOptions() {
    	boolean isHeadless = Boolean.parseBoolean(properties.getProperty("headless"));
        logger.info("Headless mode is set to: {}", isHeadless);
        return isHeadless;
    }
}
