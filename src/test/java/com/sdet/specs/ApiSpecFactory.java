
package com.sdet.specs;

import com.sdet.config.ConfigReader;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

public class ApiSpecFactory {

    public static RequestSpecification requestSpec() {

        return new RequestSpecBuilder()
                .setBaseUri(ConfigReader.get("api.base.url"))
                .setContentType(ContentType.JSON)
                .setAccept(ContentType.JSON)
                .build();
    }

    public static ResponseSpecification responseSpec(int statusCode) {

        return new ResponseSpecBuilder()
                .expectStatusCode(statusCode)
                .build();
    }
}
