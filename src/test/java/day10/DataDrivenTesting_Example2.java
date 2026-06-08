package day10;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class DataDrivenTesting_Example2 {

    // 1️⃣ Define your data sets
    @DataProvider(name = "userData")
    public Object[][] getData() {
        return new Object[][] {
            { 12345, "John Doe", "john.doe@example.com" },
            { 67890, "Jane Smith", "jane.smith@example.com" }
        };
    }

    // 2️⃣ Use the data in your test
    @Test(dataProvider = "userData")
    public void testUserDetailsAPI(int userId, String expectedName, String expectedEmail) {

        given()
            .pathParam("userId", userId) // example if API uses path param
        .when()
            .get("https://run.mocky.io/v3/your-mock-id/{userId}")
        .then()
            .statusCode(200)
            .body("data.userDetails.id", equalTo(userId))
            .body("data.userDetails.name", equalTo(expectedName))
            .body("data.userDetails.email", equalTo(expectedEmail));
    }
}
