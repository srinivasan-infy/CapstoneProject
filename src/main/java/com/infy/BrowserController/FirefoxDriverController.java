package com.infy.BrowserController;

import java.util.function.Supplier;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import io.github.bonigarcia.wdm.WebDriverManager;

public class FirefoxDriverController implements Supplier<WebDriver> {
	@Override
	public WebDriver get() {
		WebDriverManager.firefoxdriver().setup();
		FirefoxOptions options = new FirefoxOptions();
		//options.setCapability("marionette", false);
		options.addArguments("--start-maximized");

		// Add any specific options you need
		return new FirefoxDriver(options);
	}
}
