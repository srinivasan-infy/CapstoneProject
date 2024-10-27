package com.infy.stepDefinitions;

import com.github.javafaker.Faker;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.infy.cucumberObjects.*;
import com.infy.driverFactory.DriverManager;
import com.infy.models.User;
import com.infy.utility.ExtendReportScreenShot;
import com.infy.utility.StepDefinitionUtility;
import io.cucumber.java.en.*;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

import java.io.FileWriter;
import java.io.IOException;

public class RegistrationStepsFaker extends BaseStep {
	private static final Logger logger = LoggerFactory.getLogger(RegistrationStepsFaker.class);
    private WebDriver driver;
    private Faker faker;
    public User user;

    public RegistrationStepsFaker() {
		this.driver = DriverManager.getInstance().getDriver();
        faker = new Faker(); // Initialize Faker
		logger.info("Initialized RegistrationSteps with WebDriver instance");
	}
   
    @Given("User is on the registration pages")
    public void user_is_on_the_registration_pages() {
        String url = "https://parabank.parasoft.com/parabank/index.htm";
        DriverManager.getInstance().getDriver().get(url);
        logger.info("Navigated to registration page: {}", url);
        ExtendReportScreenShot.logScreenshotInfoEachStep(driver, "Navigated to registration page");
    }

    @When("User fills in the registration form with random data")
    public void user_fills_in_the_registration_form_with_random_data() throws IOException {
        // Generate random user data using Faker
        String firstName = faker.name().firstName();
        String lastName = faker.name().lastName();
        String street = faker.address().streetAddress();
        String city = faker.address().city();
        String state = faker.address().state();
        String zipCode = faker.address().zipCode();
        String phoneNumber = faker.phoneNumber().cellPhone();
        String ssn = faker.idNumber().ssnValid();
        String username = firstName + "_" + lastName;
        String password = faker.internet().password();
        logger.debug("Generated random user data using Faker: {} {} from {}, {}, {}", firstName, lastName, street, city, state);

        try {
            loginPage.clickRegisterLink();
            ExtendReportScreenShot.logScreenshotInfoEachStep(driver, "Clicked on Register Link");

            boolean registrationDataFilled = registrationPage.fillRegistrationForm(firstName, lastName, street, city, state, zipCode, phoneNumber, ssn, username, password);
            Assert.assertTrue(registrationDataFilled, "Failed to fill the registration form");

            user = new User(firstName, lastName, street, city, state, zipCode, phoneNumber, ssn, username, password);
            ExtendReportScreenShot.logScreenshotInfoEachStep(driver, "Step passed: Filling out the registration form");

            // Save registration data to JSON file
            String jsonFilePath = "target/registration_data.json"; 
            StepDefinitionUtility.saveRegistrationData(user, jsonFilePath);
            logger.info("User registration data saved to JSON file: {}", jsonFilePath);
        } catch (Exception e) {
            ExtendReportScreenShot.logScreenshotInfoEachStep(driver, "Step failed: Filling out the registration form");
            logger.error("Error during filling registration form: {}", e.getMessage());
            Assert.fail("Registration form filling failed", e);
        }
    }
    
    @When("User submits the registration form")
    public void user_submits_the_registration_form() {
        try {
            registrationPage.clickRegisterButton();
            ExtendReportScreenShot.logScreenshotInfoEachStep(driver, "Clicked on Register Button");
            logger.info("Submitted the registration form");
        } catch (Exception e) {
            ExtendReportScreenShot.logScreenshotInfoEachStep(driver, "Step failed: Submitting the registration form");
            logger.error("Error during submitting registration form: {}", e.getMessage());
            Assert.fail("Registration form submission failed", e);
        }
    }

    @Then("save registration data to JSON")
    public void save_registration_data_to_json() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try (FileWriter writer = new FileWriter("target/registration_data.json")) {
            gson.toJson(user, writer);
            logger.info("Registration data saved to target/registration_data.json");
        } catch (IOException e) {
            logger.error("Failed to save registration data to JSON", e);
        }
    }
    

    @Then("User should see a confirmation message")
    public void user_should_see_confirmation_message() {
        try {
            boolean isMessageDisplayed = registrationPage.isSuccessMessageDisplayed();
            Assert.assertTrue(isMessageDisplayed, "The confirmation message should be displayed after registration.");
            ExtendReportScreenShot.logScreenshotInfoEachStep(driver, "Step passed: Confirmation message displayed successfully");
            logger.info("Confirmed that the registration success message is displayed.");
        } catch (AssertionError e) {
            ExtendReportScreenShot.logScreenshotInfoEachStep(driver, "Step failed: Confirmation message not displayed");
            logger.error("Confirmation message not displayed: {}", e.getMessage());
            Assert.fail("Confirmation message check failed", e);
        }
    }
    
}