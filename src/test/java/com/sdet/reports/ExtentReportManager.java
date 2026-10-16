
package com.sdet.reports;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

public class ExtentReportManager {

    private static ExtentReports extent;

    public static synchronized ExtentReports getInstance() {

        if (extent == null) {

            ExtentSparkReporter reporter =
                new ExtentSparkReporter(
                    "target/extent-report.html"
                );

            reporter.config().setDocumentTitle(
                "SDET Automation Report"
            );

            reporter.config().setReportName(
                "UI and API Automation Results"
            );

            extent = new ExtentReports();
            extent.attachReporter(reporter);

            extent.setSystemInfo("Framework", "Playwright + REST Assured");
            extent.setSystemInfo("Language", "Java");
            extent.setSystemInfo("Test Runner", "TestNG");
        }

        return extent;
    }
}
