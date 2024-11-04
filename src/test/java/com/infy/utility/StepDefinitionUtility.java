package com.infy.utility;

import org.openqa.selenium.By;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import com.infy.cucumberObjects.*;
import com.infy.driverFactory.DriverManager;
import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import com.infy.models.User;

public class StepDefinitionUtility{
	
	WebDriverWait wait;

	public static void verifyAccountAndBalance(AccountOverview accountOverview, String accountNumber, double expectedBalance) {
	    boolean accountNumberFound = accountOverview.verifyAccountNumberInTable(accountNumber);
	    Assert.assertTrue(accountNumberFound, "Account number is not displayed in the table.");

	    boolean balanceMatches = accountOverview.verifyBalanceForAccount(accountNumber, expectedBalance);
	    Assert.assertTrue(balanceMatches, "Balance does not match for the account number.");
	    System.out.println("Account number and balance verification successful for account: " + accountNumber);
	}

	public static void verifyDeductionFromAccount(AccountOverview accountOverview, String deductedFromAccount, double deductionAmount) {
	    // Similar logic as before, just passing the accountOverview object
	    System.out.println("Deducted Account Number: " + deductedFromAccount);

	    boolean accountNumberFound = accountOverview.verifyAccountNumberInTable(deductedFromAccount);
	    Assert.assertTrue(accountNumberFound, "Deducted account number is not displayed in the table.");
	}
	
	public static boolean checkAccountNumberInTable(List<WebElement> element, String accountNo) {
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        // Find all rows in the table
        List<WebElement> rows = element;

        // Iterate over each row and check the first column for the account number
        for (WebElement row : rows) {
            List<WebElement> cells = row.findElements(By.tagName("td"));
            if (cells.size() > 0) {  // Ensure there's at least one cell in the row
                String accountNumberInTable = cells.get(0).getText(); 
                if (accountNumberInTable.equals(accountNo)) {
                    return true;  
                }
            }
        }
        return false;  		
	}
	
	public static void saveRegistrationData(User userData, String filePath) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.writeValue(new File(filePath), userData);
    }

	public static boolean checkBalanceForAccount(List<WebElement> element, String accountNo, double expectedBalance) {
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
       	// Find all rows in the table
        List<WebElement> rows = element;

        // Iterate over each row and check the first column for the account number
        for (WebElement row : rows) {
            List<WebElement> cells = row.findElements(By.tagName("td"));
            if (cells.size() >= 2) {  // Ensure there's at least two cells in the row
                String accountNumberInTable = cells.get(0).getText();  // First column (Account Number)
                String balanceInTable = cells.get(1).getText();        // Second column (Balance)
                // Remove $ and commas for parsing
                balanceInTable = balanceInTable.replace("$", "").replace(",", "");
                double actualBalance = Double.parseDouble(balanceInTable);
                // Check if the account number matches
                if (accountNumberInTable.equals(accountNo)) {
                    // Verify if the balance matches
                    return (actualBalance == expectedBalance);
                }
            }
        }
        return false;  // Balance not found or does not match
	}
	
	public static double[] calculateBalances(List<WebElement> rows) {
	    double sumOfBalances = 0.0;
	    double totalBalance = 0.0;

	    // Loop through the rows to calculate both the sum and total balance
	    for (int i = 0; i < rows.size(); i++) {
	        List<WebElement> cells = rows.get(i).findElements(By.tagName("td"));
	        if (cells.size() > 1) {
	            // Balance is present in 2nd column
	            String balanceText = cells.get(1).getText().trim();

	            // Remove $ and commas for parsing
	            balanceText = balanceText.replace("$", "").replace(",", "");

	            // If it's the last row, treat it as the total balance
	            if (i == rows.size() - 1) {
	                totalBalance = Double.parseDouble(balanceText);
	            } else {
	                // Sum balances for all other rows
	                sumOfBalances += Double.parseDouble(balanceText);
	            }
	        }
	    }

	    // Return both sum and total in an array
	    return new double[]{sumOfBalances, totalBalance};
	}
	
	public static void processUserRegistration(RegistrationPage registrationPage,Logout logout, LoginPage loginPage, Map<String, String> userDetails) {
		// Enter user details and submit form
		registrationPage.enterUserDetails(userDetails);
		registrationPage.clickRegisterButton();

		// Check if registration is successful
		if (registrationPage.isSuccessMessageDisplayed()) {
			String successMessage = registrationPage.verifyRegistrationSuccess();
			userDetails.put("Status", "Success");
			System.out.println("Text from the success element: " + successMessage);
			logout.clickLogoutLink();
		} else {
			// Handle column data if needed
			int columnIndex = 2;
			String columnData = registrationPage.getColumnData(columnIndex);
			userDetails.put("Status", columnData);
		}

		// Reset for next registration
		loginPage.clickRegisterLink();
	}

	public static void handleException(Map<String, String> userDetails, String errorMessage, Exception e) {
		userDetails.put("Status", errorMessage);
		e.printStackTrace();
	}
	
	
	public static String fetchRandomCellData(List<WebElement> tableLocator) {

		// Set up an explicit wait for rows to be visible
	    WebDriverWait wait = new WebDriverWait(DriverManager.getInstance().getDriver(), Duration.ofSeconds(10)); // 10-second timeout
	    wait.until(ExpectedConditions.visibilityOfAllElements(tableLocator)); // Wait until all
	    
		// Ensure the tableLocator is not empty and has more than one row
	    if (tableLocator == null || tableLocator.size() < 2) {
	        throw new IllegalArgumentException("The table has insufficient rows to ignore the last one.");
	    }
   
	    // Find all the rows in the table, excluding the last row
	    List<WebElement> rows = tableLocator;

	    // Generate a random row index, excluding the last row
	    Random random = new Random();
	    int randomRowIndex = random.nextInt(rows.size() - 1); // Ignore the last row

	    // Fetch the random row
	    WebElement randomRow = rows.get(randomRowIndex);

	    // Find all cells (td) in the random row
	    List<WebElement> cells = randomRow.findElements(By.tagName("td"));

	    // Ensure the row contains cells
	    if (cells.isEmpty()) {
	        throw new IllegalArgumentException("The selected row has no cells.");
	    }

	    // Fetch the random cell
	    WebElement account = cells.get(0);

	    // Return the text content of the random cell
	    return account.getText();
    }
	
	// Method to fetch data from the account overview web table
    public static Map<String, String[]> fetchWebTableData(List<WebElement> tableElement) {
    	Map<String, String[]> accountData = new LinkedHashMap<>();

       // Wait for the rows in the table to be present
       // wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("//*[@id='accountTable']/tbody/tr")));

    	// Stream the rows, filter ones with at least 3 cells, and process each row
        tableElement
            .stream()
            .filter(row -> row.findElements(By.tagName("td")).size() >= 3)  // Filter out rows with less than 3 cells
            .forEach(row -> {
                List<WebElement> cells = row.findElements(By.tagName("td"));

                String accountNumber = cells.get(0).getText().trim();  // First column (account number)
                String balance = cells.get(1).getText().trim();        // Second column (balance)
                String availableBalance = cells.get(2).getText().trim(); // Third column (available balance)

                accountData.put(accountNumber, new String[]{balance, availableBalance});  // Store in map
            });
        accountData.forEach((key, value) -> System.out.println(key + " = " + Arrays.toString(value)));
        return accountData;  // Return the populated map
    }
}