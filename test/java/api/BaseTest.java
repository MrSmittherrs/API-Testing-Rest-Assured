package api;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.junit.jupiter.api.BeforeAll;

/**
 * Base class for all API tests.
 *
 * Configure the base URI, default headers, logging, and shared
 * request/response specifications here. All test classes extend this.
 */
public abstract class BaseTest {

    // Change this to the base URL of the API under test
    protected static final String BASE_URI = "https://reqres.in/api/users/2";

    protected static RequestSpecification requestSpec;
    protected static ResponseSpecification responseSpec;

    @BeforeAll
    static void setup() {
        RestAssured.baseURI = BASE_URI;
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();

        requestSpec = new RequestSpecBuilder()
                .setBaseUri(BASE_URI)
                .setContentType(ContentType.JSON)
                .setAccept(ContentType.JSON)
                .log(LogDetail.METHOD)
                .log(LogDetail.URI)
                .build();

        responseSpec = new ResponseSpecBuilder()
                .log(LogDetail.STATUS)
                .build();
    }
}
