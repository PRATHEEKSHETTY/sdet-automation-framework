
package com.sdet.pages;

import com.microsoft.playwright.Page;

public class CartPage {

    private final Page page;

    public CartPage(Page page) {
        this.page = page;
    }

    public String getProductName() {
        return page.locator(
            "[data-test='cart-list'] [data-test='inventory-item-name']"
        ).innerText();
    }

    public void clickCheckout() {
        page.locator("[data-test='checkout']").click();
    }
}
