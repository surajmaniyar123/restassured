package day10;  // Defines the package location for this class

import static io.restassured.RestAssured.given;  // Static import for RestAssured methods
import java.util.Map;  // Import for using Map in JSON data provider
import org.json.JSONObject;  // Import for creating JSON objects
import org.testng.annotations.Test;  // TestNG annotation for test methods

public class DataDrivenTesting {

    // -------------------- AUTHENTICATION & BASE URL --------------------
    private static final String AUTH_TOKEN = "Bearer 24615c7dd917c0c504514080843047e63c162015d34c10c92dd822f786813143";
    private static final String BASE_URL = "https://simple-books-api.glitch.me/orders";

    // -------------------- TEST WITH EXCEL DATA --------------------
    @Test(dataProvider="excelDataProvider", dataProviderClass=DataProviders.class)
    public void testWithExcelData(String bookId, String customerName) {
        // Call generic method to submit and delete order using Excel data
        testSubmitAndDeleteOrder(bookId, customerName);
    }

    // -------------------- TEST WITH JSON DATA --------------------
    @Test(dataProvider="jsonDataProvider", dataProviderClass=DataProviders.class)
    public void testWithJsonData(Map<String, String> data) {
        // Call generic method using values from JSON Map
        testSubmitAndDeleteOrder(data.get("BookID"), data.get("CustomerName"));
    }

    // -------------------- TEST WITH CSV DATA --------------------
    @Test(dataProvider="csvDataProvider", dataProviderClass=DataProviders.class)
    public void testWithCSVData(String bookId, String customerName) {
        // Call generic method to submit and delete order using CSV data
        testSubmitAndDeleteOrder(bookId, customerName);
    }

    // -------------------- GENERIC METHOD TO SUBMIT AND DELETE ORDER --------------------
    void testSubmitAndDeleteOrder(String bookId, String customerName) {
        // Create JSON request body for order submission
        JSONObject requestBody = new JSONObject();
        requestBody.put("bookId", Integer.parseInt(bookId));  // Convert bookId string to integer
        requestBody.put("customerName", customerName);        // Add customer name to JSON body

        // -------------------- SUBMIT ORDER --------------------
        String orderId = given()
                .contentType("application/json")       // Set request content type
                .header("Authorization", AUTH_TOKEN)   // Add Authorization header
                .body(requestBody.toString())          // Attach JSON body
            .when()
                .post(BASE_URL)                        // POST request to create order
            .then()
                .statusCode(201)                       // Verify successful creation
                .log().body()                          // Log response body for debugging
                .extract().jsonPath().getString("orderId");  // Extract orderId from response

        // -------------------- DELETE ORDER --------------------
        given()
            .header("Authorization", AUTH_TOKEN)  // Add Authorization header
            .pathParam("orderId", orderId)        // Set path parameter for deletion
        .when()
            .delete(BASE_URL + "/{orderId}")      // DELETE request to remove order
        .then()
            .statusCode(204);                     // Verify successful deletion (No Content)
    }
}
