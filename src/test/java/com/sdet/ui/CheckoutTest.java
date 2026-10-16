
package com.sdet.ui;

import com.sdet.base.BaseTest;
import com.sdet.pages.LoginPage;
import com.sdet.pages.InventoryPage;
import com.sdet.pages.CartPage;
import com.sdet.pages.CheckoutPage;
import com.sdet.utils.TestDataReader;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class CheckoutTest extends BaseTest {

    @Test(groups = {"ui", "smoke"})
    public void successfulCheckoutTest() {

        LoginPage loginPage = new LoginPage(page);
        InventoryPage inventoryPage = new InventoryPage(page);
        CartPage cartPage = new CartPage(page);
        CheckoutPage checkoutPage = new CheckoutPage(page);

        loginPage.login("standard_user", "secret_sauce");

        inventoryPage.addBackpackToCart();
        inventoryPage.openCart();

        Assert.assertTrue(
            page.url().contains("cart.html"),
            "User should navigate to the cart page"
        );

        Assert.assertEquals(
            cartPage.getProductName(),
            "Sauce Labs Backpack"
        );

        cartPage.clickCheckout();

        checkoutPage.enterCustomerDetails(
            TestDataReader.get("checkout.firstname"),
            TestDataReader.get("checkout.lastname"),
            TestDataReader.get("checkout.postalcode")
        );

        checkoutPage.clickContinue();
        checkoutPage.clickFinish();

        Assert.assertEquals(
            checkoutPage.getConfirmationMessage(),
            "Thank you for your order!"
        );
    }

    // Test data for three negative checkout scenarios
    @DataProvider(name = "invalidCheckoutData")
    public Object[][] invalidCheckoutData() {

        String firstName =
            TestDataReader.get("checkout.firstname");

        String lastName =
            TestDataReader.get("checkout.lastname");

        String postalCode =
            TestDataReader.get("checkout.postalcode");

        return new Object[][] {

            // Missing first name
            {
                "",
                lastName,
                postalCode,
                "First Name is required"
            },

            // Missing last name
            {
                firstName,
                "",
                postalCode,
                "Last Name is required"
            },

            // Missing postal code
            {
                firstName,
                lastName,
                "",
                "Postal Code is required"
            }
        };
    }

    @Test(
        dataProvider = "invalidCheckoutData",
        groups = {"ui", "negative"}
    )
    public void checkoutValidationTest(
            String firstName,
            String lastName,
            String postalCode,
            String expectedError) {

        LoginPage loginPage = new LoginPage(page);
        InventoryPage inventoryPage = new InventoryPage(page);
        CartPage cartPage = new CartPage(page);
        CheckoutPage checkoutPage = new CheckoutPage(page);

        // Login
        loginPage.login("standard_user", "secret_sauce");

        // Add product to cart
        inventoryPage.addBackpackToCart();
        inventoryPage.openCart();

        Assert.assertTrue(
            page.url().contains("cart.html"),
            "User should navigate to the cart page"
        );

        // Navigate to checkout
        cartPage.clickCheckout();

        // Enter test data from DataProvider
        checkoutPage.enterCustomerDetails(
            firstName,
            lastName,
            postalCode
        );

        checkoutPage.clickContinue();

        // Verify user remains on checkout step one
        Assert.assertTrue(
            page.url().contains("checkout-step-one.html"),
            "Checkout should remain on step one for invalid data"
        );

        // Verify the correct validation error
        Assert.assertTrue(
            checkoutPage.getErrorMessage()
                .contains(expectedError),
            "Expected validation error: " + expectedError
        );
    }
}
