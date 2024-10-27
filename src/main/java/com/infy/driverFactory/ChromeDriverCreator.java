package com.infy.driverFactory;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ChromeDriverCreator {
    private static final Logger logger = LoggerFactory.getLogger(ChromeDriverCreator.class);

    public WebDriver create() {
        try {
            // Set up the ChromeDriver using WebDriverManager
            WebDriverManager.chromedriver().setup();
            logger.info("ChromeDriver has been set up successfully.");

            // Set ChromeOptions for the driver
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--start-maximized"); // Start maximized

            // Create and return the ChromeDriver instance
            WebDriver driver = new ChromeDriver(options);
            logger.info("ChromeDriver instance created successfully.");
            return driver;

        } catch (Exception e) {
            logger.error("Failed to create ChromeDriver: {}", e.getMessage());
            throw new RuntimeException("Could not initialize ChromeDriver", e);
        }
    }
}