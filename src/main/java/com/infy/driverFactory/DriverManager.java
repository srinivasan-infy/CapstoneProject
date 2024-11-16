package com.infy.driverFactory;

import java.net.URI;
import java.util.Optional;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DriverManager {
	private static final Logger logger = LoggerFactory.getLogger(DriverManager.class);
	private static DriverManager instance;
	private static final ThreadLocal<WebDriver> driver = new ThreadLocal<>();
	private static BrowserType browserType;
	private static final Optional<String> GRID_URL = ConfigLoaderUtility.getProperty("selenium.grid.url");

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

	private void initializeBrowserType() {
	    String browserFromTestNG = System.getProperty("browser");
	    logger.info("browserFromTestNG: {}", browserFromTestNG);
	    String browser = (browserFromTestNG != null) 
	        ? browserFromTestNG 
	        : ConfigLoaderUtility.getProperty("browser").orElseThrow(() -> 
	            new RuntimeException("Browser type is not set in the configuration."));
	    browserType = BrowserType.valueOf(browser.toUpperCase());
	}

	// Get the WebDriver instance, initializing if necessary
	public WebDriver getDriver() {
		if (driver.get() == null) {
			driver.set(createDriver());
			logger.info("WebDriver created for browser: {}", browserType);
		}
		return driver.get();
	}

	// Create the WebDriver based on the browser type
	private WebDriver createDriver() {
        boolean isHeadless = getBrowserOptions();
        String executionMode = ConfigLoaderUtility.getProperty("executionMode").orElse("local");

        if ("remote".equalsIgnoreCase(executionMode)) {
            return createRemoteDriver(getCapabilities(isHeadless));
        } else {
            return createLocalDriver(isHeadless);
        }
    }

    private WebDriver createRemoteDriver(DesiredCapabilities capabilities) {
        try {
            URI uri = new URI(getGridUrl());
            logger.info("Connecting to Selenium Grid at: {}", GRID_URL.get());
            return new RemoteWebDriver(uri.toURL(), capabilities);
        } catch (Exception e) {
            throw new RuntimeException("Invalid Selenium Grid URL: " + getGridUrl(), e);
        }
    }

    private WebDriver createLocalDriver(boolean isHeadless) {
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

    private DesiredCapabilities getCapabilities(boolean isHeadless) {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        switch (browserType) {
            case CHROME:
                capabilities.setBrowserName("chrome");
                if (isHeadless) capabilities.setCapability("goog:chromeOptions", new ChromeOptions().addArguments("--headless"));
                break;
            case FIREFOX:
                capabilities.setBrowserName("firefox");
                if (isHeadless) capabilities.setCapability("moz:firefoxOptions", new FirefoxOptions().addArguments("--headless"));
                break;
            case EDGE:
                capabilities.setBrowserName("edge");
                break;
        }
        return capabilities;
    }

	// Quit the WebDriver instance
	public void quitDriver() {
	    WebDriver currentDriver = driver.get();
	    if (currentDriver != null) {
	        try {
	            currentDriver.quit();
	            logger.info("WebDriver for browser {} has been quit", browserType);
	        } catch (Exception e) {
	            logger.error("Failed to quit WebDriver for browser {}: {}", browserType, e.getMessage());
	        } finally {
	            driver.remove();
	        }
	    }
	}

	// Retrieve headless option from properties
	private boolean getBrowserOptions() {
        return ConfigLoaderUtility.getProperty("headless").map(Boolean::parseBoolean).orElse(false);
    }

    private String getGridUrl() {
        return GRID_URL.orElseThrow(() -> new RuntimeException("Selenium Grid URL is not provided."));
    }
}
