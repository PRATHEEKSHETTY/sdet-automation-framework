
package com.sdet.ui;

import com.sdet.base.BaseTest;
import com.sdet.pages.LoginPage;
import org.testng.Assert;
import org.testng.annotations.Test;
import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class LoginTest extends BaseTest {

    @Test(groups = {"ui", "smoke"})
    public void validLoginTest() {

        LoginPage loginPage = new LoginPage(page);

        loginPage.login("standard_user", "secret_sauce");

        assertThat(page).hasURL("https://www.saucedemo.com/inventory.html");
        Assert.assertTrue(
        page.url().contains("inventory.html"),
        "User should navigate to the inventory page"
);

//For screen Shot verification 
// Assert.assertTrue(
//     page.url().contains("wrong-page.html"),
//     "Intentional failure to verify screenshot attachment"
// );
    }

    @Test(groups = {"ui", "negative"})
    public void invalidLoginTest() {

        LoginPage loginPage = new LoginPage(page);

        loginPage.login("invalid_user", "wrong_password");

        String errorMessage = loginPage.getErrorMessage();

        Assert.assertTrue(
            errorMessage.contains(
                "Username and password do not match"
            ),
            "Expected error message for invalid credentials"
        );
    }
}
