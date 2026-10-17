
package com.sdet.api;

import com.sdet.clients.AuthApiClient;
import io.restassured.response.Response;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

public class AuthApiTest {

    private final AuthApiClient client = new AuthApiClient();

    @DataProvider(name = "invalidLoginData")
    public Object[][] invalidLoginData() {

        return new Object[][] {
            {
                "Invalid password",
                "emilys",
                "wrong_password",
                "Invalid credentials"
            },
            {
                "Missing password",
                "emilys",
                null,
                "Username and password required"
            },
            {
                "Missing username",
                null,
                "emilyspass",
                "Username and password required"
            }
        };
    }

    @Test(
        dataProvider = "invalidLoginData",
        groups = {"api", "negative", "regression"}
    )
    public void invalidAuthenticationTest(
            String scenario,
            String username,
            String password,
            String expectedMessage) {

        Map<String, Object> payload = new HashMap<>();

        if (username != null) {
            payload.put("username", username);
        }

        if (password != null) {
            payload.put("password", password);
        }

        Response response = client.login(payload);

        // Verify invalid input is rejected
        Assert.assertEquals(
            response.statusCode(),
            400,
            "Expected HTTP 400 for: " + scenario
        );

        // Verify meaningful error response
        Assert.assertEquals(
            response.jsonPath().getString("message"),
            expectedMessage,
            "Incorrect error message for: " + scenario
        );
    }
}
