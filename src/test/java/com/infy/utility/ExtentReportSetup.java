package com.infy.utility;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

public class ExtentReportSetup {
    private static ExtentReports extent;
    private static ThreadLocal<ExtentTest> test = new ThreadLocal<>();
    
    public static void init() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String reportPath = "target/extent-reports/" + timeStamp + "/ExtentReport.html";

        ExtentSparkReporter extentSpark = new ExtentSparkReporter(reportPath);
        extentSpark.loadXMLConfig("src/test/resources/extentConfig/extentconfig.xml");
    	
    	extent = new ExtentReports();
        extent.attachReporter(extentSpark);
        
        // Adding environment info for better report detail
        extent.setSystemInfo("Project Name", "Capstone Project");
        extent.setSystemInfo("Environment", "QA");
        extent.setSystemInfo("Tester", "Automation Team");
    }

    public static ExtentReports getExtent() {
        return extent;
    }  
    
    public static ExtentTest getTest() {
        return test.get();
    }
    
    public static void createTest(String testName, String category, String author) {
        test.set(extent.createTest(testName).assignCategory(category).assignAuthor(author));
    }

    public static void removeTest() {
        test.remove();
    }

    public static void flush() {
        if (extent != null) {
            extent.flush();
        }
    }

}
