
package com.sdet.clients;

import com.sdet.specs.ApiSpecFactory;

import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class PostsApiClient {

    public Response getPost(int postId) {

        return given()
                .spec(ApiSpecFactory.requestSpec())
        .when()
                .get("/posts/{id}", postId);
    }

    public Response createPost(Object payload) {

        return given()
                .spec(ApiSpecFactory.requestSpec())
                .body(payload)
        .when()
                .post("/posts");
    }

    public Response updatePost(int postId, Object payload) {

        return given()
                .spec(ApiSpecFactory.requestSpec())
                .body(payload)
        .when()
                .put("/posts/{id}", postId);
    }

    public Response patchPost(int postId, Object payload) {

        return given()
                .spec(ApiSpecFactory.requestSpec())
                .body(payload)
        .when()
                .patch("/posts/{id}", postId);
    }

    public Response deletePost(int postId) {

        return given()
                .spec(ApiSpecFactory.requestSpec())
        .when()
                .delete("/posts/{id}", postId);
    }
}
