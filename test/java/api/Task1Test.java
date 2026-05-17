package api;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.*;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

/**
 * IST Assessment – REST Assured API Tests
 *
 * Target:     https://reqres.in/api/users/2
 * Auth:       x-api-key: reqres-free-v1
 *
 */
@DisplayName("IST Assessment – reqres.in API Tests")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class Task1Test {

    private static final String BASE_URI  = "https://reqres.in";
    private static final String ENDPOINT  = "/api/users/2";
    private static final String API_KEY   = "reqres-free-v1";

    
    private static final String YOUR_EMAIL = "ksmith2@inspiredtesting.com";

    private static RequestSpecification requestSpec;

    @BeforeAll
    static void setup() {
        RestAssured.baseURI = BASE_URI;

        requestSpec = new RequestSpecBuilder()
                .setBaseUri(BASE_URI)
                .setContentType(ContentType.JSON)
                .setAccept(ContentType.JSON)
                .addHeader("x-api-key", API_KEY)
                .build();
    }

    // -----------------------------------------------------------------------
    // Step 1 – Retrieve user data
    // Step 2 – Print the status code
    // Step 3 – Print the first name
    // Step 4 – Extract and print the URL from the response body
    // -----------------------------------------------------------------------

    @Test
    @Order(1)
    @DisplayName("Step 1-4: GET /api/users/2 – retrieve data, print status code, first name, and URL")
    void retrieveUserData_printStatusCodeFirstNameAndUrl() {

        Response response = given()
                .spec(requestSpec)
            .when()
                .get(ENDPOINT)
            .then()
                .statusCode(200)
                .body("data.id",         equalTo(2))
                .body("data.first_name", not(emptyOrNullString()))
                .body("support.url",     not(emptyOrNullString()))
                .extract().response();

        // Step 2: Print status code
        System.out.println("=== Step 2: Status Code ===");
        System.out.println("Status Code: " + response.getStatusCode());

        // Step 3: Print first name
        String firstName = response.jsonPath().getString("data.first_name");
        System.out.println("\n=== Step 3: First Name ===");
        System.out.println("First Name: " + firstName);

        // Step 4: Extract and print the URL from support.url
        String url = response.jsonPath().getString("support.url");
        System.out.println("\n=== Step 4: URL from Response Body ===");
        System.out.println("URL: " + url);
    }

    // -----------------------------------------------------------------------
    // Step 5 – Update the email address
    // -----------------------------------------------------------------------

    @Test
    @Order(2)
    @DisplayName("Step 5: PATCH /api/users/2 – update email address")
    void updateEmailAddress() {

        Map<String, String> body = new HashMap<>();
        body.put("email", YOUR_EMAIL);

        Response response = given()
                .spec(requestSpec)
                .body(body)
            .when()
                .patch(ENDPOINT)
            .then()
                .statusCode(200)
                .body("email", equalTo(YOUR_EMAIL))
                .extract().response();

        System.out.println("=== Step 5: Updated Email ===");
        System.out.println("Email updated to: " + response.jsonPath().getString("email"));
    }

    // -----------------------------------------------------------------------
    // Step 6 – Delete the last name (clear it via PATCH)
    // -----------------------------------------------------------------------

    @Test
    @Order(3)
    @DisplayName("Step 6: PATCH /api/users/2 – delete (clear) last name")
    void deleteLastName() {

        Map<String, Object> body = new HashMap<>();
        body.put("last_name", null);

        Response response = given()
                .spec(requestSpec)
                .body(body)
            .when()
                .patch(ENDPOINT)
            .then()
                .statusCode(200)
                .extract().response();

        System.out.println("=== Step 6: Last Name After Deletion ===");
        System.out.println("Last name: '" + response.jsonPath().getString("last_name") + "'");
    }
}
