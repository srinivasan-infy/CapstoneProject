package com.infy.cucumberObjects;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class RegistrationPage {

	private WebDriver driver;
	WebDriverWait wait;
	
	@FindBy(name = "customer.firstName")
	private WebElement firstName;
	
	@FindBy(name = "customer.lastName")
	private WebElement lastName;
	
	@FindBy(name = "customer.address.street")
	private WebElement street;
	
	@FindBy(name = "customer.address.city")
	private WebElement city;
	
	@FindBy(name = "customer.address.state")
	private WebElement state;
	
	@FindBy(name = "customer.address.zipCode")
	private WebElement zipCode;
	
	@FindBy(name = "customer.phoneNumber")
	private WebElement phoneNumber;
	
	@FindBy(name = "customer.ssn")
	private WebElement ssn;
	
	@FindBy(name = "customer.username")
	private WebElement userName;
	
	@FindBy(name = "customer.password")
	private WebElement password;
	
	@FindBy(name = "repeatedPassword")
	private WebElement confirmPassword;
	
	@FindBy(xpath = "//input[@value='Register']")
	private WebElement registerButton;
	
	@FindBy(xpath = "//h1[contains(text(),'Signing up is easy!')]")
	private WebElement singUpHeading;
	
	@FindBy(xpath = "//p[contains(text(),'Your account was created successfully. You are now logged in.')]")
	private WebElement registrationSuccess;
	
	@FindBy(xpath = "//*[@id=\"customerForm\"]/table")
	private WebElement registrationTable;

	private LoginPage loginPage;
    private Logout logout;

    // Constructor Injection (preferred way)
    public RegistrationPage(LoginPage loginPage, Logout logout) {
        this.loginPage = loginPage;
        this.logout = logout;
    }
		
	public RegistrationPage(WebDriver driver) {
		this.driver = driver;
		this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		PageFactory.initElements(driver, this);
	}
	
	
	public void enterUserDetails(Map<String, String> userDetails) {
		firstName.sendKeys(userDetails.get("FirstName"));
        lastName.sendKeys(userDetails.get("LastName"));
        street.sendKeys(userDetails.get("Address"));
        city.sendKeys(userDetails.get("City"));
        state.sendKeys(userDetails.get("State"));
        zipCode.sendKeys(userDetails.get("Zipcode"));
        phoneNumber.sendKeys(userDetails.get("Phone"));
        ssn.sendKeys(userDetails.get("SSN"));
        userName.sendKeys(userDetails.get("UserName"));
        password.sendKeys(userDetails.get("Password"));
        confirmPassword.sendKeys(userDetails.get("ConfirmPassword"));
    }
	
	// Method to fill the registration form
	public boolean fillRegistrationForm(String firstNameInput, String lastNameInput, String streetInput,
			String cityInput, String stateInput, String zipCodeInput, String phoneNumberInput, String ssnInput,
			String usernameInput, String passwordInput) {
		WebElement[] elementsToCheck = { firstName, lastName, street, city, state, zipCode, phoneNumber, ssn, userName, password, confirmPassword };

		for (WebElement element : elementsToCheck) {
			if (element == null || !isElementPresent(element)) {
				return false;
			}
		}

		firstName.sendKeys(firstNameInput);
		lastName.sendKeys(lastNameInput);
		street.sendKeys(streetInput);
		city.sendKeys(cityInput);
		state.sendKeys(stateInput);
		zipCode.sendKeys(zipCodeInput);
		phoneNumber.sendKeys(phoneNumberInput);
		ssn.sendKeys(ssnInput);
		userName.sendKeys(usernameInput);
		password.sendKeys(passwordInput);
		confirmPassword.sendKeys(passwordInput); 

		return true; 
	}

	private boolean isElementPresent(WebElement element) {
		try {
			wait.until(ExpectedConditions.visibilityOf(element));
			return true; 
		} catch (Exception e) {
			return false; 
		}
	}

    public void clickRegisterButton() {
        registerButton.click();
    }
    
    public String verifyRegistrationPage() {
    	return singUpHeading.getText();
    }
    
    public String verifyRegistrationSuccess() {
    	return registrationSuccess.getText();
    }
      
    public boolean isSuccessMessageDisplayed() {
        try {
            return registrationSuccess.isDisplayed(); 
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    public void processUserRegistration(Map<String, String> userDetails) {
        enterUserDetails(userDetails);
        clickRegisterButton();

        if (isSuccessMessageDisplayed()) {
            String successMessage = verifyRegistrationSuccess();
            userDetails.put("Status", "Success");
            System.out.println("Text from the success element: " + successMessage);
            logout.clickLogoutLink();
        } else {
            int columnIndex = 2;
            String columnData = getColumnData(columnIndex);
            userDetails.put("Status", columnData);
        }
        
        loginPage.clickRegisterLink();
    }

    public void handleException(Map<String, String> userDetails, String errorMessage, Exception e) {
        userDetails.put("Status", errorMessage);
        e.printStackTrace();
    }
    
    public String getColumnData(int columnIndex) {
        StringBuilder columnData = new StringBuilder();

        // Get all rows from the table
        List<WebElement> rows = registrationTable.findElements(By.tagName("tr"));
        
        for (WebElement row : rows) {
            List<WebElement> cells = row.findElements(By.tagName("td"));
            if (cells.size() > columnIndex) {
                String cellData = cells.get(columnIndex).getText();
                columnData.append(cellData).append("\n"); 
            }
        }
        return columnData.toString().trim(); 
    }
       
}