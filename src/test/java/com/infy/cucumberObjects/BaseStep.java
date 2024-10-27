package com.infy.cucumberObjects;

import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.infy.driverFactory.DriverManager;


public abstract class BaseStep {
	protected WebDriver driver;
    protected LoginPage loginPage;
    protected AccountOverview accountOverview;
    protected AccountServices accountServices;
    protected BillPay billPay;
    protected FindTransaction findTransaction;
    protected OpenNewAccount openNewAccount;
    protected RegistrationPage registrationPage;
    protected RequestLoan requestLoan;
    protected TransferFunds transferFunds;
    protected UpdateContactInfo updateContactInfo;
    protected Logout logout;
    
    private static final Logger logger = LoggerFactory.getLogger(BaseStep.class);

    public BaseStep() {
        this.driver = DriverManager.getInstance().getDriver();
        initializeObjects();
    }

    protected void initializeObjects() {
        loginPage = new LoginPage(driver);
        accountOverview = new AccountOverview(driver);
        logout = new Logout(driver);
        updateContactInfo =  new UpdateContactInfo(driver);
        transferFunds = new TransferFunds(driver);
        requestLoan = new RequestLoan(driver);
        registrationPage = new RegistrationPage(driver);
        openNewAccount = new OpenNewAccount(driver);
        findTransaction = new FindTransaction(driver);
        billPay = new BillPay(driver);
        requestLoan = new RequestLoan(driver);
        accountServices = new AccountServices(driver);
        logger.info("Page objects initialized: LoginPage and Logout");
    }
}