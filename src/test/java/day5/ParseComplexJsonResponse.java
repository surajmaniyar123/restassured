package day5;                                           // 👉 Declares that this class belongs to package 'day5'

// 👉 Importing JsonPath from RestAssured to extract values from JSON easily
import io.restassured.path.json.JsonPath;

// 👉 Importing assertion library (Hamcrest)
import static org.hamcrest.MatcherAssert.assertThat; // 👉 Used for assertions
import static org.hamcrest.Matchers.*;                // 👉 Provides matchers like is(), lessThan()

// 👉 Importing Java file handling classes
import java.io.File;                                  // 👉 Represents a file from disk
import java.io.FileNotFoundException;                // 👉 Exception thrown if file missing
import java.io.FileReader;                           // 👉 Used to read file content

// 👉 Importing JSON libraries to parse JSON
import org.json.JSONObject;                           // 👉 Represents a JSON object
import org.json.JSONTokener;                          // 👉 Reads raw text and converts into JSON tokens

// 👉 Importing TestNG annotation to mark test methods
import org.testng.annotations.Test;

/**
 * 👉 This class demonstrates how to parse and validate a complex JSON response.
 * 👉 It loads a JSON file, reads it, converts to JSONObject, then uses JsonPath
 * 👉 to extract values for validation using Hamcrest assertions.
 */
public class ParseComplexJsonResponse {                // 👉 Class declaration

    /**
     * 👉 Utility method to load and parse the JSON file (complex.json).
     * 👉 Returns the JSON response as a JSONObject.
     */
    JSONObject getJsonResponse() {                     // 👉 Method starts

        File myfile = new File(".\\src\\test\\resources\\complex.json"); // 👉 Create file object pointing to complex.json

        FileReader fileReader = null;                 // 👉 Declare FileReader variable

        try {                                         // 👉 Try to open file
            fileReader = new FileReader(myfile);     // 👉 Attempt to open file for reading
        } catch (FileNotFoundException e) {          // 👉 Handle file not found
            e.printStackTrace();                     // 👉 Print stack trace if file missing
        }

        JSONTokener jsonTokener = new JSONTokener(fileReader); // 👉 Convert file content into JSON tokens

        JSONObject jsonResponse = new JSONObject(jsonTokener); // 👉 Create JSON object from tokens

        return jsonResponse;                          // 👉 Return the full JSON object
    }
    
    /*
    public class ParseComplexJsonResponse {   // Class declaration

        JsonPath getJsonResponse() {   // Utility method to load JSON
            return new JsonPath(new File(".\\src\\test\\resources\\complex.json"));
        }
        
        */

    /**
     * 👉 Test #1: Validate top-level user details and response status.
     * 👉 Checks: Status, User ID, name, email, Phone numbers, Address, Preferences
     */
    @Test(priority = 1)                               // 👉 TestNG annotation: this test executes first
    public void testUserDetailsValidation() {         // 👉 Method starts

        JsonPath jsonPath = new JsonPath(getJsonResponse().toString()); // 👉 Convert JSONObject into JsonPath
     
        //   JsonPath jsonPath = getJsonResponse(); 
        // Alternative way  getJsonResponse() directly returns JsonPath if we use line 53 to 57

        String status = jsonPath.getString("status"); // 👉 Extract status value from JSON
        assertThat(status, is("success"));            // 👉 Validate that status == "success"

        int id = jsonPath.getInt("data.userDetails.id");             // 👉 Extract user ID
        String name = jsonPath.getString("data.userDetails.name");   // 👉 Extract user name
        String email = jsonPath.getString("data.userDetails.email"); // 👉 Extract user email

        assertThat(id, is(12345));                     // 👉 Validate user ID
        assertThat(name, is("John Doe"));              // 👉 Validate name
        assertThat(email, is("john.doe@example.com"));// 👉 Validate email

        String homePhone = jsonPath.getString("data.userDetails.phoneNumbers[0].number"); // 👉 Extract home phone number
        String homePhoneType = jsonPath.getString("data.userDetails.phoneNumbers[0].type"); // 👉 Extract type

        assertThat(homePhone, is("123-456-7890"));   // 👉 Validate number
        assertThat(homePhoneType, is("home"));       // 👉 Validate type

        double latitude = jsonPath.getDouble("data.userDetails.address.geo.latitude");   // 👉 Extract latitude
        double longitude = jsonPath.getDouble("data.userDetails.address.geo.longitude"); // 👉 Extract longitude

        assertThat(latitude, is(39.7817));          // 👉 Validate latitude
        assertThat(longitude, is(-89.6501));        // 👉 Validate longitude

        boolean notifications = jsonPath.getBoolean("data.userDetails.preferences.notifications"); // 👉 Extract notification preference
        String theme = jsonPath.getString("data.userDetails.preferences.theme");                   // 👉 Extract theme

        assertThat(notifications, is(true));        // 👉 Validate notifications preference
        assertThat(theme, is("dark"));              // 👉 Validate theme
    }

    /**
     * 👉 Test #2: Validate recent orders.
     * 👉 Checks: Number of orders, First order details, Item details inside orders
     */
    @Test(priority = 2)                               // 👉 This test runs second
    public void testRecentOrdersValidation() {        // 👉 Method starts

        JsonPath jsonPath = new JsonPath(getJsonResponse().toString()); // 👉 Convert JSON to JsonPath

        int totalOrders = jsonPath.getInt("data.recentOrders.size()"); // 👉 Get total number of orders
        assertThat(totalOrders, is(2));             // 👉 Should be exactly 2

        int firstOrderId = jsonPath.getInt("data.recentOrders[0].orderId"); // 👉 Extract first order ID
        double firstOrderTotal = jsonPath.getDouble("data.recentOrders[0].totalAmount"); // 👉 Extract first order amount

        assertThat(firstOrderId, is(101));          // 👉 Validate ID
        assertThat(firstOrderTotal, is(1226.49));   // 👉 Validate amount

        String secondItemName = jsonPath.getString("data.recentOrders[0].items[1].name"); // 👉 Extract name of second item
        double secondItemPrice = jsonPath.getDouble("data.recentOrders[0].items[1].price"); // 👉 Extract price

        assertThat(secondItemName, is("Mouse"));    // 👉 Validate item name
        assertThat(secondItemPrice, is(25.50));    // 👉 Validate item price

        int secondOrderItems = jsonPath.getInt("data.recentOrders[1].items.size()"); // 👉 Count items in second order
        String secondOrderItemName = jsonPath.getString("data.recentOrders[1].items[0].name"); // 👉 First item name
        double secondOrderItemPrice = jsonPath.getDouble("data.recentOrders[1].items[0].price"); // 👉 First item price

        assertThat(secondOrderItems, is(1));        // 👉 Should have only 1 item
        assertThat(secondOrderItemName, is("Smartphone")); // 👉 Validate item name
        assertThat(secondOrderItemPrice, is(799.99));     // 👉 Validate item price
    }

    /**
     * 👉 Test #3: Validate user language preferences and metadata.
     * 👉 Checks: Number of languages, First language value, Metadata details
     */
    @Test(priority = 3)                               // 👉 Third test to run
    public void testPreferencesAndMetadataValidation() { // 👉 Method starts

        JsonPath jsonPath = new JsonPath(getJsonResponse().toString()); // 👉 Convert JSON to JsonPath

        int totalLanguages = jsonPath.getInt("data.userDetails.preferences.languages.size()"); // 👉 Count languages
        assertThat(totalLanguages, is(3));           // 👉 Should be 3 languages

        String firstLanguage = jsonPath.getString("data.userDetails.preferences.languages[0]"); // 👉 Extract first language
        assertThat(firstLanguage, is("English"));    // 👉 Validate language

        String requestId = jsonPath.getString("meta.requestId"); // 👉 Extract request ID
        int responseTime = jsonPath.getInt("meta.responseTimeMs"); // 👉 Extract response time

        assertThat(requestId, is("abc123xyz"));      // 👉 Validate request ID
        assertThat(responseTime, lessThan(300));     // 👉 Validate response time < 300ms
    }
}
