
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

    if (test == null) {
        System.err.println(
            "ERROR: ExtentTest reference is null. "
            + "Screenshot not attached: " + screenshotPath
        );
        return;
    }

    try {
        test.addScreenCaptureFromPath(screenshotPath);

        System.out.println(
            "SUCCESS: Screenshot attachment registered: "
            + screenshotPath
        );

    } catch (Exception e) {
        System.err.println(
            "ERROR: Screenshot attachment failed: "
            + e.getMessage()
        );
        e.printStackTrace();
    }
}
public static void clearCurrentTest() {
    currentTest.remove();
}
}
