package com.infy.testCases;

import java.time.Duration;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;

import com.infy.base.BaseTest;
import com.infy.base.DriverSingleton;
import com.infy.pageObjects.LoginPage;

public class TestLogin_Invalid extends BaseTest {
	WebDriverWait wait;

	@Test
	public void MavenParamTest() throws InterruptedException {

		WebDriver driver = DriverSingleton.getCurrentDriver();

		wait = new WebDriverWait(driver, Duration.ofSeconds(15));

		driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(20));

		LoginPage loginpage = new LoginPage();
		loginpage.login("standard_user", "secret_sauce1");

		Thread.sleep(2000);
	}
}
