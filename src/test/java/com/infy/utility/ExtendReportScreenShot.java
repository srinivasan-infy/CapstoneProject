package com.infy.utility;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;

import com.aventstack.extentreports.MediaEntityBuilder;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.util.List;

public class ExtendReportScreenShot {

	public static String captureScreenshotAsBase64(WebDriver driver) {
		return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BASE64);
	}

	public static void logScreenshotInfo(WebDriver driver, ExtentTest test, String message) {
		String base64Screenshot = ExtendReportScreenShot.captureScreenshotAsBase64(driver);
		if (message.contains("Step failed: ")) {
			ExtentReportSetup.getTest().fail(message,
					MediaEntityBuilder.createScreenCaptureFromBase64String(base64Screenshot).build());
		} else {
			ExtentReportSetup.getTest().pass(message,
					MediaEntityBuilder.createScreenCaptureFromBase64String(base64Screenshot).build());
		}

	}

	// Method for logging a single screenshot with a message
	public static void logScreenshotInfoEachStep(WebDriver driver, String message) {
		String base64Screenshot = captureScreenshotAsBase64(driver);
		if (message.contains("Step failed: ")) {
			ExtentReportSetup.getTest().fail(message,
					MediaEntityBuilder.createScreenCaptureFromBase64String(base64Screenshot).build());
		} else {
			ExtentReportSetup.getTest().pass(message,
					MediaEntityBuilder.createScreenCaptureFromBase64String(base64Screenshot).build());
		}
	}

	public static void logMultipleScreenshots(WebDriver driver, List<String> messages) {
		for (String message : messages) {
			String base64Screenshot = captureScreenshotAsBase64(driver);
			if (message.contains("Step failed: ")) {
				ExtentReportSetup.getTest().fail(message,
						MediaEntityBuilder.createScreenCaptureFromBase64String(base64Screenshot).build());
			} else {
				ExtentReportSetup.getTest().pass(message,
						MediaEntityBuilder.createScreenCaptureFromBase64String(base64Screenshot).build());
			}
		}
	}
}

/*
 * public class ExtendReportScreenShot {
 * 
 * public static String captureScreenshotAsBase64(WebDriver driver) { return
 * ((TakesScreenshot) driver).getScreenshotAs(OutputType.BASE64); }
 * 
 * public static void logScreenshotInfo(WebDriver driver, ExtentTest test,
 * String message) { String base64Screenshot =
 * ExtendReportScreenShot.captureScreenshotAsBase64(driver); if
 * (message.contains("Step failed: ")) {
 * ExtentReportSetup.getTest().fail(message,
 * MediaEntityBuilder.createScreenCaptureFromBase64String(base64Screenshot).
 * build()); } else { ExtentReportSetup.getTest().pass(message,
 * MediaEntityBuilder.createScreenCaptureFromBase64String(base64Screenshot).
 * build()); }
 * 
 * }
 * 
 * }
 */
