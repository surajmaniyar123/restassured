package day4;

import org.testng.annotations.Test; // Import TestNG @Test
import io.restassured.http.Cookie; // Import Rest Assured Cookie class
import io.restassured.response.Response; // Import Rest Assured Response class
import static io.restassured.RestAssured.*; // Import Rest Assured statically
import static org.hamcrest.Matchers.*; // Import Hamcrest matchers
import java.util.Map; // Import Map

public class CookiesTest {

	@Test
	void testCookiesInResponse() {

		// Step 1️⃣: Send a GET request to Google's homepage 
		// This will return an HTTP response containing multiple cookies (e.g., AEC, NID, 1P_JAR, etc.)
		Response response = given()
							.when()
								.get("https://www.google.com/") // Send GET request
							.then()
								.log().cookies() // Log all cookies from the response for inspection
								.statusCode(200) // Validate that the request was successful
								.cookie("AEC", notNullValue()) // Check that the cookie "AEC" exists and is not null
								.extract().response(); // Extract the full response for further cookie operations

		// Step 2️⃣: Extract a specific cookie value 
		// Fetch the value of the "AEC" cookie from the response
		
		String cookieValue = response.getCookie("AEC");
		System.out.println("Value of 'AEC' Cookie: " + cookieValue); // Print cookie value

		// Step 3️⃣: Extract all cookies as a Map 
		// REST Assured returns cookies as key-value pairs
		
		Map<String, String> allCookies = response.getCookies();
		System.out.println("All cookies received: " + allCookies); // Print all cookies

		// Step 4️⃣: Loop through and print each cookie with its value 
		 // Iterate overMap and print each cookie
		
		for (String key : allCookies.keySet()) {
			System.out.println(key + " : " + allCookies.get(key)); // Print each cookie key and value
		}

		// Step 5️⃣: Get detailed information about a specific cookie 
		// Cookie object contains metadata (expiry, secure flag, value)
		
		Cookie cookie_info = response.getDetailedCookie("AEC");

		System.out.println("Has Expiry Date? " + cookie_info.hasExpiryDate()); // Print if cookie has expiry
		System.out.println("Expiry Date: " + cookie_info.getExpiryDate()); // Print expiry date
		System.out.println("Has Value? " + cookie_info.hasValue()); // Print if cookie has value
		System.out.println("Cookie Value: " + cookie_info.getValue()); // Print cookie value
		System.out.println("Is Secure? " + cookie_info.isSecured()); // Print if cookie is secure
	}
}
