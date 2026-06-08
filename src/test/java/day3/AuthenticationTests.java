package day3;

import org.testng.annotations.Test;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

/**
 * This class demonstrates different types of authentication methods supported
 * in REST Assured using real-world public APIs.
 * 
 * Types covered: 1. Basic Authentication 2. Basic Preemptive Authentication 3.
 * Digest Authentication 4. Bearer Token Authentication 5. API Key
 * Authentication
 */
public class AuthenticationTests {

	// 1️⃣ Basic Authentication
	// ------------------------
	// In basic authentication, credentials (username and password)
	// are sent with each request encoded in Base64 format.
	// The server validates them and returns a response.

	@Test
	void verifyBasicAuth() 
	{
				given()
					.auth().basic("postman", "password") // Attach username and password
				.when()
					.get("https://postman-echo.com/basic-auth") // Public API for testing basic auth
				.then()
					.statusCode(200) // Verify HTTP 200 OK
					.body("authenticated", equalTo(true)) // Assert JSON response field
					.log().body(); // Log response body for reference
	}	

	// 2️⃣ Basic Preemptive Authentication
	// ----------------------------------
	// Preemptive authentication sends credentials *before* the server
	// explicitly asks for them, which avoids one extra round trip (401 challenge).

	// @Test
	void verifyPreemptiveAuth() 
	{
				given()
					.auth().preemptive().basic("postman", "password") // Send credentials upfront
				.when()
					.get("https://postman-echo.com/basic-auth")
				.then()
					.statusCode(200)
					.body("authenticated", equalTo(true))
					.log().body();
	}

	// 3️⃣ Digest Authentication
	// -------------------------
	// Digest authentication is more secure than Basic Authentication.
	// It uses a hashing technique so that credentials are not sent in plain text.

	// @Test
	void verifyDigestAuth() 
	{
				given()
					.auth().digest("postman", "password") // Digest-based authentication
				.when()
					.get("https://postman-echo.com/basic-auth")
				.then()
					.statusCode(200)
					.body("authenticated", equalTo(true))
					.log().body();
	}

	// 4️⃣ Bearer Token Authentication
	// -------------------------------
	// Commonly used in OAuth 2.0, a Bearer Token is sent in the Authorization header
	// This token gives access to protected resources without needing username/password.

	// @Test
	void verifyTokenAuth() 
	{
		//String bearerToken = "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"; // Replace with a valid GitHub token

				given()
			//		.header("Authorization", "Bearer " + bearerToken) // Set Authorization header
				.when()
					.get("https://api.github.com/user/repos") // Access user's repositories
				.then()
					.statusCode(200)
					.log().body(); // Print response for validation
	}

	// 5️⃣ API Key Authentication
	// --------------------------
	// Many public APIs (like OpenWeather, Google Maps, etc.) use API keys.
	// The key is usually passed as a query parameter or header.

	@Test
	void verifyAPIKeyAuth() 
	{
				given()
					.queryParam("q", "Delhi") // Query parameter for city name
					.queryParam("appid", "fe9c5cddb7e01d747b4611c3fc9eaf2c") // API key for OpenWeatherMap
				.when()
					.get("https://api.openweathermap.org/data/2.5/weather") // Endpoint for current weather
				.then()
					.statusCode(200) // Verify request succeeded
					.log().body(); // Log response for inspection
	}
}
