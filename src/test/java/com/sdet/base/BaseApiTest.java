
package com.sdet.api;

import io.restassured.RestAssured;
import org.testng.annotations.BeforeClass;
import com.sdet.config.ConfigReader;

public class BaseApiTest {

    @BeforeClass(alwaysRun = true)
    public void setupApi() {
        RestAssured.baseURI = ConfigReader.get("api.base.url");
    }
}
