package api;

import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Sample API tests against JSONPlaceholder (https://jsonplaceholder.typicode.com).
 *
 * These demonstrate common REST Assured patterns:
 *   - GET with path and query parameters
 *   - POST with a request body
 *   - PUT / PATCH
 *   - DELETE
 *   - Response body assertions with Hamcrest matchers
 *   - Extracting values from responses
 *
 * Replace BASE_URI in BaseTest and update these tests to target your API.
 */
@DisplayName("Sample API Tests")
class SampleApiTest extends BaseTest {

    // -----------------------------------------------------------------------
    // GET
    // -----------------------------------------------------------------------

    @Test
    @DisplayName("GET /posts - returns 200 and a non-empty list")
    void getAllPosts_returns200AndList() {
        given()
            .spec(requestSpec)
        .when()
            .get("/posts")
        .then()
            .spec(responseSpec)
            .statusCode(200)
            .contentType("application/json")
            .body("$", not(empty()))
            .body("size()", greaterThan(0));
    }

    @Test
    @DisplayName("GET /posts/{id} - returns correct post by ID")
    void getPostById_returnsCorrectPost() {
        given()
            .spec(requestSpec)
            .pathParam("id", 1)
        .when()
            .get("/posts/{id}")
        .then()
            .spec(responseSpec)
            .statusCode(200)
            .body("id", equalTo(1))
            .body("userId", notNullValue())
            .body("title", not(emptyOrNullString()))
            .body("body", not(emptyOrNullString()));
    }

    @Test
    @DisplayName("GET /posts?userId=1 - filters posts by query param")
    void getPostsByUserId_returnsFilteredResults() {
        given()
            .spec(requestSpec)
            .queryParam("userId", 1)
        .when()
            .get("/posts")
        .then()
            .spec(responseSpec)
            .statusCode(200)
            .body("$", not(empty()))
            .body("userId", everyItem(equalTo(1)));
    }

    @Test
    @DisplayName("GET /posts/9999 - returns 404 for unknown resource")
    void getPostById_returns404WhenNotFound() {
        given()
            .spec(requestSpec)
        .when()
            .get("/posts/9999")
        .then()
            .spec(responseSpec)
            .statusCode(404);
    }

    // -----------------------------------------------------------------------
    // POST
    // -----------------------------------------------------------------------

    @Test
    @DisplayName("POST /posts - creates a new post and returns 201")
    void createPost_returns201WithCreatedPost() {
        Map<String, Object> newPost = new HashMap<>();
        newPost.put("title", "IST Assessment Post");
        newPost.put("body", "This is a test post body.");
        newPost.put("userId", 1);

        given()
            .spec(requestSpec)
            .body(newPost)
        .when()
            .post("/posts")
        .then()
            .spec(responseSpec)
            .statusCode(201)
            .body("title", equalTo("IST Assessment Post"))
            .body("userId", equalTo(1))
            .body("id", notNullValue());
    }

    // -----------------------------------------------------------------------
    // PUT
    // -----------------------------------------------------------------------

    @Test
    @DisplayName("PUT /posts/{id} - replaces a post and returns 200")
    void updatePost_returns200WithUpdatedData() {
        Map<String, Object> updatedPost = new HashMap<>();
        updatedPost.put("id", 1);
        updatedPost.put("title", "Updated Title");
        updatedPost.put("body", "Updated body content.");
        updatedPost.put("userId", 1);

        given()
            .spec(requestSpec)
            .pathParam("id", 1)
            .body(updatedPost)
        .when()
            .put("/posts/{id}")
        .then()
            .spec(responseSpec)
            .statusCode(200)
            .body("title", equalTo("Updated Title"));
    }

    // -----------------------------------------------------------------------
    // PATCH
    // -----------------------------------------------------------------------

    @Test
    @DisplayName("PATCH /posts/{id} - partially updates a post")
    void patchPost_returnsUpdatedTitle() {
        Map<String, Object> patch = new HashMap<>();
        patch.put("title", "Patched Title");

        given()
            .spec(requestSpec)
            .pathParam("id", 1)
            .body(patch)
        .when()
            .patch("/posts/{id}")
        .then()
            .spec(responseSpec)
            .statusCode(200)
            .body("title", equalTo("Patched Title"));
    }

    // -----------------------------------------------------------------------
    // DELETE
    // -----------------------------------------------------------------------

    @Test
    @DisplayName("DELETE /posts/{id} - returns 200")
    void deletePost_returns200() {
        given()
            .spec(requestSpec)
            .pathParam("id", 1)
        .when()
            .delete("/posts/{id}")
        .then()
            .spec(responseSpec)
            .statusCode(200);
    }

    // -----------------------------------------------------------------------
    // Extracting response values
    // -----------------------------------------------------------------------

    @Test
    @DisplayName("Extract response fields and assert with JUnit assertions")
    void extractResponseFields_assertWithJUnit() {
        Response response = given()
            .spec(requestSpec)
            .pathParam("id", 1)
        .when()
            .get("/posts/{id}")
        .then()
            .spec(responseSpec)
            .statusCode(200)
            .extract().response();

        int id        = response.jsonPath().getInt("id");
        String title  = response.jsonPath().getString("title");

        assertEquals(1, id, "Post ID should be 1");
        assertNotNull(title, "Title should not be null");
        assertFalse(title.isBlank(), "Title should not be blank");
    }
}
