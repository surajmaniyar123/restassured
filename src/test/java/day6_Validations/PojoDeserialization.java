package day6_Validations;

import static io.restassured.RestAssured.*;

import java.util.List;

import org.testng.Assert;
import org.testng.annotations.Test;

public class PojoDeserialization {

    @Test
    void approach5_POJO() {

        ApiResponse res = given()
            .when()
                .get("https://run.mocky.io/v3/your-mock-id")
            .then()
                .statusCode(200)
                .extract()
                .as(ApiResponse.class);

        // User Details
        Assert.assertEquals(res.status, "success");
        Assert.assertEquals(res.data.userDetails.id, 12345);
        Assert.assertEquals(res.data.userDetails.name, "John Doe");
        Assert.assertEquals(res.data.userDetails.email, "john.doe@example.com");
        Assert.assertEquals(res.data.userDetails.phoneNumbers.get(0).type, "home");
        Assert.assertEquals(res.data.userDetails.phoneNumbers.get(0).number, "123-456-7890");
        Assert.assertEquals(res.data.userDetails.address.geo.latitude, 39.7817, 0.0001);
        Assert.assertEquals(res.data.userDetails.address.geo.longitude, -89.6501, 0.0001);
        Assert.assertTrue(res.data.userDetails.preferences.notifications);
        Assert.assertEquals(res.data.userDetails.preferences.theme, "dark");

        // Recent Orders
        Assert.assertEquals(res.data.recentOrders.size(), 2);
        Assert.assertEquals(res.data.recentOrders.get(0).orderId, 101);
        Assert.assertEquals(res.data.recentOrders.get(0).totalAmount, 1226.49, 0.01);
        Assert.assertEquals(res.data.recentOrders.get(0).items.get(1).name, "Mouse");
        Assert.assertEquals(res.data.recentOrders.get(0).items.get(1).price, 25.50, 0.01);
        Assert.assertEquals(res.data.recentOrders.get(1).items.size(), 1);
        Assert.assertEquals(res.data.recentOrders.get(1).items.get(0).name, "Smartphone");
        Assert.assertEquals(res.data.recentOrders.get(1).items.get(0).price, 799.99, 0.01);

        // Preferences & Metadata
        List<String> languages = res.data.userDetails.preferences.languages;
        Assert.assertTrue(languages.contains("English"));
        Assert.assertTrue(languages.contains("Spanish"));
        Assert.assertTrue(languages.contains("French"));
        Assert.assertEquals(res.meta.requestId, "abc123xyz");
        Assert.assertTrue(res.meta.responseTimeMs < 300);
    }
}
