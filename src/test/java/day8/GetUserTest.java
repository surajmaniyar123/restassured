package day8;

import static io.restassured.RestAssured.given;              // Import Rest Assured statically
import org.testng.ITestContext;                              // Import TestNG ITestContext
import org.testng.annotations.Test;                          // Import TestNG @Test

public class GetUserTest {

    static final String BASE_URL = "https://gorest.co.in/public/v2/users"; // Base URL of the API
    static final String BEARER_TOKEN = "c35e10e748c6f113775527bcef204e9929b4c9f4b995a8ee253eec46aed57b06"; // API Bearer token

    @Test(dependsOnMethods = {"day8.CreateUserTest.createUser"})          // Run after createUser() from CreateUserTest
    void getUser(ITestContext context) {

        given()
            .headers("Authorization", "Bearer " + BEARER_TOKEN)           // Add Authorization header with Bearer token
            .pathParam("id", (Integer) context.getAttribute("userId"))    // Get userId from test context and set as path param
           // .pathParam("id", (Integer) context.getSuite().getAttribute("userId"))  //for suite level
        .when()
            .get(BASE_URL + "/{id}")                                       // Send GET request to fetch user by ID
        .then()
            .statusCode(200)                                               // Validate HTTP 200 OK
            .log().body();                                                 // Log response body
    }
}
