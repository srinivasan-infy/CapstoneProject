package com.infy.utility;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigLoaderUtility {

    private static Properties properties;

    // Load the properties from the config.properties file
    public static Properties loadProperties() {
        if (properties == null) {
            properties = new Properties();
            try {
                FileInputStream input = new FileInputStream("./src/main/resources/AutomationConfig.properties");
                properties.load(input);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return properties;
    }

    // Method to get the property value by key
    public static String getProperty(String key) {
        return loadProperties().getProperty(key);
    }
}
