
//Instead of repeating locators in every test, we keep them inside the page class. If the application's login page changes, we update the locators in one place.

package com.sdet.pages;

import com.microsoft.playwright.Page;

public class LoginPage {

    private final Page page;

    public LoginPage(Page page) {
        this.page = page;
    }

    public void enterUsername(String username) {
        page.locator("#user-name").fill(username);
    }

    public void enterPassword(String password) {
        page.locator("#password").fill(password);
    }

    public void clickLogin() {
        page.locator("#login-button").click();
    }

    public void login(String username, String password) {
        enterUsername(username);
        enterPassword(password);
        clickLogin();
    }

    public String getErrorMessage() {
        return page.locator("[data-test='error']").innerText();
    }
}
