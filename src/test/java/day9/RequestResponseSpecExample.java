package day9;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class RequestResponseSpecExample {

    RequestSpecification requestSpec;
    ResponseSpecification responseSpec;

    // =========================================
    // Setup: Runs once before all test cases
    // =========================================
    @BeforeClass
    public void setup() {

        // ---------- Request Specification ----------
        requestSpec = new RequestSpecBuilder()
                .setBaseUri("https://reqres.in")          // Base URI
                .setBasePath("/api")                      // Base Path
                .setContentType("application/json")       // Content-Type
                .addHeader("Accept", "application/json")  // Header
                .addPathParam("id", 2)                    // Path Param (common)
                .addQueryParam("page", 2)                 // Query Param (common)
                .build();

        // ---------- Response Specification ----------
        responseSpec = new ResponseSpecBuilder()
                .expectContentType("application/json")    // Content-Type
                .expectHeader("Content-Type", containsString("application/json"))
                .expectResponseTime(lessThan(3000L))      // Response Time
                .build();
    }

    // =========================================
    // Test Case 1: GET with Path Parameter
    // =========================================
    @Test
    public void getSingleUserWithPathParam() {

        given()
                .spec(requestSpec) 
        .when()
                .get("/users/{id}")                       //https://reqres.in/api/users/2?page=2
        .then()
                .spec(responseSpec)
                .statusCode(200)
                .body("data.id", equalTo(2))
                .body("data.email", containsString("@"));
    }

    // =========================================
    // Test Case 2: GET with Query Parameter
    // =========================================
    @Test
    public void getUsersWithQueryParam() {

        given()
                .spec(requestSpec)
        .when()
                .get("/users")                          
        .then()
                .spec(responseSpec)
                .statusCode(200)
                .body("page", equalTo(2));
    }

    // =========================================
    // Test Case 3: POST with Request Body
    // =========================================
    @Test
    public void createUserWithRequestBody() {

        String requestBody = "{\n" +
                "  \"name\": \"John\",\n" +
                "  \"job\": \"QA Engineer\"\n" +
                "}";

        given()
                .spec(requestSpec)
                .body(requestBody)                        
        .when()
                .post("/users")
        .then()
                .statusCode(201)                          
                .body("name", equalTo("John"))
                .body("job", equalTo("QA Engineer"));
    }
}
