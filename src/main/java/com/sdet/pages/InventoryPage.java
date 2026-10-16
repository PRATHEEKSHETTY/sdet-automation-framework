
package com.sdet.pages;

import com.microsoft.playwright.Page;

public class InventoryPage {

    private final Page page;

    public InventoryPage(Page page) {
        this.page = page;
    }

    public void addBackpackToCart() {
        page.locator("[data-test='add-to-cart-sauce-labs-backpack']").click();
    }

    public void openCart() {
        page.locator("[data-test='shopping-cart-link']").click();
    }
}
