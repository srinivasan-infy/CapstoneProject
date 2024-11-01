package com.infy.driverFactory;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ChromeDriverCreator {
	private static final Logger logger = LoggerFactory.getLogger(ChromeDriverCreator.class);

	private final String browser;

    // Constructor that accepts the browser type
    public ChromeDriverCreator(String browser) {
        this.browser = browser;
    }

    public WebDriver create(boolean headless) {
        try {
            // Set up the ChromeDriver using WebDriverManager
            WebDriverManager.chromedriver().setup();
            logger.info("ChromeDriver has been set up successfully.");

            // Set ChromeOptions for the driver
            ChromeOptions options = new ChromeOptions();
            if (headless) {
                options.addArguments("--headless");
                options.addArguments("--disable-gpu"); // Disable GPU hardware acceleration
                options.addArguments("--window-size=1920,1080"); // Set window size
                logger.info("Running Chrome in headless mode.");
            }
            options.addArguments("--start-maximized");
            options.addArguments("disable-features=PasswordManager");

            logger.info("ChromeOptions set: {}", options);

            // Create and return the ChromeDriver instance
            WebDriver driver = new ChromeDriver(options);
            logger.info("ChromeDriver instance created successfully.");
            return driver;

        } catch (Exception e) {
            logger.error("Failed to create ChromeDriver: {}", e.getMessage());
            throw new WebDriverCreationException(browser, "Could not initialize ChromeDriver", e);
        }
    }
    
}