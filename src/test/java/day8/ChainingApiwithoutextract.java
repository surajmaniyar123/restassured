package day8;

import org.testng.annotations.Test;
import static io.restassured.RestAssured.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import org.json.JSONObject;
import com.github.javafaker.Faker;

import io.restassured.response.Response;

public class ChainingApiwithoutextract {

    static final String BASE_URL = "https://gorest.co.in/public/v2/users"; // Base URL
    static final String BEARER_TOKEN = "c35e10e748c6f113775527bcef204e9929b4c9f4b995a8ee253eec46aed57b06"; // Bearer token
    int userId; // Variable to store user ID
    Faker faker = new Faker(); // Faker for random data

    @Test
    void createUser() {
        JSONObject requestData = new JSONObject();
        requestData.put("name", faker.name().fullName());
        requestData.put("gender", "Male");
        requestData.put("email", faker.internet().emailAddress());
        requestData.put("status", "inactive");

        // Send POST request and get response
        Response response = given()
                                .headers("Authorization", "Bearer " + BEARER_TOKEN)
                                .contentType("application/json")
                                .body(requestData.toString())
                            .when()
                                .post(BASE_URL);

        // Assertions
        assertThat("Status code should be 201", response.getStatusCode(), equalTo(201));

        // Extract user ID from response JSON
        userId = response.jsonPath().getInt("id");
        System.out.println("✅ Created user ID: " + userId);
        
        
        /*
         * Another way to extract ID:
         * Convert response to JsonPath
         * JsonPath json = new JsonPath(response.asString());
         * Extract user ID
         * userId = json.getInt("id");
         */
    }

    @Test(dependsOnMethods = { "createUser" })
    void getUser() {
        Response response = given()
                                .headers("Authorization", "Bearer " + BEARER_TOKEN)
                                .pathParam("id", userId)
                            .when()
                                .get(BASE_URL + "/{id}");

        // Assertions
        assertThat("Status code should be 200", response.getStatusCode(), equalTo(200));
        assertThat("User ID should match", response.jsonPath().getInt("id"), equalTo(userId));

        System.out.println("✅ Fetched user details:\n" + response.getBody().asString());
    }

    @Test(dependsOnMethods = { "getUser" })
    void updateUser() {
        JSONObject requestData = new JSONObject();
        requestData.put("name", faker.name().fullName());
        requestData.put("gender", "Male");
        requestData.put("email", faker.internet().emailAddress());
        requestData.put("status", "active");

        Response response = given()
                                .headers("Authorization", "Bearer " + BEARER_TOKEN)
                                .contentType("application/json")
                                .body(requestData.toString())
                                .pathParam("id", userId)
                            .when()
                                .put(BASE_URL + "/{id}");

        // Assertions
        assertThat("Status code should be 200", response.getStatusCode(), equalTo(200));
        assertThat("Status should be active", response.jsonPath().getString("status"), equalTo("active"));

        System.out.println("✅ Updated user details:\n" + response.getBody().asString());
    }

    @Test(dependsOnMethods = { "updateUser" })
    void deleteUser() {
        Response response = given()
                                .headers("Authorization", "Bearer " + BEARER_TOKEN)
                                .pathParam("id", userId)
                            .when()
                                .delete(BASE_URL + "/{id}");

        // Assertion
        assertThat("Status code should be 204", response.getStatusCode(), equalTo(204));
        System.out.println("✅ Deleted user with ID: " + userId);
    }
}
