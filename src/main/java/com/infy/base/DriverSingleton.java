package com.infy.base;

import java.util.Objects;

import org.openqa.selenium.WebDriver;

public class DriverSingleton {

	private static final ThreadLocal<WebDriver> webDriver = new ThreadLocal<>();

	private static DriverSingleton instance = null;

	// Empty Private constructor of the class to restrict object creation outside of
	// the class.
	private DriverSingleton() {

	}

	// Public method to provide access to the singleton instance
	public static synchronized DriverSingleton getInstance() {
		if (Objects.isNull(instance)) {
			instance = new DriverSingleton();
		}
		return instance;
	}

	// Method to get the WebDriver
	public WebDriver getDriver() {
		return webDriver.get();
	}

	// Method to set the WebDriver
	public void setDriver(WebDriver driver) {
		webDriver.set(driver);
	}

	// Method to access the getDriver from other/outside class
	public static WebDriver getCurrentDriver() {
		return getInstance().getDriver();
	}
	
}
