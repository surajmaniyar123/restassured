package day4;

import org.testng.annotations.Test;

import io.restassured.http.Header;
import io.restassured.http.Headers;
import io.restassured.response.Response;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

/**
 * This class demonstrates how to handle and validate HTTP headers in API
 * responses using REST Assured.
 * 
 * Covered topics: 1. Logging and validating response headers 2. Extracting
 * specific header values 3. Retrieving and iterating through all headers
 */
public class HeadersTest {

	@Test
	void testHeadersInResponse() {

		// Step 1️⃣: Send GET request to Google's homepage
		// -----------------------------------------------
		// Google responds with multiple headers (e.g., Content-Type, Server,
		// X-Frame-Options, etc.)

		Response response = given()
							.when()
								.get("https://www.google.com/") // Send GET request
							.then()
								.log().headers() // Log all response headers for inspection
								.statusCode(200) // Verify the HTTP response code is 200 OK
								.header("Content-Type", containsString("text/html")) // Check that content is HTML
								.header("Content-Encoding", notNullValue()) // Ensure header is present
								.header("Content-Encoding", equalTo("gzip")) // Confirm content is compressed with gzip
								.header("Content-Encoding", "gzip") // Equivalent validation (simplified)
								.header("X-Frame-Options", equalTo("SAMEORIGIN")) // Prevents embedding in other sites (security header)
								.header("Server", equalTo("gws")) // Verify server type (Google Web Server)
								.extract().response(); // Extract the full response for further use

		// Step 2️⃣: Extract a specific header value
		// -----------------------------------------
		// Fetch and print the value of the "Content-Type" header from the response.
		
		String headerValue = response.getHeader("Content-Type");
		System.out.println("Value of 'Content-Type' header: " + headerValue);

		// Step 3️⃣: Extract all headers
		// -----------------------------
		// REST Assured provides a Headers object that contains all headers as Header objects.
		
		Headers headers = response.getHeaders();

		// Step 4️⃣: Print each header name and its corresponding value
		// ------------------------------------------------------------
		for (Header h : headers) {
			System.out.println(h.getName() + " ==> " + h.getValue());
		}
	}
}
