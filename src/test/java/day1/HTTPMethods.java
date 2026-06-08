package day1;

import org.testng.annotations.Test;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import java.util.HashMap;

/**
 * Given() → all request preconditions (URI, headers, params, auth, body, content type, cookies)

When() → only the action (HTTP method + endpoint)

Then() → all validations (status code, headers, cookies, body, time)
 */
public class HTTPMethods {

	int userId; // store the ID of the user created during the test run

	@Test(priority = 1)
	void getUsers() {
				given()
					.header("Content-Type", "application/json") // optional for GET, keeps consistency
				.when()
					.get("https://reqres.in/api/users?page=2") // send GET request to fetch page 2 users
				.then()
					.statusCode(200) // verify HTTP status code is 200 OK
				    .statusLine(containsString("OK"))
					.header("Content-Type", equalTo("application/json; charset=utf-8")) // verify response header
					.body("page", equalTo(2)) // verify JSON field "page" equals 2
					.body(containsString("email")) // check that response contains "email" field
					.time(lessThan(3000L)) // verify response time is under 3 seconds
					.log().all(); // log full response for debugging
	}

	@Test(priority = 2)
	void createUsers() {

		HashMap<String, String> data = new HashMap<>(); // create request body as a map
		data.put("name", "morpheus"); // set name field
		data.put("job", "leader"); // set job field

		userId = given()
					.header("x-api-key", "reqres-free-v1") // optional custom header
					.header("Content-Type", "application/json") // set content type to JSON
					//.contentType("application/json") // alternative way to set content type
					.body(data) // attach request body
				.when()
					.post("https://reqres.in/api/users") // send POST request to create user
				.then()
					.statusCode(201) // verify user creation success
					.header("Content-Type", equalTo("application/json; charset=utf-8")) // verify response header
					.body("name", equalTo("morpheus")) // verify response name matches request
					.body(containsString("id")) // check that response contains "id" field
					.log().all() // log full response
					.extract().jsonPath().getInt("id"); // extract user ID from response for later use

		System.out.println("✅ Created user with ID: " + userId); // print created user ID
	}

	@Test(priority = 3, dependsOnMethods = { "createUsers" })
	void updateUsers() {

		HashMap<String, String> data = new HashMap<>(); // prepare updated request body
		data.put("name", "morpheus1"); // updated name
		data.put("job", "leader1"); // updated job

				given()
					.header("x-api-key", "reqres-free-v1") // optional custom header
					.header("Content-Type", "application/json") // set content type
					.body(data) // attach updated data
				.when()
					.put("https://reqres.in/api/users/" + userId) // send PUT request to update user
				.then()
					.statusCode(200) // verify successful update
					.header("Content-Type", equalTo("application/json; charset=utf-8")) // verify response header
					.body("name", equalTo("morpheus1")) // verify updated name
					.log().all(); // log response
	}

	@Test(priority = 4, dependsOnMethods = { "createUsers","updateUsers" })
	void deleteUsers() {
				given()
					.header("x-api-key", "reqres-free-v1") // optional custom header
				.when()
					.delete("https://reqres.in/api/users/" + userId) // send DELETE request
				.then()
					.statusCode(204) // verify user deletion (204 = No Content)
					.body(emptyOrNullString()) // response body should be empty
					.log().all(); // log response

		System.out.println("✅ Deleted user with ID: " + userId); // print confirmation
	}
}
