
package com.sdet.pages;

import com.microsoft.playwright.Page;

public class CheckoutPage {

    private final Page page;

    public CheckoutPage(Page page) {
        this.page = page;
    }

    public void enterCustomerDetails(
            String firstName,
            String lastName,
            String postalCode) {

        page.locator("[data-test='firstName']").fill(firstName);
        page.locator("[data-test='lastName']").fill(lastName);
        page.locator("[data-test='postalCode']").fill(postalCode);
    }

    public void clickContinue() {
        page.locator("[data-test='continue']").click();
    }

    // Checkout overview validation methods

    public boolean isCheckoutOverviewDisplayed() {
        return page.url().contains("checkout-step-two.html");
    }

    public String getOverviewProductName() {
        return page.locator(
            "[data-test='inventory-item-name']"
        ).innerText();
    }

    public String getOverviewProductPrice() {
        return page.locator(
            "[data-test='inventory-item-price']"
        ).innerText();
    }

    public String getOverviewProductQuantity() {
        return page.locator(
            "[data-test='item-quantity']"
        ).innerText();
    }

    public void clickFinish() {
        page.locator("[data-test='finish']").click();
    }

    public String getConfirmationMessage() {
        return page.locator("[data-test='complete-header']").innerText();
    }

    public String getErrorMessage() {
        return page.locator("[data-test='error']").innerText();
    }
}
