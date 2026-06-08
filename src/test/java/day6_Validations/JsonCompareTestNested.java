package day6_Validations;  

// -------------------- IMPORTS --------------------
import static io.restassured.RestAssured.given; // For BDD-style REST API calls (given/when/then)
import java.nio.file.Files;  // To read files from the file system
import java.nio.file.Paths;  // To specify file paths
import org.json.JSONArray;   // To handle JSON arrays
import org.json.JSONObject;  // To handle JSON objects
import org.testng.annotations.Test;  // TestNG annotation for test methods
import io.restassured.response.Response; // To store API response

// -------------------- CLASS --------------------
public class JsonCompareTestNested {

    // -------------------- AUTH TOKEN & BASE URL --------------------
    // Auth token to access API
    private static final String AUTH_TOKEN =
        "Bearer 24615c7dd917c0c504514080843047e63c162015d34c10c92dd822f786813143";

    // Base URL for the API endpoint
    private static final String BASE_URL = "https://simple-books-api.glitch.me/orders/1";

    // -------------------- TEST METHOD --------------------
    @Test
    public void verifyApiResponseAgainstJsonFile() throws Exception {
        // 1️⃣ Read expected JSON file from disk
        // Files.readAllBytes reads the whole file as a byte array
        // new String(...) converts bytes to string
        String expectedStr = new String(Files.readAllBytes(Paths.get("./testdata/expectedResponse.json")));
        
        // Convert the string to a JSONObject so we can easily access keys and values
        JSONObject expected = new JSONObject(expectedStr);

        // 2️⃣ Call API using RestAssured BDD style
        Response response = given()
                                .header("Authorization", AUTH_TOKEN) // Set Authorization header
                            .when()
                                .get(BASE_URL) // GET request to the API
                            .then()
                                .statusCode(200) // Verify HTTP status code is 200 OK
                                .extract().response(); // Extract the response into Response object

        // Convert API response body (JSON string) into JSONObject
        JSONObject actual = new JSONObject(response.getBody().asString());

        // 3️⃣ Compare expected JSON vs actual JSON
        // Supports nested objects and arrays
        compareJsonObjects(expected, actual, ""); // "" is the parentKey, initially empty
    }

    // -------------------- METHOD TO COMPARE JSON OBJECTS --------------------
    // Recursively compares JSON objects
    private void compareJsonObjects(JSONObject expected, JSONObject actual, String parentKey) {
        // Loop through all keys in expected JSON
        for (String key : expected.keySet()) {
            // Build full key path for better reporting (e.g., order.customer.name)
            String fullKey = parentKey.isEmpty() ? key : parentKey + "." + key;

            // Check if actual JSON contains this key
            if (!actual.has(key)) {
                System.out.println("❌ Missing key: " + fullKey);
                continue; // Skip further checks for this key
            }

            // Get values for the key from both JSONs
            Object expVal = expected.get(key);
            Object actVal = actual.get(key);

            // If value is another JSON object, recursively compare
            if (expVal instanceof JSONObject) {
                compareJsonObjects((JSONObject) expVal, (JSONObject) actVal, fullKey);
            } 
            // If value is a JSON array, compare arrays
            else if (expVal instanceof JSONArray) {
                compareJsonArrays((JSONArray) expVal, (JSONArray) actVal, fullKey);
            } 
            // If value is primitive (string, number, boolean)
            else {
                if (!expVal.equals(actVal)) { // Compare values
                    System.out.println("❌ Mismatch for '" + fullKey + "': expected = "
                            + expVal + ", actual = " + actVal);
                } else {
                    System.out.println("✅ Match for '" + fullKey + "': value = " + actVal);
                }
            }
        }
    }

    // -------------------- METHOD TO COMPARE JSON ARRAYS --------------------
    private void compareJsonArrays(JSONArray expected, JSONArray actual, String parentKey) {
        // Compare elements index by index
        int len = Math.min(expected.length(), actual.length()); // Compare up to shorter array length
        for (int i = 0; i < len; i++) {
            Object expVal = expected.get(i);
            Object actVal = actual.get(i);

            // Build array key path like books[0].title
            String arrayKey = parentKey + "[" + i + "]";

            if (expVal instanceof JSONObject) { // If element is JSONObject, recurse
                compareJsonObjects((JSONObject) expVal, (JSONObject) actVal, arrayKey);
            } 
            else if (!expVal.equals(actVal)) { // Compare primitive elements
                System.out.println("❌ Mismatch for '" + arrayKey + "': expected = "
                        + expVal + ", actual = " + actVal);
            } else {
                System.out.println("✅ Match for '" + arrayKey + "': value = " + actVal);
            }
        }

        // Check if array lengths mismatch
        if (expected.length() != actual.length()) {
            System.out.println("❌ Array length mismatch for '" + parentKey
                    + "': expected = " + expected.length() + ", actual = " + actual.length());
        }
    }
}
