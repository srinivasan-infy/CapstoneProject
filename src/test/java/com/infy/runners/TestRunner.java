package com.infy.runners;

import java.io.IOException;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.DataProvider;
import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import com.infy.utility.ExtentReportSetup;

@CucumberOptions(
	    features = "src/test/resources/features",
	    glue = {"com.infy.stepDefinitions"},
	    plugin = {"pretty", "html:target/cucumber-html-report","json:target/cucumber.json"},
	    tags = "@Regression or @Smoke"
	)

public class TestRunner extends AbstractTestNGCucumberTests {
	
	@Override
    @DataProvider(parallel = true)
    public Object[][] scenarios() {
        return super.scenarios();
    }
	
	@BeforeClass(alwaysRun = true)
	@Parameters("browser")
    public void setUpClass(String browser) throws IOException {
        System.setProperty("browser", browser);
        ExtentReportSetup.init();
    }

    @AfterClass(alwaysRun = true)
    public void tearDownClass() {
    	ExtentReportSetup.getExtent().flush();
    }
}
