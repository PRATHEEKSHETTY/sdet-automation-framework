
package com.sdet.reports;

import com.aventstack.extentreports.ExtentTest;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class ExtentTestListener implements ITestListener {

    private static final ThreadLocal<ExtentTest> currentTest =
        new ThreadLocal<>();

    @Override
    public void onTestStart(ITestResult result) {

        ExtentTest test;

        synchronized (ExtentReportManager.getInstance()) {
            test = ExtentReportManager.getInstance()
                .createTest(
                    result.getTestClass().getName()
                    + "."
                    + result.getMethod().getMethodName()
                );
        }

        currentTest.set(test);
    }

    @Override
    public void onTestSuccess(ITestResult result) {

        ExtentTest test = currentTest.get();

        if (test != null) {
            test.pass("Test passed successfully");
        }

        currentTest.remove();
    }

    
@Override
public void onTestFailure(ITestResult result) {

    ExtentTest test = currentTest.get();

    if (test != null) {
        test.fail(result.getThrowable());
    }

    // Do not remove currentTest here.
    // BaseTest still needs it to attach the screenshot.

        currentTest.remove();
    }

    @Override
    public void onTestSkipped(ITestResult result) {

        ExtentTest test = currentTest.get();

        if (test != null) {
            test.skip("Test skipped");
        }

        currentTest.remove();
    }

    @Override
    public void onFinish(ITestContext context) {

        ExtentReportManager.getInstance().flush();
    }
public static void attachScreenshot(String screenshotPath) {

    ExtentTest test = currentTest.get();

    if (test != null) {
        try {
            test.addScreenCaptureFromPath(screenshotPath);
        } catch (Exception e) {
            test.warning("Screenshot attachment failed: " + e.getMessage());
        }
    }
}
}
