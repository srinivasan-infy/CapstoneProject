package com.infy.cucumberObjects;

import java.time.Duration;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class LoginPage {
	private WebDriver driver;
	WebDriverWait wait;
	
	@FindBy(name = "username")
	private WebElement usernameInput;

	@FindBy(name = "password")
	private WebElement passwordInput;

	@FindBy(xpath = "//input[@value='Log In']")
	private WebElement loginButton;
	
	@FindBy(xpath = "//p[contains(text(), 'Please enter a username and password.')]")
	WebElement errorMessageForCredential;
	
	@FindBy(xpath = "//p[contains(text(), 'The username and password could not be verified.')]")
	WebElement errorMessageForInvalid;
	
	@FindBy(xpath = "//h1[contains(text(),'Accounts Overview')]")
    WebElement accountOverviewTitle;
	
	@FindBy(xpath = "//a[contains(text(), 'Register')]")
	WebElement register;
 
	 
	public LoginPage(WebDriver driver) {
		this.driver = driver;
		this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		PageFactory.initElements(driver, this);
	}

	public void enterUsername(String username) {
		wait.until(ExpectedConditions.visibilityOf(usernameInput));
		usernameInput.clear();
		usernameInput.sendKeys(username);
	}

	public void enterPassword(String password) {
		wait.until(ExpectedConditions.visibilityOf(passwordInput));
		passwordInput.clear();
		passwordInput.sendKeys(password);
	}

	public void clickLoginButton() {
		wait.until(ExpectedConditions.elementToBeClickable(loginButton)).click();
	}
	
	public void clickRegisterLink() {
		wait.until(ExpectedConditions.elementToBeClickable(register)).click();
	}
	
	public String accountOverview() {
		wait.until(ExpectedConditions.visibilityOf(accountOverviewTitle));
        return accountOverviewTitle.getText();
	}

	public String userpwdMissing() {
		wait.until(ExpectedConditions.visibilityOf(errorMessageForCredential));
		return errorMessageForCredential.getText();
	}
	
	public String invalidCred() {
		wait.until(ExpectedConditions.visibilityOf(errorMessageForInvalid));
		return errorMessageForInvalid.getText();
	}
}