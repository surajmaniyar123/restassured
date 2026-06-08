package day8;

import static io.restassured.RestAssured.given;                       // Import Rest Assured statically
import org.testng.ITestContext;                                        // Import TestNG ITestContext
import org.testng.annotations.Test;                                    // Import TestNG @Test

public class DeleteUserTest {

    static final String BASE_URL = "https://gorest.co.in/public/v2/users"; // Base URL of the API
    static final String BEARER_TOKEN = "c35e10e748c6f113775527bcef204e9929b4c9f4b995a8ee253eec46aed57b06"; // API Bearer token

    @Test(dependsOnMethods = {"day8.UpdateUserTest.updateUser"})       // Run after updateUser() from UpdateUserTest
    void deleteUser(ITestContext context) {

        given()
            .headers("Authorization", "Bearer " + BEARER_TOKEN)       // Add Authorization header with Bearer token
            .pathParam("id", (Integer) context.getAttribute("userId")) // Get userId from context and set as path param
        .when()
            .delete(BASE_URL + "/{id}")                                 // Send DELETE request to delete user by ID
        .then()
            .statusCode(204);                                           // Validate HTTP 204 No Content (successful deletion)
    }
}
