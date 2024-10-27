package com.infy.BrowserController;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.function.Supplier;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

import io.github.bonigarcia.wdm.WebDriverManager;

public class RemoteFireFoxDriverController implements Supplier<WebDriver> {
	@Override
	public WebDriver get() {
		WebDriverManager.firefoxdriver().setup();
		FirefoxOptions options = new FirefoxOptions();
		//options.setCapability("marionette", false);
		options.addArguments("--start-maximized");
		RemoteWebDriver driver = null;
		try {
			driver = new RemoteWebDriver(new URL("http://localhost:4444"), options);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return driver;
	}
}
