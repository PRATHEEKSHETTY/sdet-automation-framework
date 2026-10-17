
package com.sdet.clients;

import com.sdet.config.ConfigReader;
import io.restassured.response.Response;

import java.util.Map;

import static io.restassured.RestAssured.given;

public class AuthApiClient {

    public Response login(Map<String, Object> payload) {

        return given()
                .baseUri(ConfigReader.get("auth.api.base.url"))
                .contentType("application/json")
                .accept("application/json")
                .body(payload)
                .log().method()
                .log().uri()
                .when()
                .post("/auth/login");
    }
}
