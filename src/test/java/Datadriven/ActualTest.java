package Datadriven;

import static io.restassured.RestAssured.given;

import java.util.Map;

import org.json.JSONObject;
import org.testng.annotations.Test;

import Datadriven.DataProviders;

public class ActualTest {

    // -------------------- AUTHENTICATION & BASE URL --------------------
    private static final String AUTH_TOKEN = 
        "Bearer 24615c7dd917c0c504514080843047e63c162015d34c10c92dd822f786813143";
    private static final String BASE_URL = "https://simple-books-api.glitch.me/orders";

    // -------------------- TEST WITH EXCEL DATA --------------------
    @Test(dataProvider = "excelDataProvider", dataProviderClass = DataProviders.class)
    public void testWithExcelData(String bookId, String customerName) {
        submitAndDeleteOrder(bookId, customerName);
    }

    // -------------------- TEST WITH JSON DATA --------------------
    @Test(dataProvider = "jsonDataProvider", dataProviderClass = DataProviders.class)
    public void testWithJsonData(Map<String, String> data) {
        submitAndDeleteOrder(data.get("BookID"), data.get("CustomerName"));
    }

    // -------------------- TEST WITH CSV DATA --------------------
    @Test(dataProvider = "csvDataProvider", dataProviderClass = DataProviders.class)
    public void testWithCSVData(String bookId, String customerName) {
        submitAndDeleteOrder(bookId, customerName);
    }

    // -------------------- GENERIC METHOD TO SUBMIT AND DELETE ORDER --------------------
    private void submitAndDeleteOrder(String bookId, String customerName) {

        // Create JSON request body
        JSONObject requestBody = new JSONObject();
        requestBody.put("bookId", Integer.parseInt(bookId));
        requestBody.put("customerName", customerName);

        // -------------------- SUBMIT ORDER --------------------
        String orderId = given()
                .contentType("application/json")
                .header("Authorization", AUTH_TOKEN)
                .body(requestBody.toString())
            .when()
                .post(BASE_URL)
            .then()
                .statusCode(201)
                .log().body()
                .extract().jsonPath().getString("orderId");

        // -------------------- DELETE ORDER --------------------
        given()
            .header("Authorization", AUTH_TOKEN)
            .pathParam("orderId", orderId)
        .when()
            .delete(BASE_URL + "/{orderId}")
        .then()
            .statusCode(204);
    }
}
