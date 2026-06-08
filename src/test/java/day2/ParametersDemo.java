package day2;

import static io.restassured.RestAssured.*;   // Import Rest Assured methods statically
import org.testng.annotations.Test;            // Import TestNG @Test annotation

/**
 * ============================================================
 *  Rest Assured - Parameters Handling Demo
 * ============================================================
 * Demonstrates usage of:
 * 1️⃣ Path Parameters  → part of the URL path
 * 2️⃣ Query Parameters → added after '?' in the URL
 * Examples:
 *  • Path param:   https://restcountries.com/v2/name/India
 *  • Query params: https://reqres.in/api/users?page=2&id=5
 * ============================================================
 */
public class ParametersDemo {

    // ============================================================
    // 1️⃣ Example of PATH PARAMETERS
    // ============================================================
    //@Test   // Uncomment to run this test
    void pathParams() {

        given()
            .pathParam("country", "India")   // Set path parameter {country} dynamically

        .when()
            .get("https://restcountries.com/v2/name/{country}")   // Use path param in endpoint URL => /name/India

        .then()
            .statusCode(200)     // Expect HTTP 200 OK
            .log().body();       // Log response body
    }

    // ============================================================
    // 2️⃣ Example of QUERY PARAMETERS
    // ============================================================
    @Test
    void queryParams() {

        given()
            .queryParam("page", 2)   // First query parameter appended as ?page=2
            .queryParam("id", 5)     // Second query parameter appended as &id=5

        .when()
            .get("https://reqres.in/api/users")   // Send GET request with query params //URL becomes: /api/users?page=2&id=5

        .then()
            .statusCode(200)     // Verify HTTP 200 OK
            .log().body();       // Print response body in console
    }
}
