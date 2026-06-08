package day5;

import io.restassured.response.Response;
import io.restassured.path.json.JsonPath;

import static io.restassured.RestAssured.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import java.io.File;

import org.testng.annotations.Test;

public class ValidateApiResponseWithExpectedJson {

	@Test
	public void validateUserDetailsAgainstExpectedJsonFile() {

		// 1️⃣ Send API request and capture response
		Response response = given()
								.baseUri("https://api.example.com") // 🔁 replace with real base URI
							.when()
								.get("/user/details") // 🔁 replace with real endpoint
							.then()
								.statusCode(200)
								.extract().response();

		// 2️⃣ Convert ACTUAL API response to JsonPath
		JsonPath actualJson = response.jsonPath();

		// 3️⃣ Load EXPECTED JSON from file
		File expectedFile = new File(".\\src\\test\\resources\\complex.json");
		JsonPath expectedJson = new JsonPath(expectedFile);

		// 4️⃣ Validate top-level fields
		assertThat(actualJson.getString("status"), is(expectedJson.getString("status")));

		// 5️⃣ Validate user basic details
		assertThat(actualJson.getInt("data.userDetails.id"), is(expectedJson.getInt("data.userDetails.id")));

		assertThat(actualJson.getString("data.userDetails.name"), is(expectedJson.getString("data.userDetails.name")));

		assertThat(actualJson.getString("data.userDetails.email"), is(expectedJson.getString("data.userDetails.email")));

		// 6️⃣ Validate phone numbers
		assertThat(actualJson.getString("data.userDetails.phoneNumbers[0].number"), is(expectedJson.getString("data.userDetails.phoneNumbers[0].number")));

		assertThat(actualJson.getString("data.userDetails.phoneNumbers[0].type"), is(expectedJson.getString("data.userDetails.phoneNumbers[0].type")));

		// 7️⃣ Validate geo location
		assertThat(actualJson.getDouble("data.userDetails.address.geo.latitude"), is(expectedJson.getDouble("data.userDetails.address.geo.latitude")));

		assertThat(actualJson.getDouble("data.userDetails.address.geo.longitude"), is(expectedJson.getDouble("data.userDetails.address.geo.longitude")));

		// 8️⃣ Validate preferences
		assertThat(actualJson.getBoolean("data.userDetails.preferences.notifications"), is(expectedJson.getBoolean("data.userDetails.preferences.notifications")));

		assertThat(actualJson.getString("data.userDetails.preferences.theme"), is(expectedJson.getString("data.userDetails.preferences.theme")));
	}
}
