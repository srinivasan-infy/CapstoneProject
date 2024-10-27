package com.infy.stepDefinitions;

import com.infy.cucumberObjects.*;
import com.infy.driverFactory.DriverManager;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoginSteps extends BaseStep {
    private static final Logger logger = LoggerFactory.getLogger(LoginSteps.class);
   
    @Given("User is in the login page")
    public void user_is_in_the_login_page() {
    	logger.info("Navigating to login page");
        DriverManager.getInstance().getDriver().get("https://parabank.parasoft.com/parabank/index.htm");
    }
           
    @When("User enters valid credentials {string} and {string}")
    public void user_enters_valid_credentials_and(String username, String password) {
    	logger.info("Entering valid credentials: username={}, password={}", username, password);
        loginPage.enterUsername(username);
        loginPage.enterPassword(password);
    }
    
    @When("User enter valid credentials")
    public void user_enter_valid_credentials() {
    	logger.info("Entering hardcoded valid credentials");
    	loginPage.enterUsername("standard_user");
        loginPage.enterPassword("secret_sauce");
    }
    
    @When("User enters invalid credentials {string} and {string}")
    public void user_enters_invalid_credentials_and(String username, String password) {
    	logger.warn("Entering invalid credentials: username={}, password={}", username, password);
    	loginPage.enterUsername(username);
        loginPage.enterPassword(password);
    }
    
    @When("User enter invalid credentials")
    public void user_enter_invalid_credentials() {
    	logger.warn("Entering hardcoded invalid credentials");
    	loginPage.enterUsername("standard_user");
        loginPage.enterPassword("secret_sauce1");
    }

    @And("User click the login button")
    public void user_click_the_login_button() {
    	logger.info("Clicking login button");
    	loginPage.clickLoginButton();
    }
    
    @Then("User should be navigate to home page {string}")
    public void user_should_be_navigate_to_home_page(String expectedMessage) {
    	String actualMessage = loginPage.accountOverview();
    	logger.info("Validating navigation to home page. Expected: {}, Actual: {}", expectedMessage, actualMessage);
    	Assert.assertEquals(actualMessage, expectedMessage, "User is not on the " + expectedMessage + " page");
    }  
    
    @Then("User should see an error message {string}")
    public void user_should_see_an_error_message(String expectedMessage) {
    	String actualErrorMessage = loginPage.invalidCred();
    	logger.error("Expected error message: {}, Actual: {}", expectedMessage, actualErrorMessage);
    	Assert.assertTrue(actualErrorMessage.contains(expectedMessage), "Expected error message not found. User is logged into the system instead.");
    }
    
    @Then("User should see an error message1 {string}")
    public void user_should_see_an_error_message1(String expectedMessage) {
    	String actualMessage = loginPage.userpwdMissing();
    	logger.error("Validating missing password message. Expected: {}, Actual: {}", expectedMessage, actualMessage);
    	Assert.assertTrue(actualMessage.contains(expectedMessage), "Expected message for missing password not found. User is logged into the system instead.");
    }

    @Then("Logout after successful login")
    public void logout_after_successful_login() {
    	logger.info("Logging out after successful login");
    	logout.clickLogoutLink();
    }   
    
}