package day6_Validations;

import static io.restassured.RestAssured.*;

import java.util.List;

import org.testng.Assert;
import org.testng.annotations.Test;

import io.restassured.path.json.JsonPath;

public class HybridExtractValues {

    @Test
    void approach4_HybridExtract() {

        JsonPath json = given()
            .when()
                .get("https://run.mocky.io/v3/your-mock-id")
            .then()
                .statusCode(200)
                .extract()
                .jsonPath();

        // User Details
        Assert.assertEquals(json.getString("status"), "success");
        Assert.assertEquals(json.getInt("data.userDetails.id"), 12345);
        Assert.assertEquals(json.getString("data.userDetails.name"), "John Doe");
        Assert.assertEquals(json.getString("data.userDetails.email"), "john.doe@example.com");
        Assert.assertEquals(json.getString("data.userDetails.phoneNumbers[0].number"), "123-456-7890");
        Assert.assertEquals(json.getDouble("data.userDetails.address.geo.latitude"), 39.7817, 0.0001);
        Assert.assertEquals(json.getDouble("data.userDetails.address.geo.longitude"), -89.6501, 0.0001);
        Assert.assertTrue(json.getBoolean("data.userDetails.preferences.notifications"));
        Assert.assertEquals(json.getString("data.userDetails.preferences.theme"), "dark");

        // Recent Orders
        Assert.assertEquals(json.getList("data.recentOrders").size(), 2);
        Assert.assertEquals(json.getInt("data.recentOrders[0].orderId"), 101);
        Assert.assertEquals(json.getDouble("data.recentOrders[0].totalAmount"), 1226.49, 0.01);
        Assert.assertEquals(json.getString("data.recentOrders[0].items[1].name"), "Mouse");
        Assert.assertEquals(json.getDouble("data.recentOrders[0].items[1].price"), 25.50, 0.01);
        Assert.assertEquals(json.getList("data.recentOrders[1].items").size(), 1);
        Assert.assertEquals(json.getString("data.recentOrders[1].items[0].name"), "Smartphone");
        Assert.assertEquals(json.getDouble("data.recentOrders[1].items[0].price"), 799.99, 0.01);

        // Preferences & Metadata
        List<String> languages = json.getList("data.userDetails.preferences.languages");
        Assert.assertTrue(languages.contains("English"));
        Assert.assertTrue(languages.contains("Spanish"));
        Assert.assertTrue(languages.contains("French"));
        Assert.assertEquals(json.getString("meta.requestId"), "abc123xyz");
        Assert.assertTrue(json.getInt("meta.responseTimeMs") < 300);
    }
}
