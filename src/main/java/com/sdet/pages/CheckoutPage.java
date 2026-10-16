
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
