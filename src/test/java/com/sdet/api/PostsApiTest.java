
package com.sdet.api;

import com.sdet.clients.PostsApiClient;
import com.sdet.specs.ApiSpecFactory;

import io.restassured.response.Response;
import com.sdet.payloads.PostPayload;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

public class PostsApiTest extends BaseApiTest {

    private final PostsApiClient client = new PostsApiClient();

    @Test(groups = {"api", "smoke"})
    public void getPostTest() {

        Response response = client.getPost(1);

        response.then()
                .spec(ApiSpecFactory.responseSpec(200));

        Assert.assertEquals(
                response.jsonPath().getInt("id"),
                1
        );

        Assert.assertEquals(
                response.jsonPath().getInt("userId"),
                1
        );
    }

    @Test(groups = {"api", "smoke"})
    public void createPostTest() {

        PostPayload payload = new PostPayload(1,
                 "SDET Automation",
                 "REST Assured API testing"
);

Response response = client.createPost(payload);  
        response.then()
                .spec(ApiSpecFactory.responseSpec(201));

        Assert.assertEquals(
                response.jsonPath().getString("title"),
                "SDET Automation"
        );

        Assert.assertEquals(
                response.jsonPath().getInt("userId"),
                1
        );
    }

    @Test(groups = {"api", "regression"})
    public void updatePostTest() {

        Map<String, Object> payload = new HashMap<>();

        payload.put("id", 1);
        payload.put("title", "Updated Post");
        payload.put("body", "Updated using PUT");
        payload.put("userId", 1);

        Response response = client.updatePost(1, payload);

        response.then()
                .spec(ApiSpecFactory.responseSpec(200));

        Assert.assertEquals(
                response.jsonPath().getString("title"),
                "Updated Post"
        );
    }

    @Test(groups = {"api", "regression"})
    public void patchPostTest() {

        Map<String, Object> payload = new HashMap<>();

        payload.put("title", "Patched Post");

        Response response = client.patchPost(1, payload);

        response.then()
                .spec(ApiSpecFactory.responseSpec(200));

        Assert.assertEquals(
                response.jsonPath().getString("title"),
                "Patched Post"
        );
    }

    @Test(groups = {"api", "regression"})
    public void deletePostTest() {

        Response response = client.deletePost(1);

        response.then()
                .spec(ApiSpecFactory.responseSpec(200));
    }

    @Test(groups = {"api", "negative"})
    public void getInvalidPostTest() {

        Response response = client.getPost(999999);

        response.then()
                .spec(ApiSpecFactory.responseSpec(404));
    }
}
