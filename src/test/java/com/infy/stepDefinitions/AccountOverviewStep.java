package com.infy.stepDefinitions;

import io.cucumber.java.en.*;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import com.infy.cucumberObjects.*;
import com.infy.driverFactory.DriverManager;
import com.infy.utility.ExtendReportScreenShot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AccountOverviewStep extends BaseStep {
    // Logger for this class
    private static final Logger logger = LoggerFactory.getLogger(AccountOverviewStep.class);
    
    @Then("User clicks on the Open New Account link")
    public void user_clicks_on_the_open_new_account_link() {
        try {
            logger.info("Clicking on the 'Open New Account' link.");
            accountOverview.clickOpenNewAccount();
            ExtendReportScreenShot.logScreenshotInfoEachStep(driver, "Step passed: Clicked on Open New Account link");
        } catch (Exception e) {
            ExtendReportScreenShot.logScreenshotInfoEachStep(driver, "Step failed: Clicking on Open New Account link");
            logger.error("Error clicking on the Open New Account link: {}", e.getMessage());
            Assert.fail("Failed to click on the Open New Account link", e);
        }
    }

    @Then("User verifies the Account Overview page is displayed")
    public void user_verifies_the_account_overview_page_is_displayed() {
        try {
            boolean isDisplayed = accountOverview.isAccountOverviewDisplayed();
            logger.info("Verifying if the Account Overview page is displayed: {}", isDisplayed);
            Assert.assertTrue(isDisplayed, "Account Overview page is not displayed.");
            ExtendReportScreenShot.logScreenshotInfoEachStep(driver, "Step passed: Account Overview page displayed successfully");
        } catch (AssertionError e) {
            ExtendReportScreenShot.logScreenshotInfoEachStep(driver, "Step failed: Verifying Account Overview page");
            logger.error("Account Overview page not displayed: {}", e.getMessage());
            Assert.fail("Account Overview page verification failed", e);
        }
    }

    @Then("User verifies the table header is present")
    public void user_verifies_the_table_header_is_present() {
        try {
            boolean isHeaderPresent = accountOverview.verifyTableHeadersInSequence();
            logger.info("Verifying if the table headers are in sequence: {}", isHeaderPresent);
            Assert.assertTrue(isHeaderPresent, "Table headers are not in sequence.");
            ExtendReportScreenShot.logScreenshotInfoEachStep(driver, "Step passed: Table headers are present and correct");
        } catch (AssertionError e) {
            ExtendReportScreenShot.logScreenshotInfoEachStep(driver, "Step failed: Verifying table header presence");
            logger.error("Table headers are not in sequence: {}", e.getMessage());
            Assert.fail("Table header verification failed", e);
        }
    }

    @Then("User verifies account details are valid")
    public void user_verifies_account_details_are_valid() {
        try {
            boolean areDetailsValid = accountOverview.isAccountDetailsValid();
            logger.info("Verifying if the account details are valid: {}", areDetailsValid);
            Assert.assertTrue(areDetailsValid, "Account details are invalid.");
            ExtendReportScreenShot.logScreenshotInfoEachStep(driver, "Step passed: Account details are valid");
        } catch (AssertionError e) {
            ExtendReportScreenShot.logScreenshotInfoEachStep(driver, "Step failed: Verifying account details validity");
            logger.error("Account details are not valid: {}", e.getMessage());
            Assert.fail("Account details verification failed", e);
        }
    }

    @Then("User verifies Balance includes deposits text is displayed")
    public void user_verifies_balance_includes_deposits_text_is_displayed() {
        try {
            boolean isTextDisplayed = accountOverview.isBalanceIncludesDepositsTextDisplayed();
            logger.info("Verifying if 'Balance includes deposits' text is displayed: {}", isTextDisplayed);
            Assert.assertTrue(isTextDisplayed, "'Balance includes deposits' text is not displayed.");
            ExtendReportScreenShot.logScreenshotInfoEachStep(driver, "Step passed: 'Balance includes deposits' text is displayed");
        } catch (AssertionError e) {
            ExtendReportScreenShot.logScreenshotInfoEachStep(driver, "Step failed: Verifying balance includes deposits text");
            logger.error("'Balance includes deposits' text not displayed: {}", e.getMessage());
            Assert.fail("Balance includes deposits text verification failed", e);
        }
    }

    @Then("User verifies the total balance is correct")
    public void user_verifies_the_total_balance_is_correct() {
        try {
            boolean isBalanceCorrect = accountOverview.isTotalBalanceCorrect();
            logger.info("Verifying if the total balance is correct: {}", isBalanceCorrect);
            Assert.assertTrue(isBalanceCorrect, "The total balance is incorrect.");
            ExtendReportScreenShot.logScreenshotInfoEachStep(driver, "Step passed: The total balance is correct");
            logger.info("The total balance is correct.");
        } catch (AssertionError e) {
            ExtendReportScreenShot.logScreenshotInfoEachStep(driver, "Step failed: Verifying total balance correctness");
            logger.error("The total balance is incorrect: {}", e.getMessage());
            Assert.fail("Total balance verification failed", e);
        }
    }
}