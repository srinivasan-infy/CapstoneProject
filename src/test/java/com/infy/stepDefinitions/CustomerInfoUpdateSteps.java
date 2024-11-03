package com.infy.stepDefinitions;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Given;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

import com.infy.cucumberObjects.AccountOverview;
import com.infy.cucumberObjects.LoginPage;
import com.infy.cucumberObjects.RegistrationPage;
import com.infy.cucumberObjects.UpdateContactPage;
import com.infy.driverFactory.DriverManager;
import com.infy.utility.ExcelUtility;

public class CustomerInfoUpdateSteps {

    private WebDriver driver;
    private RegistrationPage registrationPage;
    private LoginPage loginPage;
    private UpdateContactPage updateContactPage;
    private AccountOverview accountOverview;
    private ExcelUtility excelUtils;

    public CustomerInfoUpdateSteps() {
        this.driver = DriverManager.getInstance().getDriver();
        excelUtils = new ExcelUtility();
        initializeObjects();
    }
    
    private void initializeObjects() {
    	registrationPage = new RegistrationPage(driver);
    	loginPage = new LoginPage(driver);
    	accountOverview = new AccountOverview(driver);
    	updateContactPage = new UpdateContactPage(driver);
    }
    
    
    @Given("User is on the registration page")
    public void userIsOnTheRegistrationPage() {
        driver.get("URL_OF_REGISTRATION_PAGE"); // Replace with actual URL
    }

    @When("User enters registration details from excel file {string}")
    public void userEntersRegistrationDetailsFromExcel(String filePath) {
        String[] registrationDetails = excelUtils.readData(filePath, "Sheet1"); // Modify as needed
        registrationPage.enterDetails(registrationDetails); // Implement this method in your Page Object
    }

    @When("User submits the registration form")
    public void userSubmitsTheRegistrationForm() {
        registrationPage.submitForm(); // Implement this method in your Page Object
    }

    @Then("User should see a confirmation message")
    public void userShouldSeeAConfirmationMessage() {
        String confirmationMessage = registrationPage.getConfirmationMessage(); // Implement this method
        assertEquals("Expected Confirmation Message", confirmationMessage); // Replace with actual expected message
    }

    @Then("Logout after successful login")
    public void logoutAfterSuccessfulLogin() {
        loginPage.logout(); // Implement this method in your LoginPage object
    }

    @Then("User enter the credential from excel sheet")
    public void userEnterTheCredentialFromExcel() {
        String[] loginCredentials = excelUtils.readData("./DataSheet/RegDataUpdateCustomer.xlsx", "LoginSheet"); // Modify as needed
        loginPage.login(loginCredentials[0], loginCredentials[1]); // Assuming the array contains username and password
    }

    @Then("User click on the Update Customer link")
    public void userClickOnTheUpdateCustomerLink() {
        updateCustomerPage.clickUpdateCustomerLink(); // Implement this method in your UpdateCustomerPage object
    }

    @Then("User update the customer info from excel sheet")
    public void userUpdateTheCustomerInfoFromExcel() {
        String[] customerInfo = excelUtils.readData("./DataSheet/RegDataUpdateCustomer.xlsx", "CustomerInfoSheet"); // Modify as needed
        updateCustomerPage.updateCustomerInfo(customerInfo); // Implement this method in your UpdateCustomerPage object
    }

    @When("Click on the Submit Button")
    public void clickOnTheSubmitButton() {
        updateCustomerPage.submitUpdate(); // Implement this method in your UpdateCustomerPage object
    }

    @Then("User verify the customer info is updated")
    public void userVerifyTheCustomerInfoIsUpdated() {
        String updatedInfo = updateCustomerPage.getUpdatedCustomerInfo(); // Implement this method
        assertEquals("Expected Updated Info", updatedInfo); // Replace with actual expected updated info
    }
}
