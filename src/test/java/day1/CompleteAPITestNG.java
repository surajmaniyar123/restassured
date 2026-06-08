package day1;

import static io.restassured.RestAssured.given; // For BDD-style RestAssured requests
import static org.hamcrest.Matchers.*;          // For Hamcrest matchers like equalTo, containsString, etc.

import io.restassured.RestAssured;             // Core RestAssured class
import io.restassured.http.ContentType;        // Enum for Content-Type
import org.testng.annotations.BeforeClass;     // TestNG setup before all tests
import org.testng.annotations.Test;            // TestNG test annotation

public class CompleteAPITestNG {

    // Store the user ID returned from POST request
    // This will be reused in GET, PUT, PATCH, DELETE tests
    private String userId;

    // -------------------------------
    // 1) Setup Base URI and Base Path
    // This method runs once before all tests
    // Base URI: https://reqres.in
    // Base Path: /api
    // Full API endpoint example: https://reqres.in/api/users/2?delay=3
    // -------------------------------
    @BeforeClass
    public void setup() {
        RestAssured.baseURI = "https://reqres.in";  // Set domain for API
        RestAssured.basePath = "/api";              // Set base path for endpoints
    }

    // -------------------------------
    // 2) POST - Create a new user
    // -------------------------------
    @Test(priority = 1)
    public void testCreateUser() {
        // Store the generated userId from response for future tests
        userId =
        given()                                      // GIVEN: request setup
            .contentType(ContentType.JSON)           // Set Content-Type header to JSON
            .body("{ \"name\": \"John\", \"job\": \"Developer\" }") // JSON payload
        .when()                                      // WHEN: HTTP method and endpoint
            .post("/users")                          // POST request to /users
        .then()                                      // THEN: assertions on response
            .statusCode(201)                         // Assert HTTP 201 Created
            .body("name", equalTo("John"))           // Assert response JSON 'name' field
            .body("job", equalTo("Developer"))       // Assert response JSON 'job' field
            .body("id", notNullValue())              // Assert 'id' is returned
            .header("Content-Type", containsString("json")) // Assert Content-Type header contains 'json'
            .time(lessThan(3000L))                   // Assert response time < 3 seconds
            .log().all()                              // Log full response for debugging
            .extract()                                // Extract value from response
            .path("id");                              // Save 'id' for further tests
    }

    // -------------------------------
    // 3) GET - Retrieve the created user
    // -------------------------------
    @Test(priority = 2, dependsOnMethods = "testCreateUser") // Run after POST
    public void testGetUser() {

        given()                                      // GIVEN
            .pathParam("userId", userId)            // Path parameter {userId} -> replaced in URL
            .queryParam("delay", 1)                 // Query parameter ?delay=1 appended to URL
            .header("Content-Type", "application/json") // Set header
        .when()                                      // WHEN
            .get("/users/{userId}")                 // GET request with path param
        .then()                                      // THEN
            .statusCode(200)                         // HTTP 200 OK
            .body("data.id", equalTo(Integer.parseInt(userId))) // Assert JSON 'id' matches
            .body("support.text", containsString("support"))   // Assert support text exists
            .log().all();                             // Log full response
    }

    // -------------------------------
    // 4) PUT - Update the created user completely
    // -------------------------------
    @Test(priority = 3, dependsOnMethods = "testCreateUser")
    public void testUpdateUser() {

        given()                                      // GIVEN
            .contentType(ContentType.JSON)           // JSON content type
            .pathParam("userId", userId)            // Path param {userId}
            .body("{ \"name\": \"John\", \"job\": \"QA Engineer\" }") // Updated JSON payload
        .when()                                      // WHEN
            .put("/users/{userId}")                  // PUT request to update user
        .then()                                      // THEN
            .statusCode(200)                         // HTTP 200 OK
            .body("name", equalTo("John"))           // Assert name updated
            .body("job", equalTo("QA Engineer"))     // Assert job updated
            .log().all();                             // Log full response
    }

    // -------------------------------
    // 5) PATCH - Partial update (only job)
    // -------------------------------
    @Test(priority = 4, dependsOnMethods = "testCreateUser")
    public void testPatchUser() {

        given()                                      // GIVEN
            .contentType(ContentType.JSON)           // JSON content type
            .pathParam("userId", userId)            // Path param
            .body("{ \"job\": \"Senior QA\" }")      // Only 'job' field updated
        .when()                                      // WHEN
            .patch("/users/{userId}")                // PATCH request
        .then()                                      // THEN
            .statusCode(200)                         // HTTP 200 OK
            .body("job", equalTo("Senior QA"))       // Assert job updated
            .log().all();                             // Log full response
    }

    // -------------------------------
    // 6) DELETE - Remove the created user
    // -------------------------------
    @Test(priority = 5, dependsOnMethods = "testCreateUser")
    public void testDeleteUser() {

        given()                                      // GIVEN
            .pathParam("userId", userId)            // Path param
        .when()                                      // WHEN
            .delete("/users/{userId}")              // DELETE request
        .then()                                      // THEN
            .statusCode(204)                         // Assert HTTP 204 No Content
            .log().all();                             // Log full response
    }
}
