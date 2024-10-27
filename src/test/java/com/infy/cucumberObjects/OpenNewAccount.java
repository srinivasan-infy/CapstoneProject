package com.infy.cucumberObjects;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.infy.driverFactory.DriverManager;

public class OpenNewAccount {
	private WebDriver driver;
	WebDriverWait wait;
	
	@FindBy(xpath = "//select[@id='type']")
	WebElement accountTypeDropdown;
	
	@FindBy(xpath = "//select[@id='fromAccountId']")
	WebElement fromAccountID;
	
	@FindBy(xpath = "//input[@value='Open New Account']")
	WebElement openNewAccountButton;
	
	@FindBy(xpath = "//div[@id='openAccountForm']//p[2]")
	WebElement minimumDepositText;
	
	@FindBy(xpath = "//h1[contains(text(), 'Account Opened!')]")
	WebElement accountOpened;
	
	@FindBy(xpath = "//p[contains(text(), 'Congratulations, your account is now open.')]")
	WebElement congratulations;
	
	@FindBy(xpath = "//b[contains(text(), 'Your new account number:')]")
	WebElement newAccountNumber;
	
	@FindBy(xpath="//*[@id='newAccountId']")
	WebElement newAccountID;
	
	
	public OpenNewAccount(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
		this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
	}
	
	public WebElement getAccountOpened() {
        return accountOpened;
    }

    public WebElement getOpenAccountButton() {
        return openNewAccountButton;
    }
		
	public void clickOpenAccount() {
		openNewAccountButton.click();
    }
	
	public void selectAccountType(String accountType) {
		// Create a Select object to interact with the dropdown
		wait.until(ExpectedConditions.visibilityOf(accountTypeDropdown));
	    Select select = new Select(accountTypeDropdown);
	    // Select the desired option (either "Saving" or "Checking")
	    select.selectByVisibleText(accountType); // Use the text to select
	}
	
	public double fetchMinimumDeposit() {
        String text = minimumDepositText.getText();
        // Regex pattern to match the minimum deposit amount
        Pattern pattern = Pattern.compile("\\$([\\d,.]+)");
        Matcher matcher = pattern.matcher(text);
        
        if (matcher.find()) {
            String amount = matcher.group(1).replace(",", "");  // Remove commas for parsing
            return Double.parseDouble(amount);
        } else {
            throw new RuntimeException("Minimum deposit amount not found in the text.");
        }
    }
	
	public String selectAccountNumber() {
		// Create a Select object to interact with the dropdown
	    Select accountID = new Select(fromAccountID);
	    
	    // Get all options from the dropdown
	    List<WebElement> options = accountID.getOptions();
	    
	    // Check if options list is empty
	    if (options.isEmpty()) {
	        throw new IllegalStateException("No account numbers available in the dropdown.");
	    }
	    
	    // Generate a random index
	    Random random = new Random();
	    int randomIndex = random.nextInt(options.size());
	    
	    // Select the account number random
	    accountID.selectByIndex(randomIndex);
	    String accountNumber = options.get(randomIndex).getText();
		return accountNumber;	    
	}
	
	public WebElement getAccountDropdown(){
		return accountTypeDropdown;
	}

    public boolean isAccountOpened() {
        return accountOpened.isDisplayed();
    }
    
    public boolean isSuccessMessage() {
        return congratulations.isDisplayed();
    }
    
    public boolean isAccountNumberText() {
        return newAccountNumber.isDisplayed();
    }

	public String getNewAccountNumber() {
		return newAccountID.getText();
	}	
	

}
