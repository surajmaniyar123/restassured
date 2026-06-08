package day9;   // Package declaration

// Importing static methods from RestAssured for easy HTTP request handling
import static io.restassured.RestAssured.*;

// Importing Hamcrest's assertion methods for readable validations
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

// Importing TestNG annotations for structuring test cases
import org.testng.annotations.Test;

// Importing RestAssured Response class to capture API responses
import io.restassured.response.Response;

/**
 * This class demonstrates the concept of Serialization and Deserialization using RestAssured and the Student class.
 * 
 * Serialization = Converting Java Object (POJO) → JSON
 * Deserialization = Converting JSON → Java Object (POJO)
 */
public class SerializationAndDeserializationExample 
{

	String stuId;   // Variable to store student ID (returned from API)

	/**
	 * Step 1: Test Serialization
	 * --------------------------
	 * Converts a Student Java object into JSON and sends it to the API (POST request).
	 * After the student record is created, the ID from the response is extracted and stored.
	 */
	@Test
	public void testSerialization()
	{
		// Creating an array of courses
		String courses[] = {"Selenium", "Java", "Python"};
		
		// Creating a new Student object (will be serialized to JSON automatically)
		Student stu = new Student("John", "Delhi", "1234567890", courses);
		
		// Sending POST request to create a new student
		stuId = given()
					.contentType("application/json")   // Setting request content type as JSON
					.body(stu)                          // Serialization happens here (Java → JSON)
				.when()
					.post("http://localhost:3000/students")   // POST endpoint to create student
				.then()
					.statusCode(201)                   // Validate that response status is 'Created'
					.log().body()                      // Log the response body for visibility
					.extract().response().jsonPath().getString("id");       // Extract 'id' from response JSON
	}
	
	/**
	 * Step 2: Test Deserialization
	 * ----------------------------
	 * Fetches the previously created student record (GET request),
	 * converts JSON response into a Student Java object,
	 * and verifies its details.
	 */
	@Test(dependsOnMethods="testSerialization")
	public void testDeserialization()
	{
		// Sending GET request to retrieve the student record by ID
		Response response = given()
								.pathParam("id", stuId)               // Pass student ID in URL
							.when()
								.get("http://localhost:3000/students/{id}")   // GET endpoint
							.then()
								.statusCode(200)                     // Validate successful response
								.extract().response();               // Extract response object
		
		// Extract 'id' separately from JSON (optional, for confirmation)
		String extractedId = response.jsonPath().getString("id");
		
		// Deserialization: Convert JSON response into Student POJO class object
		Student stu = response.as(Student.class);
		
		// Assertions to verify that deserialized object has correct data
		assertThat(stu.getName(), is("John"));
		assertThat(stu.getLocation(), is("Delhi"));
		assertThat(stu.getPhone(), is("1234567890"));
		assertThat(stu.getCourses()[0], is("Selenium"));
		
		/* TestNG Assertions alternative
		Assert.assertEquals(stu.getName(), "John");
		Assert.assertEquals(stu.getLocation(), "Delhi");
		Assert.assertEquals(stu.getPhone(), "1234567890");
		Assert.assertEquals(stu.getCourses()[0], "Selenium");
		*/
		
		// Print deserialized student details for verification
		System.out.println("Student details: " + stu + extractedId);
	}
	
	/**
	 * Step 3: Delete Student Record
	 * -----------------------------
	 * Deletes the created student record using its ID (DELETE request).
	 * This ensures cleanup after test execution.
	 */
    @Test(dependsOnMethods="testDeserialization")
    public void deleteStudent() {
    
        // Sending DELETE request to remove the student record
        given()
            .pathParam("id", stuId)                     // Pass student ID in URL
        .when()
            .delete("http://localhost:3000/students/{id}")   // DELETE endpoint
        .then()
            .statusCode(200);                           // Validate successful deletion
    }
}
