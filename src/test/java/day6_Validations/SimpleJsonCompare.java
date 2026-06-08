package day6_Validations;

// -------------------- IMPORTS --------------------
import static io.restassured.RestAssured.given; // For BDD-style REST API calls (given/when/then)
import java.nio.file.Files;  // To read files from the filesystem
import java.nio.file.Paths;  // To specify the path of the file
import org.json.JSONObject;  // To handle JSON objects
import org.testng.Assert;     // For assertions (optional here)
import org.testng.annotations.Test; // To mark test methods
import io.restassured.response.Response; // To store API response

// -------------------- CLASS --------------------
public class SimpleJsonCompare {

    // -------------------- AUTH TOKEN & BASE URL --------------------
    // Auth token to access the API
    private static final String AUTH_TOKEN = 
        "Bearer 24615c7dd917c0c504514080843047e63c162015d34c10c92dd822f786813143";

    // Base URL for the API endpoint
    private static final String BASE_URL = "https://simple-books-api.glitch.me/orders/1";

    // -------------------- TEST METHOD --------------------
    @Test
    public void validateJsonResponse() throws Exception {
        // 1️⃣ Read expected JSON from file
        // Files.readAllBytes reads the entire file as a byte array
        // new String(...) converts the byte array to a String
        String expectedStr = new String(Files.readAllBytes(Paths.get("./testdata/expectedResponse.json")));

        // Convert JSON string to JSONObject for easier key/value access
        JSONObject expectedJson = new JSONObject(expectedStr);

        // 2️⃣ Call API using RestAssured BDD style
        Response response = given()
                                .header("Authorization", AUTH_TOKEN) // Add Authorization header
                            .when()
                                .get(BASE_URL) // Make GET request
                            .then()
                                .statusCode(200) // Validate HTTP 200 OK response
                                .extract().response(); // Extract full response object

        // Convert API response body (JSON string) into JSONObject
        JSONObject actualJson = new JSONObject(response.getBody().asString());

        // 3️⃣ Compare JSON key by key
        for (String key : expectedJson.keySet()) {
            // Get the expected value for this key
            Object expectedValue = expectedJson.get(key);

            // Get the actual value from the response
            // opt() returns null if the key does not exist in the actual JSON
            Object actualValue = actualJson.opt(key);  

            // Check if key exists in actual response
            if (actualValue == null) {
                System.out.println("❌ Missing key: " + key);
            } 
            // If key exists, check if values match
            else if (!expectedValue.equals(actualValue)) {
                System.out.println("❌ Mismatch for key '" + key + "': expected = "
                        + expectedValue + ", actual = " + actualValue);
            } 
            // Values match
            else {
                System.out.println("✅ Match for key '" + key + "': " + actualValue);
            }
        }
    }
}
