package com.infy.driverFactory;

/* class ChromeDriverCreationException extends RuntimeException {
    public ChromeDriverCreationException(String message, Throwable cause) {
        super(message, cause);
    }
}  */


public class WebDriverCreationException extends RuntimeException {
    public WebDriverCreationException(String browser, String message, Throwable cause) {
        super("Error creating " + browser + " driver: " + message, cause);
    }
}