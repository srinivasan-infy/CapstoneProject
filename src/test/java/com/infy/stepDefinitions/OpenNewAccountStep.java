package com.infy.stepDefinitions;

import io.cucumber.java.en.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import com.infy.cucumberObjects.*;
import com.infy.driverFactory.DriverManager;

import java.time.Duration;
import java.util.Map;
import static org.testng.Assert.*;
import com.infy.utility.ExcelUtility;
import com.infy.utility.ExtendReportScreenShot;
import com.infy.utility.StepDefinitionUtility;

public class OpenNewAccountStep {
	private static final Logger logger = LoggerFactory.getLogger(OpenNewAccountStep.class);
	private String newAccountNumber;
	private String deductedFromAccount;
	private double minimumDeposit;
	private FluentWait<WebDriver> wait;

	private WebDriver driver;
    private AccountOverview accountOverview;
    private OpenNewAccount openNewAccount;
    private AccountServices accountServices;
  
    public OpenNewAccountStep() {
        this.driver = DriverManager.getInstance().getDriver();
        this.wait = new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(30))
                .pollingEvery(Duration.ofSeconds(5)) 
                .ignoring(Exception.class);
        initializeObjects();
    }
    
    private void initializeObjects() {
    	accountOverview = new AccountOverview(driver);
        openNewAccount = new OpenNewAccount(driver);
        accountServices = new AccountServices(driver);
    }	
	
	@Then("User select the account type {string}")
	public void user_select_the_account_type(String accountType) {
		try {
			logger.info("Selecting account type: {}", accountType);
			openNewAccount.selectAccountType(accountType);
			ExtendReportScreenShot.logScreenshotInfoEachStep(driver,
					"Step passed: Selected account type: " + accountType);
		} catch (Exception e) {
			ExtendReportScreenShot.logScreenshotInfoEachStep(driver, "Step failed: Selecting account type");
			logger.error("Error selecting account type: {}", e.getMessage());
			Assert.fail("Failed to select account type", e);
		}
	}

	@Then("User fetch the Minimum deposit amount details")
	public void user_fetch_the_minimum_deposit_amount_details() {
		try {
			minimumDeposit = openNewAccount.fetchMinimumDeposit();
			logger.info("Minimum deposit fetched: {}", minimumDeposit);
			ExtendReportScreenShot.logScreenshotInfoEachStep(driver,
					"Step passed: Fetched minimum deposit: " + minimumDeposit);
		} catch (Exception e) {
			ExtendReportScreenShot.logScreenshotInfoEachStep(driver, "Step failed: Fetching minimum deposit amount");
			logger.error("Error fetching minimum deposit: {}", e.getMessage());
			Assert.fail("Failed to fetch minimum deposit", e);
		}
	}

	@Then("User choose the account and click on Open New Account")
	public void user_choose_the_account_and_click_on_open_new_account() {
		try {
			logger.info("Choosing account to deduct funds from");
			wait.until(ExpectedConditions.visibilityOf(openNewAccount.getAccountDropdown()));
			deductedFromAccount = openNewAccount.selectAccountNumber();
			logger.info("Deducting from account: {}", deductedFromAccount);

			wait.until(ExpectedConditions.elementToBeClickable(openNewAccount.getOpenAccountButton()));
			openNewAccount.clickOpenAccount();
			logger.info("Clicked 'Open New Account' button");
			ExtendReportScreenShot.logScreenshotInfoEachStep(driver, "Step passed: Clicked 'Open New Account'");
		} catch (Exception e) {
			ExtendReportScreenShot.logScreenshotInfoEachStep(driver,
					"Step failed: Choosing account and opening new account");
			logger.error("Error while opening new account: {}", e.getMessage());
			Assert.fail("Failed to open new account", e);
		}
	}

	@Then("User verify account opened status")
	public void user_verify_account_opened_status() {
		try {
			wait.until(ExpectedConditions.textToBePresentInElement(openNewAccount.getAccountOpened(), "Account Opened!"));

			assertTrue(openNewAccount.isAccountOpened(), "Account was not opened.");
			assertTrue(openNewAccount.isSuccessMessage(), "Success message not displayed.");
			assertTrue(openNewAccount.isAccountNumberText(), "Account number text not found.");

			// Fetch and log the new account number
			newAccountNumber = openNewAccount.getNewAccountNumber();
			logger.info("Account opened successfully. New account number: {}", newAccountNumber);
			ExtendReportScreenShot.logScreenshotInfoEachStep(driver,
					"Step passed: Account opened successfully with number: " + newAccountNumber);
		} catch (Exception e) {
			ExtendReportScreenShot.logScreenshotInfoEachStep(driver, "Step failed: Verifying account opened status");
			logger.error("Error occurred while verifying account opened status.", e);
			Assert.fail("Verification of account opened status failed", e);
		}
	}

	@And("User click the Account Overview link")
	public void user_click_the_account_overview_link() {
		try {
			accountServices.clickAccountOverview();
			logger.info("Navigating to Account Overview");
			//wait.until(ExpectedConditions.visibilityOfAllElements(accountOverview.getAccountTableBody()));
			ExtendReportScreenShot.logScreenshotInfoEachStep(driver, "Step passed: Clicked on Account Overview link");
		} catch (Exception e) {
			ExtendReportScreenShot.logScreenshotInfoEachStep(driver, "Step failed: Clicking Account Overview link");
			logger.error("Error clicking Account Overview link: {}", e.getMessage());
			Assert.fail("Failed to click Account Overview link", e);
		}
	}

	@Then("User verifies the created new account and the deduction from the other account")
	public void user_verifies_created_account_and_deduction() {
		try {
			StepDefinitionUtility.verifyAccountAndBalance(accountOverview, newAccountNumber, minimumDeposit);
			StepDefinitionUtility.verifyDeductionFromAccount(accountOverview, deductedFromAccount, minimumDeposit);
			logger.info("Account verification and deduction from the other account successful.");
			ExtendReportScreenShot.logScreenshotInfoEachStep(driver,
					"Step passed: Verified new account and deduction from account: " + deductedFromAccount);
		} catch (Exception e) {
			ExtendReportScreenShot.logScreenshotInfoEachStep(driver, "Step failed: Verifying account and deduction");
			logger.error("Error verifying account and deduction: {}", e.getMessage());
			Assert.fail("Account verification and deduction failed", e);
		}
	}
}
