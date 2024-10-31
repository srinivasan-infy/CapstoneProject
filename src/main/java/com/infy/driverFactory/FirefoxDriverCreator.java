package com.infy.driverFactory;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FirefoxDriverCreator {
    private static final Logger logger = LoggerFactory.getLogger(FirefoxDriverCreator.class);

    public WebDriver create() {
        try {
            // Set up the FirefoxDriver using WebDriverManager
            WebDriverManager.firefoxdriver().setup();
            logger.info("FirefoxDriver has been set up successfully.");

            // Set FirefoxOptions for the driver
            FirefoxOptions options = new FirefoxOptions();
            // Add any desired options here
            options.addPreference("signon.rememberSignons", false);
            options.addPreference("signon.autofillForms", false);
            options.addPreference("signon.autofillForms.http", false);
            
            options.addArguments("--start-maximized"); // Example option

            // Create and return the FirefoxDriver instance
            WebDriver driver = new FirefoxDriver(options);
            logger.info("FirefoxDriver instance created successfully.");
            return driver;

        } catch (Exception e) {
            logger.error("Failed to create FirefoxDriver: {}", e.getMessage());
            throw new RuntimeException("Could not initialize FirefoxDriver", e);
        }
    }
}