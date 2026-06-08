package day5;

// Import TestNG annotation for writing test methods
import org.testng.annotations.Test;

// Import classes for parsing JSON and handling response body
import io.restassured.path.json.JsonPath;
import io.restassured.response.ResponseBody;

// Import RestAssured methods for HTTP requests
import static io.restassured.RestAssured.given;

// Import Hamcrest methods for assertions
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class ParsingJSONArray_EmployeeAPI {

    @Test
    void testJsonResponseBody()
    {
        // Send GET request to the employees API and store response body
        ResponseBody responseBody = given()
        							.when()
        								.get("http://localhost:3000/employees") // Call the API endpoint
        							.then()
        								.statusCode(200) // Verify the response status code is 200 (OK)
        								.extract().response().body(); // Extract the response body for further processing
       
        JsonPath jsonpath = new JsonPath(responseBody.asString()); // // Convert response body to JsonPath object to parse JSON easily
        // JsonPath jsonPath = responseBody.jsonPath(); // Alternative way to get JsonPath from response body
         
          
        /* Alternative way
         * JsonPath jsonpath = given()
        						.when()
            						.get("http://localhost:3000/employees")
        						.then()
            						.statusCode(200)
        							.extract().jsonPath();   // Directly extract JsonPath

    			int employeeCount = jsonpath.getInt("size()");
         */
        
       
   
         // Get the size of the JSON array (number of employees)
        int employeeCount = jsonpath.getInt("size()");
        
        // Loop through all employees and print their details
        for(int i = 0; i < employeeCount; i++)
        {
            String firstName = jsonpath.getString("[" + i + "].first_name"); // Get first name of employee i
            String lastName = jsonpath.getString("[" + i + "].last_name");   // Get last name of employee i
            String email = jsonpath.getString("[" + i + "].email");          // Get email of employee i
            String gender = jsonpath.getString("[" + i + "].gender");        // Get gender of employee i
            System.out.println(firstName + " " + lastName + "  " + email + "  " + gender); // Print employee details
        }
        
        // Search for an employee named "Steve" in the list
        boolean status = false; // Flag to check if "Steve" exists
        
        for(int i = 0; i < employeeCount; i++)
        {
            String firstName = jsonpath.getString("[" + i + "].first_name"); // Get first name of employee i
            if(firstName.equals("Steve")) // Check if first name is "Steve"
            {
                status = true; // Set flag to true if found
                break; // Exit loop once found
            }
        }
        
        // Assert that "Steve" exists in the employee list
        assertThat(status, is(true)); 
    }
}
