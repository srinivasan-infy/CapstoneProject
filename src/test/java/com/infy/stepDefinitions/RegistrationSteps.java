package com.infy.stepDefinitions;

import com.infy.cucumberObjects.LoginPage;
import com.infy.cucumberObjects.Logout;
import com.infy.cucumberObjects.RegistrationPage;
import com.infy.driverFactory.ConfigLoaderUtility;
import com.infy.driverFactory.DriverManager;
import com.infy.utility.ExcelUtility;
import com.infy.utility.StepDefinitionUtility;
import io.cucumber.java.en.*;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class RegistrationSteps {
	private static final Logger logger = LoggerFactory.getLogger(RegistrationSteps.class);
	private WebDriver driver;
	private final String baseUrl;
    private RegistrationPage registrationPage;
    private LoginPage loginPage;
    private Logout logout;
	
	public RegistrationSteps() {
        // Load the base URL from properties
        this.driver = DriverManager.getInstance().getDriver(); // Initialize the driver from the singleton

        // Retrieve the base URL, providing a default if not found
        baseUrl = ConfigLoaderUtility.getProperty("baseURL").orElse("https://parabank.parasoft.com/parabank"); 
        initializeObjects();
    }
    
    private void initializeObjects() {
    	registrationPage = new RegistrationPage(driver);
    	loginPage = new LoginPage(driver);
    	logout = new Logout(driver);
    }
	
	
	@Given("User is on home page")
    public void user_is_on_home_page() {
        DriverManager.getInstance().getDriver().get(baseUrl);
        logger.info("Navigated to home page: " + baseUrl);
    }

    @Given("Verify user is on the registration page \\{{string}}")
    public void verify_user_is_on_the_registration_page(String expectedPageTitle) {
        loginPage.clickRegisterLink();
        String actualTitle = registrationPage.verifyRegistrationPage();
        Assert.assertTrue(actualTitle.contains(expectedPageTitle), "User is not on " + expectedPageTitle + " page");
        logger.info("Verified registration page title: Expected: " + expectedPageTitle + ", Actual: " + actualTitle);
    }

    @When("User enters registration details from excel file {string}")
    public void user_enters_registration_details_from_excel_file(String filePath) throws IOException {
        List<Map<String, String>> usersData = ExcelUtility.readExcelMap(filePath);
        logger.info("Read user registration data from file: " + filePath);

        for (Map<String, String> userDetails : usersData) {
            try {
                logger.debug("Processing registration for user: " + userDetails.get("Username"));
                StepDefinitionUtility.processUserRegistration(registrationPage, logout, loginPage, userDetails);
            } catch (NoSuchElementException e) {
                logger.error("Element not found during registration for user: " + userDetails.get("Username"), e);
                StepDefinitionUtility.handleException(userDetails, "Failure: Element not found - " + e.getMessage(), e);
            } catch (Exception e) {
                logger.error("Exception during registration for user: " + userDetails.get("Username"), e);
                StepDefinitionUtility.handleException(userDetails, "Failure: " + e.getMessage(), e);
            }
        }
        // Write once after all users are processed
        ExcelUtility.writeExcelMap(filePath, usersData);
        logger.info("Updated Excel file after processing all users");
    }
}