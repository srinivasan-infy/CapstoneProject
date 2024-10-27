package com.infy.testCases;

import java.time.Duration;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;

import com.infy.base.BaseTest;
import com.infy.base.DriverSingleton;
import com.infy.pageObjects.LoginPage;
import com.infy.pageObjects.ProductPage;

public class TestLogin extends BaseTest {
	WebDriverWait wait;

	@Test
	public void MavenParamTest() throws InterruptedException {
		WebDriver driver = DriverSingleton.getCurrentDriver();
		wait = new WebDriverWait(driver, Duration.ofSeconds(15));
		driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(20));

		LoginPage loginpage = new LoginPage();
		loginpage.login("standard_user", "secret_sauce");
		ProductPage productPage = new ProductPage();
		productPage.waitforLandingPage();
		Thread.sleep(2000);
	}
}
