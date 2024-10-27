package com.infy.cucumberObjects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class Logout {

	private WebDriver driver;
	
	@FindBy(xpath = "//a[contains(text(), 'Log Out')]")
	private WebElement logOut;
		
	public Logout(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}

	public void clickLogoutLink() {
		logOut.click();
	}
	
}
