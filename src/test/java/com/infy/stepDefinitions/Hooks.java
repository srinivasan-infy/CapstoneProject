package com.infy.stepDefinitions;

import com.infy.driverFactory.DriverManager;

import io.cucumber.java.*;
import io.cucumber.plugin.event.PickleStepTestStep;
import com.aventstack.extentreports.ExtentTest;
import org.openqa.selenium.WebDriver;
import java.lang.reflect.Field;
import java.util.List;
import java.util.stream.Collectors;
import io.cucumber.core.backend.TestCaseState;
import io.cucumber.plugin.event.TestCase;
import java.util.Properties;
import com.infy.utility.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Hooks {
	private static final Logger logger = LoggerFactory.getLogger(Hooks.class);
    private WebDriver driver;
    private ExtentTest test;
    private Properties config;
    int count = 0;

    @Before
    public void setUp(Scenario scenario) {
    	// Load config.properties using ConfigLoader
    	ConfigLoaderUtility.setPropertiesFilePath("./src/main/resources/AutomationConfig.properties");
        config = ConfigLoaderUtility.loadProperties();
        driver = DriverManager.getInstance().getDriver();
        //ExtentReportSetup.createTest(scenario.getName());
  
        String testName = scenario.getName();
        String testType = scenario.getSourceTagNames().stream()
                .findFirst()
                .orElse("General");
        String author = "Srinivasan"; 
        ExtentReportSetup.createTest(testName, testType, author);
    }

    @BeforeStep
    public void getStepName(Scenario scenario) {
    }
     
    @AfterStep
    public void captureScreenshotIfStepFails(Scenario scenario) {
    	if (scenario.isFailed()) {
        	ExtendReportScreenShot.logScreenshotInfo(driver, test, "Step failed: " + getCurrentStepName(scenario) );
        } else {
        	ExtendReportScreenShot.logScreenshotInfo(driver, test, "Step passed: " + getCurrentStepName(scenario) );
        }
    	count++;
        ExtentReportSetup.getExtent().flush();
    }
    
    @After
    public void tearDown(Scenario scenario) {
    	ExtentReportSetup.removeTest();
    	DriverManager.getInstance().quitDriver();
    	count = 0;
    }
    
    private String getCurrentStepName(Scenario scenario) {
        try {
            Field f = scenario.getClass().getDeclaredField("delegate");
            f.setAccessible(true);
            TestCaseState sc = (TestCaseState) f.get(scenario);
            Field f1 = sc.getClass().getDeclaredField("testCase");
            f1.setAccessible(true);
            TestCase testCase = (TestCase) f1.get(sc);

            List<PickleStepTestStep> testSteps = testCase.getTestSteps().stream()
                    .filter(x -> x instanceof PickleStepTestStep)
                    .map(x -> (PickleStepTestStep) x)
                    .collect(Collectors.toList());

            if (count < testSteps.size()) {
                PickleStepTestStep currentStep = testSteps.get(count);
                return currentStep.getStep().getText();
            } else {
                return "Unknown Step";
            }
        } catch (Exception e) {
            logger.error("Error retrieving step name", e);
            return "Error retrieving step name";
        }
    } 
}