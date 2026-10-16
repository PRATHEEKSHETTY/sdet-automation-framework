
package com.sdet.ui;

import com.sdet.base.BaseTest;
import com.sdet.pages.LoginPage;
import com.sdet.pages.InventoryPage;
import com.sdet.pages.CartPage;
import com.sdet.pages.CheckoutPage;
import com.sdet.utils.TestDataReader;

import org.testng.Assert;
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

    @Test(groups = {"ui", "negative"})
    public void checkoutWithoutFirstNameTest() {

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

        cartPage.clickCheckout();

 String firstName =
    TestDataReader.get("checkout.invalid.firstname");

Assert.assertEquals(
    firstName,
    "",
    "Negative checkout test requires an empty first name"
);
        checkoutPage.enterCustomerDetails(
    firstName,
    TestDataReader.get("checkout.lastname"),
    TestDataReader.get("checkout.postalcode")
);

        checkoutPage.clickContinue();
 Assert.assertTrue(
    page.url().contains("checkout-step-one.html"),
    "Checkout should remain on step one when first name is missing"
);
        Assert.assertTrue(
            checkoutPage.getErrorMessage()
                .contains("First Name is required")
        );
    }
}
