package com.infy.driverFactory;

import java.net.MalformedURLException;
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
		Optional<String> executionMode = ConfigLoaderUtility.getProperty("executionMode");
		DesiredCapabilities capabilities = new DesiredCapabilities();

		switch (browserType) {
		case CHROME:
			capabilities.setBrowserName("chrome");
			if (isHeadless) {
				capabilities.setCapability("goog:chromeOptions", new ChromeOptions().addArguments("--headless"));
			}
			break;
		case FIREFOX:
			capabilities.setBrowserName("firefox");
			if (isHeadless) {
				capabilities.setCapability("moz:firefoxOptions", new FirefoxOptions().addArguments("--headless"));
			}
			break;
		case EDGE:
			capabilities.setBrowserName("edge");
			break;
		default:
			throw new UnsupportedOperationException("Unsupported browser type: " + browserType);
		}

		// Check if execution is remote and set up the RemoteWebDriver
		if ("remote".equalsIgnoreCase(executionMode.get())) {
			if (GRID_URL.isPresent()) {
				try {
					// Convert GRID_URL to URI and then to URL
					URI uri = new URI(GRID_URL.get());
					logger.info("Connecting to Selenium Grid at: {}", GRID_URL.get());
					return new RemoteWebDriver(uri.toURL(), capabilities);
				} catch (Exception e) {
					throw new RuntimeException("Invalid Selenium Grid URL: " + GRID_URL.get(), e);
				}
			} else {
				throw new RuntimeException("Selenium Grid URL is not provided. Set the 'selenium.grid.url' property.");
			}
		} else {
			// Local execution
			logger.info("Starting local WebDriver for browser: {}", browserType);
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
