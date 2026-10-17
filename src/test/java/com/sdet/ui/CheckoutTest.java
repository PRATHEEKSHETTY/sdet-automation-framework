
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

    // ==========================================
    // 1. Positive Test - Successful Checkout
    // ==========================================

    @Test(groups = {"ui", "smoke"})
    public void successfulCheckoutTest() {

        LoginPage loginPage = new LoginPage(page);
        InventoryPage inventoryPage = new InventoryPage(page);
        CartPage cartPage = new CartPage(page);
        CheckoutPage checkoutPage = new CheckoutPage(page);

        // Login
        loginPage.login("standard_user", "secret_sauce");

        // Add product to cart
        inventoryPage.addBackpackToCart();
        inventoryPage.openCart();

        // Verify cart page
        Assert.assertTrue(
                page.url().contains("cart.html"),
                "User should navigate to the cart page"
        );

        // Verify cart product details
        Assert.assertEquals(
                cartPage.getProductName(),
                "Sauce Labs Backpack",
                "Cart should contain the correct product"
        );

        Assert.assertEquals(
                cartPage.getProductPrice(),
                "$29.99",
                "Backpack price should be $29.99"
        );

        Assert.assertEquals(
                cartPage.getProductQuantity(),
                "1",
                "Cart should contain one backpack"
        );

        // Proceed to checkout
        cartPage.clickCheckout();

        // Enter customer information
        checkoutPage.enterCustomerDetails(
                TestDataReader.get("checkout.firstname"),
                TestDataReader.get("checkout.lastname"),
                TestDataReader.get("checkout.postalcode")
        );

        checkoutPage.clickContinue();

        // Verify checkout overview
        Assert.assertTrue(
                checkoutPage.isCheckoutOverviewDisplayed(),
                "User should navigate to checkout overview"
        );

        Assert.assertEquals(
                checkoutPage.getOverviewProductName(),
                "Sauce Labs Backpack",
                "Checkout overview should display the correct product"
        );

        Assert.assertEquals(
                checkoutPage.getOverviewProductPrice(),
                "$29.99",
                "Checkout overview should display the correct price"
        );

        Assert.assertEquals(
                checkoutPage.getOverviewProductQuantity(),
                "1",
                "Checkout overview should display quantity 1"
        );

        // Complete checkout
        checkoutPage.clickFinish();

        // Verify order confirmation
        Assert.assertEquals(
                checkoutPage.getConfirmationMessage(),
                "Thank you for your order!",
                "Order confirmation message should be displayed"
        );
    }

    // ==========================================
    // 2. Negative Tests - Required Field Validation
    // ==========================================

    @DataProvider(name = "invalidCheckoutData")
    public Object[][] invalidCheckoutData() {

        String firstName =
                TestDataReader.get("checkout.firstname");

        String lastName =
                TestDataReader.get("checkout.lastname");

        String postalCode =
                TestDataReader.get("checkout.postalcode");

        return new Object[][] {
                {
                        "",
                        lastName,
                        postalCode,
                        "First Name is required"
                },
                {
                        firstName,
                        "",
                        postalCode,
                        "Last Name is required"
                },
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

        // Enter invalid customer information
        checkoutPage.enterCustomerDetails(
                firstName,
                lastName,
                postalCode
        );

        checkoutPage.clickContinue();

        // Verify checkout remains on step one
        Assert.assertTrue(
                page.url().contains("checkout-step-one.html"),
                "Checkout should remain on step one for invalid data"
        );

        // Verify validation error
        Assert.assertTrue(
                checkoutPage.getErrorMessage().contains(expectedError),
                "Expected validation error: " + expectedError
        );
    }

    // ==========================================
    // 3. Boundary Tests - Minimum Non-Empty Inputs
    // ==========================================

    @DataProvider(name = "boundaryCheckoutData")
    public Object[][] boundaryCheckoutData() {

        return new Object[][] {
                {"A", "Shetty", "576101"},
                {"Pratheek", "Shetty", "1"}
        };
    }

    @Test(
            dataProvider = "boundaryCheckoutData",
            groups = {"ui", "regression", "boundary"}
    )
    public void checkoutBoundaryTest(
            String firstName,
            String lastName,
            String postalCode) {

        LoginPage loginPage = new LoginPage(page);
        InventoryPage inventoryPage = new InventoryPage(page);
        CartPage cartPage = new CartPage(page);
        CheckoutPage checkoutPage = new CheckoutPage(page);

        // Login
        loginPage.login("standard_user", "secret_sauce");

        // Add product to cart
        inventoryPage.addBackpackToCart();
        inventoryPage.openCart();

        // Verify correct product
        Assert.assertEquals(
                cartPage.getProductName(),
                "Sauce Labs Backpack",
                "Cart should contain the correct product"
        );

        // Navigate to checkout
        cartPage.clickCheckout();

        // Enter boundary-value inputs
        checkoutPage.enterCustomerDetails(
                firstName,
                lastName,
                postalCode
        );

        checkoutPage.clickContinue();

        // Verify checkout proceeds successfully
        Assert.assertTrue(
                checkoutPage.isCheckoutOverviewDisplayed(),
                "Checkout should proceed for boundary input: "
                        + firstName + ", " + postalCode
        );
    }

    // ==========================================
    // Critical Business Scenario - Remove Product
    // ==========================================

    @Test(groups = {"ui", "regression"})
    public void removeProductFromCartTest() {

        LoginPage loginPage = new LoginPage(page);
        InventoryPage inventoryPage = new InventoryPage(page);
        CartPage cartPage = new CartPage(page);

        // Step 1: Login
        loginPage.login("standard_user", "secret_sauce");

        // Step 2: Add backpack to cart
        inventoryPage.addBackpackToCart();
        inventoryPage.openCart();

        // Step 3: Verify product was added
        Assert.assertEquals(
                cartPage.getProductName(),
                "Sauce Labs Backpack",
                "Cart should contain Sauce Labs Backpack"
        );

        Assert.assertFalse(
                cartPage.isCartEmpty(),
                "Cart should contain a product before removal"
        );

        // Step 4: Remove backpack from cart
        cartPage.removeBackpackFromCart();

        // Step 5: Verify cart is empty
        Assert.assertTrue(
                cartPage.isCartEmpty(),
                "Cart should be empty after removing the backpack"
        );
    }

}
