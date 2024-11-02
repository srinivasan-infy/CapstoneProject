package com.infy.driverFactory;

public class WebDriverCreationException extends RuntimeException {
    public WebDriverCreationException(String browser, String message, Throwable cause) {
        super("Error creating " + browser + " driver: " + message, cause);
    }
}