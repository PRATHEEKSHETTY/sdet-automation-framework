package com.sdet.api;

import com.sdet.clients.PostsApiClient;
import com.sdet.specs.ApiSpecFactory;
import com.sdet.payloads.PostPayload;

import io.restassured.response.Response;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

public class PostsApiTest {

    private final PostsApiClient client = new PostsApiClient();

    @Test(groups = {"api", "smoke"})
    public void getPostTest() {

        Response response = client.getPost(1);

        response.then()
                .spec(ApiSpecFactory.responseSpec(200));

        Assert.assertEquals(
                response.jsonPath().getInt("id"),
                1,
                "Post ID should match the requested ID"
        );

        Assert.assertEquals(
                response.jsonPath().getInt("userId"),
                1,
                "User ID should be 1"
        );

        Assert.assertNotNull(
                response.jsonPath().getString("title"),
                "Post title should not be null"
        );

        Assert.assertFalse(
                response.jsonPath().getString("title").isBlank(),
                "Post title should not be empty"
        );

        Assert.assertNotNull(
                response.jsonPath().getString("body"),
                "Post body should not be null"
        );

        Assert.assertFalse(
                response.jsonPath().getString("body").isBlank(),
                "Post body should not be empty"
        );
    }

    @Test(groups = {"api", "smoke"})
    public void createPostTest() {

        String title = "SDET Automation";
        String body = "REST Assured API testing";
        int userId = 1;

        PostPayload payload = new PostPayload(
                userId,
                title,
                body
        );

        Response response = client.createPost(payload);

        response.then()
                .spec(ApiSpecFactory.responseSpec(201));

        Assert.assertEquals(
                response.jsonPath().getString("title"),
                title,
                "Created post title should match request"
        );

        Assert.assertEquals(
                response.jsonPath().getString("body"),
                body,
                "Created post body should match request"
        );

        Assert.assertEquals(
                response.jsonPath().getInt("userId"),
                userId,
                "Created post user ID should match request"
        );

        Assert.assertNotNull(
                response.jsonPath().get("id"),
                "Created post should have an ID"
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
                response.jsonPath().getInt("id"),
                1,
                "Updated post ID should remain unchanged"
        );

        Assert.assertEquals(
                response.jsonPath().getString("title"),
                payload.get("title"),
                "Updated title should match request"
        );

        Assert.assertEquals(
                response.jsonPath().getString("body"),
                payload.get("body"),
                "Updated body should match request"
        );

        Assert.assertEquals(
                response.jsonPath().getInt("userId"),
                payload.get("userId"),
                "Updated user ID should match request"
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
                response.jsonPath().getInt("id"),
                1,
                "Patched post ID should remain unchanged"
        );

        Assert.assertEquals(
                response.jsonPath().getString("title"),
                payload.get("title"),
                "Patched title should match request"
        );

        Assert.assertNotNull(
                response.jsonPath().getString("body"),
                "Existing post body should remain present"
        );

        Assert.assertEquals(
                response.jsonPath().getInt("userId"),
                1,
                "Existing user ID should remain unchanged"
        );
    }

    @Test(groups = {"api", "regression"})
    public void deletePostTest() {

        Response response = client.deletePost(1);

        response.then()
                .spec(ApiSpecFactory.responseSpec(200));

        Assert.assertEquals(
                response.getBody().asString().trim(),
                "{}",
                "DELETE response should contain an empty JSON object"
        );
    }

    @Test(groups = {"api", "negative"})
    public void getInvalidPostTest() {

        Response response = client.getPost(999999);

        response.then()
                .spec(ApiSpecFactory.responseSpec(404));

        Assert.assertEquals(
                response.getBody().asString().trim(),
                "{}",
                "Invalid post should return an empty JSON object"
        );
    }

    @Test(groups = {"api", "negative"})
    public void createPostWithMissingFieldsTest() {

        // Empty payload: title, body and userId are missing.
        Map<String, Object> payload = new HashMap<>();

        Response response = client.createPost(payload);

        // JSONPlaceholder is a mock API and may accept
        // incomplete payloads instead of rejecting them.
        response.then()
                .spec(ApiSpecFactory.responseSpec(201));

        Assert.assertNotNull(
                response.jsonPath().get("id"),
                "Mock API should return a generated post ID"
        );

        System.out.println(
                "Known API limitation: JSONPlaceholder accepts " +
                "a POST request with missing fields."
        );
    }

    @Test(groups = {"api", "negative"})
    public void createPostWithMalformedJsonTest() {

        String invalidJson = "{\"title\":\"Test Post";

        Response response = client.createPost(invalidJson);

        response.then()
                .spec(ApiSpecFactory.responseSpec(500));

        Assert.assertTrue(
                response.getBody().asString().contains("SyntaxError"),
                "Response should contain a JSON parsing error"
        );

        System.out.println(
                "Known API limitation: JSONPlaceholder returns " +
                "HTTP 500 instead of HTTP 400 for malformed JSON."
        );
    }
}