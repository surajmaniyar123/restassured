package day8;

import org.testng.ITestContext; // Import TestNG ITestContext
import org.testng.annotations.Test; // Import TestNG @Test
import static io.restassured.RestAssured.*; // Import Rest Assured statically
import org.json.JSONObject; // Import JSONObject
import com.github.javafaker.Faker; // Import Faker for generating random data

public class CreateUserTest {

	static final String BASE_URL = "https://gorest.co.in/public/v2/users"; // Base URL of the API
	static final String BEARER_TOKEN = "c35e10e748c6f113775527bcef204e9929b4c9f4b995a8ee253eec46aed57b06"; // API Bearer token
	int userId; // To store the created user's ID

	Faker faker = new Faker(); // Faker instance to generate random data

	@Test
	void createUser(ITestContext context) {

		JSONObject requestData = new JSONObject(); // Create JSON object for request body
		requestData.put("name", faker.name().fullName()); // Add random full name
		requestData.put("gender", "Male"); // Set gender field
		requestData.put("email", faker.internet().emailAddress()); // Add random email address
		requestData.put("status", "inactive"); // Set status to inactive

		userId = given()
					.headers("Authorization", "Bearer " + BEARER_TOKEN) // Add Authorization header with Bearer token
					.contentType("application/json") // Set content type to JSON
					.body(requestData.toString()) // Attach request body as JSON string
				.when()
					.post(BASE_URL) // Send POST request to create user
				.then()
					.statusCode(201) // Verify HTTP 201 Created
					.log().body() // Log full response body
					.extract().jsonPath().getInt("id"); // Extract the created user's ID //We can also write like .extract().response().jsonPath().getInt("id");
				System.out.println(userId);
		
		
		/*Another way to extract only id from the response
		 * userId = given().headers("Authorization", "Bearer " + BEARER_TOKEN) // Add Authorization header with Bearer token
				.contentType("application/json") // Set content type to JSON
				.body(requestData.toString()) // Attach request body as JSON string
				.when().post(BASE_URL) // Send POST request to create user
				.jsonPath().getInt("Id");
		 * System.out.println(userId);
		 * 
		 */

		context.setAttribute("userId", userId); // Store userId in test context for later use
		//context.getSuite().setAttribute("userId", userId); //we can use this for suite level
	}
}