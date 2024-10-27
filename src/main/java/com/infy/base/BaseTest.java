package com.infy.base;

import java.time.Duration;
import java.util.Objects;
import java.util.function.Supplier;
import org.aeonbits.owner.ConfigFactory;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestContext;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import com.infy.BrowserController.BrowserController;
import com.infy.driverFactory.AutomationConfig;


public class BaseTest{
	WebDriver driver = null;
	WebDriverWait wait;

	BrowserController browserController = BrowserController.getInstance();
	
	@BeforeClass
	public void setUp(ITestContext context) {
		//String browser = System.getProperty("browser", "firefox");
		String browser = context.getCurrentXmlTest().getParameter("browser");
		System.out.println("1. Browser Name  " + browser);
		if (browser == null ) {
			AutomationConfig automationConfig = ConfigFactory.create(AutomationConfig.class);
			browser = automationConfig.browser();
		}
		driver = browserController.initializeDriver(browser);
		driver.get("https://www.saucedemo.com/");
		DriverSingleton.getInstance().setDriver(driver);
		wait = new WebDriverWait(driver, Duration.ofSeconds(10));
	}

	@AfterClass
	public void tearDown() {
		Supplier<WebDriver> currentDriverSupplier = DriverSingleton::getCurrentDriver;
		try {
			WebDriver currentDriver = currentDriverSupplier.get();
			if (Objects.nonNull(currentDriver)) {
				currentDriver.quit();
				DriverSingleton.getInstance().setDriver(null);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}