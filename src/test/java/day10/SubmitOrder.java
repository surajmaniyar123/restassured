package day10;  // Defines the package location for this class

import org.testng.annotations.Test;  // Import TestNG annotation for defining test methods
import static io.restassured.RestAssured.given;  // Static import for Rest Assured's given() method
import org.json.JSONObject;  // Import for creating JSON request bodies

public class SubmitOrder {  // Class to test submitting and deleting an order using Rest Assured

    // Authorization token required for accessing the API
    private static final String AUTH_TOKEN = "Bearer 24615c7dd917c0c504514080843047e63c162015d34c10c92dd822f786813143";
    
    // Base URL for the Orders API
    private static final String BASE_URL = "https://simple-books-api.glitch.me/orders";
    
    @Test  // Marks this method as a TestNG test case
    void testSubmitAndDeleteOrder() {
        
        // ------------------ SUBMIT ORDER SECTION ------------------
        
        // Create a JSON object to represent the request body for order submission
        JSONObject requestBody = new JSONObject();
        requestBody.put("bookId", 1);            // Specify the ID of the book to order
        requestBody.put("customerName", "John"); // Specify the name of the customer placing the order
        
        // Send POST request to create a new order
        // Extract the 'orderId' from the response for later use
        String orderId = given()
            .contentType("application/json")        // Set the content type to JSON
            .header("Authorization", AUTH_TOKEN)    // Pass the authorization token in the header
            .body(requestBody.toString())           // Attach the JSON request body
        .when()
            .post(BASE_URL)                         // Send a POST request to submit the order
        .then()
            .statusCode(201)                        // Verify that the response status code is 201 (Created)
            .log().body()                           // Log the response body for debugging or review
            .extract().jsonPath().getString("orderId");  // Extract the orderId from the response JSON
        
        
        // ------------------ DELETE ORDER SECTION ------------------
        
        // Send DELETE request to remove the submitted order using the captured orderId
        given()
            .header("Authorization", AUTH_TOKEN)     // Pass the authorization token in the header
            .pathParam("orderId", orderId)           // Set the dynamic path parameter for order ID
        .when()
            .delete(BASE_URL + "/{orderId}")         // Send DELETE request to delete the order
        .then()
            .statusCode(204);                        // Verify that the response status code is 204 (No Content)
    }
}
