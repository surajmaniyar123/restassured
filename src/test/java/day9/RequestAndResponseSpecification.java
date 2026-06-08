package day9;   // Package declaration

// Importing static methods from RestAssured for simpler request syntax (given(), when(), then())
import static io.restassured.RestAssured.*;
// Importing Hamcrest matchers for readable assertions
import static org.hamcrest.Matchers.*;

// Importing TestNG annotations for test setup and execution
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

// Importing RestAssured builders and specifications for reusable request/response setup
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

/*
 * Example API Endpoints:
 * ----------------------
 * GET  http://localhost:3000/employees           → Fetch all employees
 * GET  http://localhost:3000/employees/1         → Fetch employee with ID 1
 * GET  http://localhost:3000/employees?last_name=King   → Fetch employees by last name
 * GET  http://localhost:3000/employees?first_name=Steven→ Fetch employees by first name
 * GET  http://localhost:3000/employees?gender=Female     → Fetch female employees
 * GET  http://localhost:3000/employees?gender=Male       → Fetch male employees
 */

/**
 * This class demonstrates how to use Rest Assured's RequestSpecification and
 * ResponseSpecification to avoid repetition in API tests.
 */
public class RequestAndResponseSpecification {
	
    // Object to store request specification (base URI, base path, etc.)
    RequestSpecification httpRequest;

    // Object to store response specification (status code, headers, etc.)
    ResponseSpecification httpResponse;

	/**
	 * Runs once before all tests.
	 * Sets up reusable request and response specifications.
	 */
	@BeforeClass
	public void setup()
	{
		// ---------------------- REQUEST SPECIFICATION ----------------------
		// Used to define common request details (base URI, path, headers, etc.)
		RequestSpecBuilder reqBuilder = new RequestSpecBuilder();
			reqBuilder.setBaseUri("http://localhost:3000");  // Base URI for all API calls
			reqBuilder.setBasePath("/employees");            // Common path for all endpoints
				
		httpRequest = reqBuilder.build();   // Build and store request specification
		
		/*Alternative way:
		RequestSpecification httpRequest = new RequestSpecBuilder();
		.setBaseUri("http://localhost:3000")
		.setBasePath("/employees");
		.build();
		*/
		
		// ---------------------- RESPONSE SPECIFICATION ----------------------
		// Used to define expected response details (status, headers, etc.)
		ResponseSpecBuilder resBuilder = new ResponseSpecBuilder();
			resBuilder.expectStatusCode(200);                         // Expect HTTP 200 OK
			resBuilder.expectHeader("Content-Type", equalTo("application/json")); // Expect JSON content
									
		httpResponse = resBuilder.build();   // Build and store response specification
	}
	
	
	/**
	 * Test Case 1: Get all employees
	 * Verifies that the response returns at least one employee record.
	 */
	@Test(priority=1)
	void getAllEmployees()
	{
		given()
			.spec(httpRequest)                      // Use pre-defined request specification
		.when()
			.get()                                  // Send GET request to /employees
		.then()
			.spec(httpResponse)                     // Validate using response specification
			.body("size()", greaterThan(0))         // Ensure at least one record is returned
			.log().body();                          // Log response body to console
	}
	
	/**
	 * Test Case 2: Get all male employees
	 * Verifies that each record returned has gender = "Male".
	 */
	@Test(priority=2)
	void getMaleEmployees()
	{
		given()
			.spec(httpRequest)                      // Use request spec (base URI/path)
			.queryParam("gender", "Male")           // Add query parameter
		.when()
			.get()                                  // Send GET request
		.then()
			.spec(httpResponse)                     // Validate with response spec
			.body("gender", everyItem(equalTo("Male"))) // Ensure all records have gender "Male"
			.log().body();                          // Print response
	}
	
	/**
	 * Test Case 3: Get all female employees
	 * Verifies that each record returned has gender = "Female".
	 */
	@Test(priority=3)
	void getFeMaleEmployees()
	{
		given()
			.spec(httpRequest)
			.queryParam("gender", "Female")         // Query employees by gender = Female
		.when()
			.get()
		.then()
			.spec(httpResponse)
			.body("gender", everyItem(equalTo("Female")))
			.log().body();
	}
	
	/**
	 * Test Case 4: Get employee by ID
	 * Fetches employee with ID = 1 and verifies the ID in response.
	 */
	@Test(priority = 4)
	public void getEmployeeById() {
	    given()
	        .spec(httpRequest)                      // Use base setup
	    .when()
	        .get("/1")                              // GET /employees/1
	    .then()
	        .spec(httpResponse)                     // Apply common response checks
	        .body("id", equalTo("1"));              // Validate ID matches 1
	}

	/**
	 * Test Case 5: Get employee(s) by first name
	 * Fetches employees with first name "Steven" and verifies all results.
	 */
	@Test(priority = 5)
	public void getEmployeeByFirstName() {
	    given()
	        .spec(httpRequest)
	        .queryParam("first_name", "Steven")     // Filter by first_name
	    .when()
	        .get()
	    .then()
	        .spec(httpResponse)
	        .body("first_name", everyItem(equalTo("Steven"))); // Validate all results
	}

	/**
	 * Test Case 6: Get employee(s) by last name
	 * Fetches employees with last name "King" and verifies all results.
	 */
	@Test(priority = 6)
	public void getEmployeeByLastName() {
	    given()
	        .spec(httpRequest)
	        .queryParam("last_name", "King")        // Filter by last_name
	    .when()
	        .get()
	    .then()
	        .spec(httpResponse)
	        .body("last_name", everyItem(equalTo("King"))); // Validate all results
	}
}
