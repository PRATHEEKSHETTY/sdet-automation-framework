
package com.sdet.base;

import com.microsoft.playwright.*;
import com.sdet.config.ConfigReader;
import com.sdet.reports.ExtentTestListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class BaseTest {

    private static final Logger logger =
            LoggerFactory.getLogger(BaseTest.class);

    protected Playwright playwright;
    protected Browser browser;
    protected BrowserContext context;
    protected Page page;

    @BeforeMethod(alwaysRun = true)
    public void setUp() {

        logger.info("Starting UI test");

        playwright = Playwright.create();

        browser = playwright.chromium().launch(
            new BrowserType.LaunchOptions()
                .setHeadless(
                    Boolean.parseBoolean(
                        ConfigReader.get("headless")
                    )
                )
        );

        context = browser.newContext();
        page = context.newPage();

        logger.info("Navigating to SauceDemo");

        page.navigate(
            ConfigReader.get("ui.base.url")
        );
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown(ITestResult result) {

        try {

            if (result.getStatus() == ITestResult.FAILURE
                    && page != null) {

                logger.error(
                    "Test failed: {}",
                    result.getName(),
                    result.getThrowable()
                );

                // Create screenshot directory
                Path screenshotDirectory = Paths.get(
                    "target", "screenshots"
                );

                Files.createDirectories(screenshotDirectory);

                // Generate unique screenshot filename
                String fileName = result.getName()
                    + "_"
                    + System.currentTimeMillis()
                    + ".png";

                Path screenshotPath =
                    screenshotDirectory.resolve(fileName);

                // Capture screenshot
                page.screenshot(
                    new Page.ScreenshotOptions()
                        .setPath(screenshotPath)
                        .setFullPage(true)
                );

                logger.info(
                    "Screenshot saved: {}",
                    screenshotPath
                );

                // Attach screenshot to Extent HTML report
                ExtentTestListener.attachScreenshot(
                    "screenshots/" + fileName
                );
            }

        } catch (Exception e) {

            logger.error(
                "Failed to capture or attach screenshot",
                e
            );

        } finally {

            logger.info("Closing browser resources");

            try {

                if (context != null) {
                    context.close();
                }

                if (browser != null) {
                    browser.close();
                }

                if (playwright != null) {
                    playwright.close();
                }

            } finally {

                // Clear reporting reference after screenshot attachment
                ExtentTestListener.clearCurrentTest();

                logger.info("UI test cleanup completed");
            }
        }
    }
}
