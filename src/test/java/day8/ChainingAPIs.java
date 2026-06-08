package day8;

import org.testng.annotations.Test;
import static io.restassured.RestAssured.*;

import org.json.JSONObject;
import com.github.javafaker.Faker;

public class ChainingAPIs {

	static final String BASE_URL = "https://gorest.co.in/public/v2/users"; // Base URL of the API
	static final String BEARER_TOKEN = "c35e10e748c6f113775527bcef204e9929b4c9f4b995a8ee253eec46aed57b06"; // Bearer token for authentication
	int userId; // Variable to store user ID for chaining requests
	Faker faker = new Faker(); // Faker instance to generate random test data

	@Test
	void createUser() 
	{
		JSONObject requestData = new JSONObject(); // Prepare request JSON body
		requestData.put("name", faker.name().fullName()); // Random full name
		requestData.put("gender", "Male"); // Gender
		requestData.put("email", faker.internet().emailAddress()); // Random email
		requestData.put("status", "inactive"); // User status

		userId = given()
					.headers("Authorization", "Bearer " + BEARER_TOKEN) // Set authorization header
					.contentType("application/json") // Set content type
					.body(requestData.toString()) // Attach request body
				.when()
					.post(BASE_URL) // Send POST request to create user
				.then()
					.statusCode(201) // Verify status code 201 Created
					.extract().response().jsonPath().getInt("id"); // Extract user ID from response
	}

	@Test(dependsOnMethods = { "createUser" })
	void getUser() 
	{
		 		given()
		 			.headers("Authorization", "Bearer " + BEARER_TOKEN) // Authorization header
		 			.pathParam("id", userId) // Path parameter for user ID
				.when()
					.get(BASE_URL + "/{id}") // GET request to fetch user details
				.then()
					.statusCode(200) // Verify status code 200 OK
					.log().body(); // Log response body
	}

	@Test(dependsOnMethods = { "getUser" })
	void updateUser() 
	{
		JSONObject requestData = new JSONObject(); // Prepare request JSON body with updated details
		requestData.put("name", faker.name().fullName()); // Random full name
		requestData.put("gender", "Male"); // Gender
		requestData.put("email", faker.internet().emailAddress()); // Random email
		requestData.put("status", "active"); // Update status to active

				given()
					.headers("Authorization", "Bearer " + BEARER_TOKEN) // Authorization header
					.contentType("application/json") // Content type
					.body(requestData.toString()) // Request body
					.pathParam("id", userId) // User ID path param
				.when()
					.put(BASE_URL + "/{id}") // PUT request to update user
				.then()
					.statusCode(200) // Verify 200 OK
					.log().body(); // Log response
	}

	@Test(dependsOnMethods = { "updateUser" })
	void deleteUser() 
	{
				given()
					.headers("Authorization", "Bearer " + BEARER_TOKEN) // Authorization header
					.pathParam("id", userId) // User ID path param
				.when()
					.delete(BASE_URL + "/{id}") // DELETE request to remove user
				.then()
					.statusCode(204); // Verify 204 No Content
	}
}
