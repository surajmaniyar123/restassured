package day2;

import org.testng.annotations.Test; // Import TestNG @Test
import org.json.JSONObject; // Import JSONObject class
import org.json.JSONTokener; // Import JSONTokener
import org.testng.annotations.AfterMethod; // Import @AfterMethod annotation
import static io.restassured.RestAssured.*; // Import Rest Assured statically
import static org.hamcrest.Matchers.*; // Import Hamcrest matchers

import java.io.File; // Import File class
import java.io.FileNotFoundException; // Import exception class
import java.io.FileReader; // Import FileReader
import java.util.HashMap; // Import HashMap

public class PostRequestBodyExamples {

	String studentId; // Store the student ID created in each test for deletion later

	// =====================================================
	// 1️⃣ CREATE POST REQUEST BODY USING HashMap
	// =====================================================
	// @Test
	void createStudentUsingHashMap() {

		HashMap<String, Object> requestBody = new HashMap<>(); // Create request body as HashMap
		requestBody.put("name", "Scott"); // Add name field
		requestBody.put("location", "France"); // Add location field
		requestBody.put("phone", "123456"); // Add phone field
		String courses[] = { "C", "C++" }; // Create courses array
		requestBody.put("courses", courses); // Add courses array to request body

		studentId = given()
					.contentType("application/json") // Set content type to JSON
					.body(requestBody) // Attach HashMap as request body
				.when()
					.post("http://localhost:3000/students") // POST request to API
				.then()
					.statusCode(201) // Validate HTTP 201 Created
					.body("name", equalTo("Scott")) // Validate name in response
					.body("location", equalTo("France")) // Validate location
					.body("phone", equalTo("123456")) // Validate phone
					.body("courses[0]", equalTo("C")) // Validate first course
					.body("courses[1]", equalTo("C++")) // Validate second course
					.header("Content-Type", "application/json") // Validate Content-Type header
					.log().body() // Log full response
					.extract().jsonPath().getString("id"); // Extract student ID
	}

	// =====================================================
	// 2️⃣ CREATE POST REQUEST BODY USING org.json LIBRARY  // Add json dependency in pom.xml
	// =====================================================
	// @Test
	void createStudentUsingJsonLibrary() {

		JSONObject requestBody = new JSONObject(); // Create JSONObject
		requestBody.put("name", "Scott"); // Add name field
		requestBody.put("location", "France"); // Add location field
		requestBody.put("phone", "123456"); // Add phone field
		String courses[] = { "C", "C++" }; // Create courses array
		requestBody.put("courses", courses); // Add courses to JSON

		studentId = given()
						.contentType("application/json") // Set content type to JSON
						.body(requestBody.toString()) // Convert JSONObject to String
					.when()
						.post("http://localhost:3000/students") // POST request
					.then()
						.statusCode(201) // Validate HTTP 201 Created
						.body("name", equalTo("Scott")) // Validate response fields
						.body("location", equalTo("France"))
						.body("phone", equalTo("123456"))
						.body("courses[0]", equalTo("C"))
						.body("courses[1]", equalTo("C++"))
						.header("Content-Type", "application/json") // Validate header
						.log().body() // Log response
						.extract().jsonPath().getString("id"); // Extract student ID
	}

	// =====================================================
	// 3️⃣ CREATE POST REQUEST BODY USING POJO CLASS
	// =====================================================
	// @Test
	void createStudentUsingPojoClass() {

		StudentPojo requestBody = new StudentPojo(); // Create POJO instance
		requestBody.setName("Scott"); // Set name
		requestBody.setLocation("France"); // Set location
		requestBody.setPhone("123456"); // Set phone
		String courses[] = { "C", "C++" }; // Create courses array
		requestBody.setCourses(courses); // Set courses array
		
		//StudentPojo requestBody = new StudentPojo("Scott", "France", "123456", new String[]{"C", "C++"}); // Alternative: Parameterized constructor

		studentId = given()
						.contentType("application/json") // Set content type
						.body(requestBody) // Pass POJO (auto-converted to JSON)
					.when()
						.post("http://localhost:3000/students") // POST request
					.then()
						.statusCode(201) // Validate 201 Created
						.body("name", equalTo(requestBody.getName())) // Validate name
						.body("location", equalTo(requestBody.getLocation()))// Validate location
						.body("phone", equalTo(requestBody.getPhone())) // Validate phone
						.body("courses[0]", equalTo(requestBody.getCourses()[0])) // Validate courses
						.body("courses[1]", equalTo(requestBody.getCourses()[1]))
						.header("Content-Type", "application/json") // Validate header
						.log().body() // Log response
						.extract().jsonPath().getString("id"); // Extract student ID
	}

	// =====================================================
	// 4️⃣ CREATE POST REQUEST BODY USING EXTERNAL JSON FILE
	// =====================================================
	@Test
	void createStudentUsingExternalfile() throws FileNotFoundException {

		File myfile = new File(".\\src\\test\\java\\day2\\body.json"); // JSON file path (.\\ for current location)
		FileReader fileReader = new FileReader(myfile); // Read file
		JSONTokener jsonTokener = new JSONTokener(fileReader); // Parse JSON
		JSONObject requestBody = new JSONObject(jsonTokener); // Convert to JSONObject

		studentId = given()
						.contentType("application/json") // Set content type
						.body(requestBody.toString()) // Attach JSON body
					.when()
						.post("http://localhost:3000/students") // POST request
					.then()
						.statusCode(201) // Validate 201 Created
						.body("name", equalTo("Scott")) // Validate fields
						.body("location", equalTo("France"))
						.body("phone", equalTo("123456"))
						.body("courses[0]", equalTo("C"))
						.body("courses[1]", equalTo("C++"))
						.header("Content-Type", "application/json") // Validate header
						.log().body() // Log response
						.extract().jsonPath().getString("id"); // Extract student ID
	}

	// Alternative shortcut: Directly pass File object to body() method (Rest Assured will handle reading and parsing)
	//@Test
	void createStudentUsingExternalfile2() {

	    File file = new File(".\\src\\test\\java\\day2\\body.json");
	  //File file=  new File (System.getProperty("user.dir") + "\\src\\test\\java\\day2\\body.json"); // Alternative: Use user.dir for dynamic path

	    studentId = given()
	                    .contentType("application/json")
	                    .body(file)   // ✅ direct file usage (shortcut)
	                .when()
	                    .post("http://localhost:3000/students")
	                .then()
	                    .statusCode(201)
	                    .body("name", equalTo("Scott"))
	                    .body("location", equalTo("France"))
	                    .body("phone", equalTo("123456"))
	                    .body("courses[0]", equalTo("C"))
	                    .body("courses[1]", equalTo("C++"))
	                    .header("Content-Type", "application/json")
	                    .log().body()
	                    .extract().jsonPath().getString("id");
	}
	// =====================================================
	// 🧹 CLEANUP AFTER EACH TEST
	// =====================================================
	@AfterMethod
	void deleteStudentRecord() {

		given()
		.when()
			.delete("http://localhost:3000/students/" + studentId) // Delete student by ID
		.then()
			.statusCode(200); // Validate deletion
	}
}
