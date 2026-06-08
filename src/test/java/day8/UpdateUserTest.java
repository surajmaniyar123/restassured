package day8;

import static io.restassured.RestAssured.*;                           // Import Rest Assured statically
import org.json.JSONObject;                                           // Import JSONObject for request body
import org.testng.ITestContext;                                        // Import TestNG ITestContext
import org.testng.annotations.Test;                                    // Import TestNG @Test
import com.github.javafaker.Faker;                                     // Import Faker for generating random data

public class UpdateUserTest {

    static final String BASE_URL = "https://gorest.co.in/public/v2/users"; // Base URL of the API
    static final String BEARER_TOKEN = "c35e10e748c6f113775527bcef204e9929b4c9f4b995a8ee253eec46aed57b06"; // API Bearer token

    @Test(dependsOnMethods = {"day8.GetUserTest.getUser"})                 // Run after getUser() from GetUserTest
    void updateUser(ITestContext context) {

        JSONObject requestData = new JSONObject();                        // Create JSONObject for request body
        Faker faker = new Faker();                                         // Create Faker instance for random data

        requestData.put("name", faker.name().fullName());                  // Set random name
        requestData.put("gender", "Male");                                 // Set gender
        requestData.put("email", faker.internet().emailAddress());         // Set random email
        requestData.put("status", "active");                               // Set status

        given()
            .headers("Authorization", "Bearer " + BEARER_TOKEN)           // Add Authorization header with Bearer token
            .contentType("application/json")                               // Set content type to JSON
            .body(requestData.toString())                                   // Attach request body
            .pathParam("id", (Integer) context.getAttribute("userId"))     // Get userId from context and set as path param
        .when()
            .put(BASE_URL + "/{id}")                                        // Send PUT request to update user by ID
        .then()
            .statusCode(200)                                               // Validate HTTP 200 OK
            .log().body();                                                 // Log response body
    }
}
