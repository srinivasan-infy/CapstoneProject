package com.infy.driverFactory;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FirefoxDriverCreator {
    private static final Logger logger = LoggerFactory.getLogger(FirefoxDriverCreator.class);
    private final String browser;

    // Constructor that accepts the browser type
    public FirefoxDriverCreator(String browser) {
        this.browser = browser;
    }

    public WebDriver create(boolean headless) {
        try {
            // Set up the FirefoxDriver using WebDriverManager
            WebDriverManager.firefoxdriver().setup();
            logger.info("FirefoxDriver has been set up successfully.");

            // Set FirefoxOptions for the driver
            FirefoxOptions options = new FirefoxOptions();
        
            // Configure headless mode
            if (headless) {
                options.addArguments("-headless"); // Enable headless mode
                options.addArguments("--width=1920"); // Set width for headless mode
                options.addArguments("--height=1080"); // Set height for headless mode
                logger.info("Running Firefox in headless mode.");
            } else {
                options.addArguments("--start-maximized"); // Maximize window if not headless
            }

            // Additional Firefox preferences
            options.addPreference("browser.formfill.enable", false); 
            options.addPreference("extensions.formautofill.addresses.enabled", false); 
            options.addPreference("extensions.formautofill.addresses.capture.enabled", false);
            options.addPreference("signon.rememberSignons", false); 
            options.addPreference("signon.autofillForms", false); 
            logger.info("FirefoxOptions set: {}", options);

            // Create and return the FirefoxDriver instance
            WebDriver driver = new FirefoxDriver(options);
            
            if (!headless) {
                driver.manage().window().maximize();
                logger.info("Firefox browser window maximized.");
            }            
            
            logger.info("FirefoxDriver instance created successfully.");
            return driver;

        } catch (Exception e) {
            logger.error("Failed to create FirefoxDriver: {}", e.getMessage());
            throw new RuntimeException("Could not initialize FirefoxDriver", e);
        }
    }
}