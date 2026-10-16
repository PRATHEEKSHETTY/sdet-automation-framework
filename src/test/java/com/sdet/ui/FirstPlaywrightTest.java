
package com.sdet.ui;

import com.microsoft.playwright.*;
import org.testng.Assert;
import org.testng.annotations.Test;

public class FirstPlaywrightTest {

    @Test
    public void verifySauceDemoPageTitle() {

        try (Playwright playwright = Playwright.create()) {

            Browser browser = playwright.chromium().launch(
                new BrowserType.LaunchOptions()
                    .setHeadless(true)
            );

            Page page = browser.newPage();

            page.navigate("https://www.saucedemo.com/");

            String actualTitle = page.title();

            System.out.println("Page title: " + actualTitle);

            Assert.assertEquals(actualTitle, "Swag Labs");

            browser.close();
        }
    }
}
