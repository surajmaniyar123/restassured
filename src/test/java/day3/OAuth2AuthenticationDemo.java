package day3;

import org.testng.annotations.Test;
import static io.restassured.RestAssured.given;

/*
This example demonstrates how to perform OAuth 2.0 Authentication using RestAssured.
The flow is as follows:

1) Obtain OAuth credentials (manual process):
   - Client ID
   - Client Secret

2) Get an Access Token using POST request:
   POST https://api.imgur.com/oauth2/token
   Required parameters:
      - client_id
      - client_secret
      - grant_type
      - authorization_code
      - redirect_uri

   Once the request is successful, you will receive an access token.

3) Use the access token in subsequent API requests (GET, POST, etc.).
*/

public class OAuth2AuthenticationDemo {

	@Test
	void verifyOAuth2Authentication() {

		// Step 1: Define OAuth 2.0 credentials and parameters
		String clientId = "cff93d24167b033"; // Your application's Client ID
		String clientSecret = "ac85c1a5bc7e775cfbcd5b40188a2aa3b9be68d2"; // Your application's Client Secret
		String redirectUri = "https://www.getpostman.com/oauth2/callback"; // The redirect URI registered with Imgur
		String grantType = "authorization_code"; // Grant type used for OAuth flow
		String authorizationCode = "4c91c2e0de4cc9fa95ddb6e3fd0df11cc29ef739"; // Replace with a valid Authorization code
																				
		// Step 2: Request Access Token using POST method
		// Send required parameters in the form body
		
		String token = given()
							.formParam("client_id", clientId)
							.formParam("client_secret", clientSecret)
							.formParam("grant_type", grantType)
							.formParam("code", authorizationCode)
							.formParam("redirect_uri", redirectUri)
						.when()
							.post("https://api.imgur.com/oauth2/token") // Token endpoint
						.then()
							.statusCode(200) // Expecting HTTP 200 OK on success
							.extract().jsonPath().getString("access_token"); // Extract the 'access_token' value from the JSON response
																	

		// Print the generated token to console (for debugging)
		System.out.println("Generated token: " + token);

		// Step 3: Use the Access Token to call a protected API endpoint
		// The token is passed as a Bearer token using OAuth2 authentication
		
						given()
							.auth().oauth2(token) // Attach the access token in the Authorization header
						.when()
							.get("https://api.imgur.com/3/account/me/images") // Sample API to fetch user images
						.then()
							.statusCode(200) // Expect 200 OK response
							.log().body(); // Print the response body to console for verification
	}

}
