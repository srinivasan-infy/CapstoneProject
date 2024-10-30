package com.infy.stepDefinitions;

import com.github.javafaker.Faker;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
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
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class MultpliteRegistrationWithFaker {
	private static final Logger logger = LoggerFactory.getLogger(RegistrationStepsFaker.class);
	private WebDriver driver;
    private RegistrationPage registrationPage;
    private LoginPage loginPage;
    private AccountServices accountServices;
	public Faker faker;
	private Map<String, User> registeredUsers;
	
	public MultpliteRegistrationWithFaker() {
		this.driver = DriverManager.getInstance().getDriver();
		this.faker = new Faker();
		this.registeredUsers = new HashMap<>();
		initializeObjects();
	}
     
    private void initializeObjects() {
    	registrationPage = new RegistrationPage(driver);
    	loginPage = new LoginPage(driver);
    	accountServices = new AccountServices(driver);
    }

	@When("User fills in the registration form with random data for {int} users")
    public void user_fills_in_the_registration_form_with_random_data(int numberOfUsers) {
        for (int i = 1; i <= numberOfUsers; i++) {
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

            logger.debug("Generated random user data for user {}: {} {} from {}, {}, {}", i, firstName, lastName, street, city, state);

            try {
                loginPage.clickRegisterLink();

                boolean registrationDataFilled = registrationPage.fillRegistrationForm(firstName, lastName, street, city, state, zipCode, phoneNumber, ssn, username, password);
                Assert.assertTrue(registrationDataFilled, "Failed to fill the registration form for user: " + username);
                ExtendReportScreenShot.logScreenshotInfoEachStep(driver, "Step passed: Filling out the registration form for user: " + username);

                // Submit the form and verify registration success
                registrationPage.clickRegisterButton();
                boolean isMessageDisplayed = registrationPage.isSuccessMessageDisplayed();
                Assert.assertTrue(isMessageDisplayed, "Registration failed for user: " + username);

                // Store successfully registered user in the map
                registeredUsers.put(username, new User(firstName, lastName, street, city, state, zipCode, phoneNumber, ssn, username, password));
                ExtendReportScreenShot.logScreenshotInfoEachStep(driver, "Step passed: Registration successful for user: " + username);

                accountServices.clickLogout();
                loginPage.clickRegisterLink();
                logger.info("Registration successful for user: {}", username);
            } catch (AssertionError | Exception e) {
                // Capture a failure screenshot
                String errorMessage = e.getMessage() != null ? e.getMessage() : "An error occurred";
                ExtendReportScreenShot.logScreenshotInfoEachStep(driver, "Step failed: " + errorMessage);
                logger.error("Error during registration for user {}: {}", username, errorMessage);
                throw e;  // Re-throw the exception to fail the step
            }
        }
    }

	@Then("save all successfully registered users data to a single JSON file")
    public void save_all_successfully_registered_users_data_to_json() {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try (FileWriter writer = new FileWriter("target/all_successful_registration_data.json")) {
            gson.toJson(registeredUsers, writer);
            logger.info("All successfully registered user data saved to target/all_successful_registration_data.json");
        } catch (IOException e) {
            logger.error("Failed to save all registration data to JSON", e);
        }
    }
}

