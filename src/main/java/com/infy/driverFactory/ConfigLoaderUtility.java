package com.infy.driverFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Optional;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConfigLoaderUtility {
    private static final Logger logger = LoggerFactory.getLogger(ConfigLoaderUtility.class);
    private static volatile Properties properties;
    private static final Object lock = new Object(); // Lock object for thread safety
    private static String propertiesFilePath; // Path to the properties file

    // Thread-safe method to set the properties file path
    public static synchronized void setPropertiesFilePath(String filePath) {
        propertiesFilePath = filePath;
    }

    // Load the properties from the specified properties file
    public static Properties loadProperties() {
        if (properties == null) {
            synchronized (lock) { 	//double check locking pattern implemented
                if (properties == null) { 
                    properties = new Properties();
                    if (propertiesFilePath == null || propertiesFilePath.isEmpty()) {
                        throw new IllegalStateException("Properties file path is not set!");
                    }
                    try (FileInputStream input = new FileInputStream(propertiesFilePath)) {
                        properties.load(input);
                    } catch (IOException e) {
                        logger.error("Failed to load properties file: {}", propertiesFilePath, e);
                        throw new RuntimeException("Could not load properties file", e);
                    }
                }
            }
        }
        return properties;
    }

    // Method to get the property value by key
    public static Optional<String> getProperty(String key) {
        return Optional.ofNullable(loadProperties().getProperty(key));
    }
}