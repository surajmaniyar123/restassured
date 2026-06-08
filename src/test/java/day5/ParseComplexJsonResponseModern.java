package day5;

import io.restassured.path.json.JsonPath;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import java.io.File;

import org.testng.annotations.Test;

/**
 * ✅ Modernized version of ParseComplexJsonResponse
 * - Uses Rest Assured JsonPath directly from File
 * - File creation and JsonPath conversion split into two steps
 * - Cleaner, more readable, maintainable
 */
public class ParseComplexJsonResponseModern {

    // Utility method: returns JsonPath from fixed JSON file path
    private JsonPath getJsonPathFromFile() {
        // Step 1: Create File object
        File file = new File(".\\src\\test\\resources\\complex.json");

        // Step 2: Convert File to JsonPath
        return new JsonPath(file);
    }

    @Test(priority = 1)
    public void testUserDetailsValidation() {
        JsonPath jsonPath = getJsonPathFromFile();

        // Top-level status
        assertThat(jsonPath.getString("status"), is("success"));

        // User basic details
        assertThat(jsonPath.getInt("data.userDetails.id"), is(12345));
        assertThat(jsonPath.getString("data.userDetails.name"), is("John Doe"));
        assertThat(jsonPath.getString("data.userDetails.email"), is("john.doe@example.com"));

        // Phone numbers
        assertThat(jsonPath.getString("data.userDetails.phoneNumbers[0].number"), is("123-456-7890"));
        assertThat(jsonPath.getString("data.userDetails.phoneNumbers[0].type"), is("home"));

        // Address geo
        assertThat(jsonPath.getDouble("data.userDetails.address.geo.latitude"), is(39.7817));
        assertThat(jsonPath.getDouble("data.userDetails.address.geo.longitude"), is(-89.6501));

        // Preferences
        assertThat(jsonPath.getBoolean("data.userDetails.preferences.notifications"), is(true));
        assertThat(jsonPath.getString("data.userDetails.preferences.theme"), is("dark"));
    }

    @Test(priority = 2)
    public void testRecentOrdersValidation() {
        JsonPath jsonPath = getJsonPathFromFile();

        int totalOrders = jsonPath.getInt("data.recentOrders.size()");
        assertThat(totalOrders, is(2));

        // First order
        assertThat(jsonPath.getInt("data.recentOrders[0].orderId"), is(101));
        assertThat(jsonPath.getDouble("data.recentOrders[0].totalAmount"), is(1226.49));

        assertThat(jsonPath.getString("data.recentOrders[0].items[1].name"), is("Mouse"));
        assertThat(jsonPath.getDouble("data.recentOrders[0].items[1].price"), is(25.50));

        // Second order
        assertThat(jsonPath.getInt("data.recentOrders[1].items.size()"), is(1));
        assertThat(jsonPath.getString("data.recentOrders[1].items[0].name"), is("Smartphone"));
        assertThat(jsonPath.getDouble("data.recentOrders[1].items[0].price"), is(799.99));
    }

    @Test(priority = 3)
    public void testPreferencesAndMetadataValidation() {
        JsonPath jsonPath = getJsonPathFromFile();

        int totalLanguages = jsonPath.getInt("data.userDetails.preferences.languages.size()");
        assertThat(totalLanguages, is(3));
        assertThat(jsonPath.getString("data.userDetails.preferences.languages[0]"), is("English"));

        assertThat(jsonPath.getString("meta.requestId"), is("abc123xyz"));
        assertThat(jsonPath.getInt("meta.responseTimeMs"), lessThan(300));
    }
}
