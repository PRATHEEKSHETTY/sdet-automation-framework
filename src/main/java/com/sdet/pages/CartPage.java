
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

    public String getProductPrice() {
        return page.locator(
            ".cart_item .inventory_item_price"
        ).innerText();
    }

    public String getProductQuantity() {
        return page.locator(
            ".cart_item .cart_quantity"
        ).innerText();
    }

    public void clickCheckout() {
        page.locator("[data-test='checkout']").click();
    }

    // Remove Sauce Labs Backpack from cart
    public void removeBackpackFromCart() {
        page.locator(
            "[data-test='remove-sauce-labs-backpack']"
        ).click();
    }

    // Verify cart contains no products
    public boolean isCartEmpty() {
        return page.locator(".cart_item").count() == 0;
    }
}
