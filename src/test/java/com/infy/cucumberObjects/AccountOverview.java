package com.infy.cucumberObjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.infy.utility.StepDefinitionUtility;

public class AccountOverview {
    private WebDriver driver;
    private Map<String, String[]> accountData;
    WebDriverWait wait;

    @FindBy(xpath = "//*[@id='accountTable']") 
    private WebElement accountTable1;
    
    @FindBy(xpath = "//*[@id='accountTable']/tbody/tr")
    private WebElement accountTable;
    
    @FindBy(xpath = "//*[@id='accountTable']/tbody/tr")  
    public List<WebElement> accountTableBody;
    
    @FindBy(xpath = "//*[@id='accountTable']/thead/tr/th") 
    private List<WebElement> accountTableHeaders;
    
    @FindBy(xpath = "//*[@id='accountTable']/tfoot/tr") 
    private WebElement accountTableFooter;
    
	@FindBy(xpath = "//a[contains(text(),'Open New Account')]")
    WebElement openNewAccountLink;

    public AccountOverview(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        PageFactory.initElements(driver, this);
    }

    public boolean isAccountOverviewDisplayed() {
        return driver.getTitle().contains("Accounts Overview");
    }
    
    public void clickOpenNewAccount() {
    	wait.until(ExpectedConditions.elementToBeClickable(openNewAccountLink)).click();
    }
    
    public List<WebElement> getAccountTableBody() {
        return accountTableBody;
    }
   
    public boolean verifyTableHeadersInSequence() {
        String[] expectedHeaders = {"Account", "Balance*", "Available Amount"};
        for (int i = 0; i < expectedHeaders.length; i++) {
        	System.out.println("Header : "+ accountTableHeaders.get(i).getText() +"\n");
            if (!accountTableHeaders.get(i).getText().equals(expectedHeaders[i])) {
                return false; 
            }
        }
        return true; 
    }

    public boolean isAccountDetailsValid() {
        List<WebElement> rows = accountTableBody;
        for (WebElement row : rows) {
            List<WebElement> cells = row.findElements(By.tagName("td"));
            if (cells.size() >= 3) {
                String accountNumber = cells.get(0).getText();
                String balance = cells.get(1).getText();
                String availableBalance = cells.get(2).getText();

                if (accountNumber.length() == 5 && 
                    Double.parseDouble(balance) > 0 && 
                    Double.parseDouble(availableBalance) > 0) {
                    continue;
                }
                return false;
            }
        }
        return true;
    }
    
    public boolean isBalanceIncludesDepositsTextDisplayed() {
        WebElement lastRow = accountTableFooter.findElement(By.xpath(".//td"));  
        return lastRow.getText().contains("Balance includes deposits");
    }
    
    public boolean isTotalBalanceCorrect() {

        List<WebElement> rows = accountTableBody;

        if (rows.isEmpty()) {
            return false;
        }

        // Call the method to calculate sum and total
        double[] balances = StepDefinitionUtility.calculateBalances(rows);
        double sumOfBalances = balances[0];
        double totalBalance = balances[1];
        System.out.println("Balance  "+ sumOfBalances);
        // Return true if the sum of balances matches the total balance
        return sumOfBalances == totalBalance;
    }
       
 // Method to fetch data from the account overview web table
    public Map<String, String[]> fetchWebTableData() {
        accountData = new LinkedHashMap<>();
       
        WebElement tableBody = accountTable;
        // Stream the rows, filter ones with at least 3 cells, and process each row
        tableBody.findElements(By.tagName("tr"))
            .stream()
            .filter(row -> row.findElements(By.tagName("td")).size() >= 3)  
            .forEach(row -> {
                List<WebElement> cells = row.findElements(By.tagName("td"));

                String accountNumber = cells.get(0).getText().trim(); 
                String balance = cells.get(1).getText().trim();     
                String availableBalance = cells.get(2).getText().trim(); 

                accountData.put(accountNumber, new String[]{balance, availableBalance});  
            });
        accountData.forEach((key, value) -> System.out.println(key + " = " + Arrays.toString(value)));
        return accountData;  
    }

    public boolean verifyAccountNumberInTable(String accountNumber) {
    	return StepDefinitionUtility.checkAccountNumberInTable(accountTableBody, accountNumber );
    }

    public boolean verifyBalanceForAccount(String accountNumber, double expectedBalance) {
    	return StepDefinitionUtility.checkBalanceForAccount(accountTableBody, accountNumber, expectedBalance );
    }  
}