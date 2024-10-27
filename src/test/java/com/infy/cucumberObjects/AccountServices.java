package com.infy.cucumberObjects;

import java.time.Duration;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class AccountServices {
	private WebDriver driver;
	WebDriverWait wait;
	
	@FindBy(xpath = "//a[contains(text(),'Open New Account')]")
    WebElement openNewAccountLink;
	
	@FindBy(xpath = "//a[contains(text(),'Accounts Overview')]")
    WebElement openAccountOverviewLink;
	
	@FindBy(xpath = "//a[contains(text(),'Transfer Funds')]")
    WebElement openTransferFundsLink;
	
	@FindBy(xpath = "//a[contains(text(),'Bill Pay')]")
    WebElement openBillPayLink;
	
	@FindBy(xpath = "//a[contains(text(),'Fund Transactions')]")
    WebElement openFundTransactionsLink;
	
	@FindBy(xpath = "//a[contains(text(),'Update Contact Info')]")
    WebElement openUpdateContactInfoLink;
	
	@FindBy(xpath = "//a[contains(text(),'Request Loan')]")
    WebElement openRequestLoanLink;
	
	@FindBy(xpath = "//a[contains(text(),'Log Out')]")
    WebElement logOutLink;
	
	public AccountServices(WebDriver driver) {
		this.driver = driver;
		this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		PageFactory.initElements(driver, this);
	}

	public void clickNewAccountLink() {
		wait.until(ExpectedConditions.elementToBeClickable(openNewAccountLink)).click();
	}
	
	public void clickAccountOverview() {
		wait.until(ExpectedConditions.elementToBeClickable(openAccountOverviewLink)).click();
	}
	
	public void clickTransferFunds() {
		wait.until(ExpectedConditions.elementToBeClickable(openTransferFundsLink)).click();
	}
	
	public void clickBillPay() {
		wait.until(ExpectedConditions.elementToBeClickable(openBillPayLink)).click();
	}
	
	public void clickFundTransactions() {
		wait.until(ExpectedConditions.elementToBeClickable(openFundTransactionsLink)).click();
	}
	
	public void clickUpdateContactInfo() {
		wait.until(ExpectedConditions.elementToBeClickable(openUpdateContactInfoLink)).click();
	}
	
	public void clickRequestLoan() {
		wait.until(ExpectedConditions.elementToBeClickable(openRequestLoanLink)).click();
	}
	
	public void clickLogout() {
		wait.until(ExpectedConditions.elementToBeClickable(logOutLink)).click();
	}
	
}
