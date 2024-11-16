package com.infy.stepDefinitions;

import java.util.Map;
import org.openqa.selenium.WebDriver;
import com.infy.cucumberObjects.*;
import com.infy.driverFactory.ConfigLoaderUtility;
import com.infy.driverFactory.DriverManager;
import com.infy.utility.*;
import io.cucumber.java.en.Then;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

public class DepositFundAPISteps{
    private static final Logger logger = LoggerFactory.getLogger(DepositFundAPISteps.class);
    private WebDriver driver;
    private AccountOverview accountOverview;
    private TestContext testContext;
    public String accountID, custID;
    public String[] balanceData, afterDepositBalanceData;
    private static final String ACCOUNT_API_PATH = "/services/bank/accounts/";
    private static final String CUSTOMER_ID_KEY = "customerId";
    private final String baseUrl= ConfigLoaderUtility.getProperty("baseURL").orElse("https://parabank.parasoft.com/parabank");
    
    public DepositFundAPISteps(TestContext context) {
        this.testContext = context;
        this.driver = DriverManager.getInstance().getDriver();
        initializeObjects();
    }

    private void initializeObjects() {
        accountOverview = new AccountOverview(driver);
    }
    
    @Then("User gets the account id from the Account Overview")
    public void user_gets_the_account_id_from_the_account_overview() {
        try {
            accountID = StepDefinitionUtility.fetchRandomCellData(accountOverview.getAccountTableBody());
            Map<String, String[]> accountData = fetchAccountData();

            if (accountData.isEmpty()) {
                logger.warn("No data found in the web table.");
                return;
            }

            logger.info("Data fetched from web table: {} rows.", accountData.size());
            testContext.setBalanceAmount(accountData.get(accountID));
            ExtendReportScreenShot.logScreenshotInfoEachStep(driver, "Step passed: Fetched account ID: " + accountID);
        } catch (Exception e) {
            ExtendReportScreenShot.logScreenshotInfoEachStep(driver, "Step failed: Fetching account ID");
            logger.error("Error fetching account ID: {}", e.getMessage());
            Assert.fail("Failed to get account ID", e);
        }
    }

    @Then("User extracts the CustomerID based on Account ID using API get request")
    public void user_extracts_the_customerID_based_on_account_ID_using_API_get_request() {
        try {
            String custID = APIUtility.sendGetRequestAndFetchKey(baseUrl, ACCOUNT_API_PATH, accountID, CUSTOMER_ID_KEY);
            if (custID == null || custID.isEmpty()) {
                logger.error("Customer ID not found for Account ID: {}", accountID);
            } else {
                logger.info("Customer ID fetched: {}", custID);
            }
        } catch (Exception e) {
            logger.error("Error fetching Customer ID: {}", e.getMessage());
            Assert.fail("Failed to fetch Customer ID", e);
        }
    }

    @Then("User deposits {string} dollar for Customer")
    public void user_deposits_dollar_for_Customer(String amount) {
        try {
            testContext.setDepositAmount(amount);
            logger.info("Depositing ${} for account: {}", amount, accountID);
            APIUtility.sendPostRequestAndVerifyResponse(baseUrl, accountID, testContext.getDepositAmount());
        } catch (Exception e) {
            logger.error("Error during deposit: {}", e.getMessage());
            Assert.fail("Deposit operation failed", e);
        }
    }

    @Then("User verifies that deposit through API is updated in the Web UI")
    public void user_verifies_that_deposit_through_API_is_updated_in_the_Web_UI() {
        try {
            Map<String, String[]> afterDepositAccountData = fetchAccountData();

            if (afterDepositAccountData.isEmpty()) {
                logger.warn("No data found in the web table after deposit.");
                return;
            }

            String[] afterDepositBalanceData = afterDepositAccountData.get(accountID);
            if (isDepositSuccessful(afterDepositBalanceData)) {
                logger.info("Deposit amount added successfully for account: {}", accountID);
                ExtendReportScreenShot.logScreenshotInfoEachStep(driver, "Step passed: Deposit verified successfully for account: " + accountID);
            } else {
                logger.error("Deposit amount was not added successfully for account: {}", accountID);
                ExtendReportScreenShot.logScreenshotInfoEachStep(driver, "Step failed: Deposit verification failed for account: " + accountID);
                Assert.fail("Deposit verification failed");
            }
        } catch (Exception e) {
            ExtendReportScreenShot.logScreenshotInfoEachStep(driver, "Step failed: Verifying deposit in UI");
            logger.error("Error verifying deposit in UI: {}", e.getMessage());
            Assert.fail("Deposit verification in UI failed", e);
        }
    }

    // Reusable method for fetching account data
    private Map<String, String[]> fetchAccountData() {
        logger.info("Fetching account data from the web table.");
        return StepDefinitionUtility.fetchWebTableData(accountOverview.getAccountTableBody());
    }

    // Reusable method for deposit verification
    private boolean isDepositSuccessful(String[] afterDepositBalanceData) {
        double depositAmountDouble = Double.parseDouble(testContext.getDepositAmount());
        String currentBalance = testContext.getBalanceAmount().replace("$", "").replace(",", "");
        double currentBalanceDouble = Double.parseDouble(currentBalance);

        afterDepositBalanceData[0] = afterDepositBalanceData[0].replace("$", "").replace(",", "");
        double afterDepositBalanceDouble = Double.parseDouble(afterDepositBalanceData[0]);

        boolean result = afterDepositBalanceDouble == (depositAmountDouble + currentBalanceDouble);
        logger.info("Deposit verification result: {}", result);
        return result;
    }
}