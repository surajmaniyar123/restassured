package day6_Validations;

import static io.restassured.RestAssured.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import java.util.List;

import org.testng.annotations.Test;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class Assert_that {

    @Test
    public void approach3_AssertThat_WithWhen() {

        // Send request using .when()
        Response response = given()
                            // you can add headers, params here if needed
                            .when()
                                .get("https://run.mocky.io/v3/your-mock-id"); 

        // Status code
        assertThat(response.getStatusCode(), is(200));
        
        
        
        // JsonPath json = new JsonPath(response.asString());    // Create JsonPath manually
        JsonPath json = response.jsonPath(); 

        // =======================
        // User Details Validation
        // =======================
        assertThat(json.getString("status"), equalTo("success"));
        assertThat(json.getInt("data.userDetails.id"), equalTo(12345));
        assertThat(json.getString("data.userDetails.name"), equalTo("John Doe"));
        assertThat(json.getString("data.userDetails.email"), equalTo("john.doe@example.com"));
        assertThat(json.getString("data.userDetails.phoneNumbers[0].type"), equalTo("home"));
        assertThat(json.getString("data.userDetails.phoneNumbers[0].number"), equalTo("123-456-7890"));
        assertThat(json.getDouble("data.userDetails.address.geo.latitude"), closeTo(39.7817, 0.0001));
        assertThat(json.getDouble("data.userDetails.address.geo.longitude"), closeTo(-89.6501, 0.0001));
        assertThat(json.getBoolean("data.userDetails.preferences.notifications"), is(true));
        assertThat(json.getString("data.userDetails.preferences.theme"), equalTo("dark"));

        // =======================
        // Recent Orders Validation
        // =======================
        assertThat(json.getList("data.recentOrders").size(), equalTo(2));
        assertThat(json.getInt("data.recentOrders[0].orderId"), equalTo(101));
        assertThat(json.getDouble("data.recentOrders[0].totalAmount"), closeTo(1226.49, 0.01));
        assertThat(json.getString("data.recentOrders[0].items[1].name"), equalTo("Mouse"));
        assertThat(json.getDouble("data.recentOrders[0].items[1].price"), closeTo(25.50, 0.01));
        assertThat(json.getList("data.recentOrders[1].items").size(), equalTo(1));
        assertThat(json.getString("data.recentOrders[1].items[0].name"), equalTo("Smartphone"));
        assertThat(json.getDouble("data.recentOrders[1].items[0].price"), closeTo(799.99, 0.01));

        // =======================
        // Preferences & Metadata Validation
        // =======================
        List<String> languages = json.getList("data.userDetails.preferences.languages");
        assertThat(languages, hasItems("English", "Spanish", "French"));
        assertThat(json.getString("meta.requestId"), equalTo("abc123xyz"));
        assertThat(json.getInt("meta.responseTimeMs"), lessThan(300));
    }
}
