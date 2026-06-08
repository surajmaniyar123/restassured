package day6_Validations;

import org.testng.annotations.Test;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class JsonValidationsBDD {

    @Test
    public void approach2_BDD() {

        given()
        .when()
            .get("https://run.mocky.io/v3/your-mock-id")
        .then()
            .statusCode(200)
            // User Details
            .body("status", equalTo("success"))
            .body("data.userDetails.id", equalTo(12345))
            .body("data.userDetails.name", equalTo("John Doe"))
            .body("data.userDetails.email", equalTo("john.doe@example.com"))
            .body("data.userDetails.phoneNumbers[0].type", equalTo("home"))
            .body("data.userDetails.phoneNumbers[0].number", equalTo("123-456-7890"))
            .body("data.userDetails.address.geo.latitude", closeTo(39.7817f, 0.0001f))
            .body("data.userDetails.address.geo.longitude", closeTo(-89.6501f, 0.0001f))
            .body("data.userDetails.preferences.notifications", equalTo(true))
            .body("data.userDetails.preferences.theme", equalTo("dark"))
            // Recent Orders
            .body("data.recentOrders.size()", equalTo(2))
            .body("data.recentOrders[0].orderId", equalTo(101))
            .body("data.recentOrders[0].totalAmount", closeTo(1226.49f, 0.01f))
            .body("data.recentOrders[0].items[1].name", equalTo("Mouse"))
            .body("data.recentOrders[0].items[1].price", closeTo(25.50f, 0.01f))
            .body("data.recentOrders[1].items.size()", equalTo(1))
            .body("data.recentOrders[1].items[0].name", equalTo("Smartphone"))
            .body("data.recentOrders[1].items[0].price", closeTo(799.99f, 0.01f))
            // Preferences & Metadata
            .body("data.userDetails.preferences.languages", hasItems("English", "Spanish", "French"))
            .body("meta.requestId", equalTo("abc123xyz"))
            .body("meta.responseTimeMs", lessThan(300));
    }
}
