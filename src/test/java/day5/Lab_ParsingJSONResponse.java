package day5;  

import static org.hamcrest.MatcherAssert.assertThat;  
import static org.hamcrest.Matchers.is;  

import java.io.File;  
import java.io.FileNotFoundException;  
import java.io.FileReader;  

import org.json.JSONObject;  
import org.json.JSONTokener;  

import org.testng.annotations.Test;  

import io.restassured.path.json.JsonPath;  

public class Lab_ParsingJSONResponse {  // Main class containing all JSON parsing test cases

    @Test(priority = 1)  // TestNG annotation – this test will run first
    public void testSimpleJsonParsing() throws FileNotFoundException 
    {
     /*
        File myfile = new File(".\\src\\test\\resources\\simple.json");  // Load JSON file from project folder
        FileReader fileReader = new FileReader(myfile);  // Read the file content
        JSONTokener jsonTokener = new JSONTokener(fileReader);  // Convert file stream into JSON tokens
        JSONObject simpleJsonResponse = new JSONObject(jsonTokener);  // Convert tokens into a JSONObject

        JsonPath jsonPath = new JsonPath(simpleJsonResponse.toString());  // Convert JSON Object into JsonPath for easy extraction
     */  
    	 // Alternative way to directly load file
    	
        JsonPath jsonPath = new JsonPath(new File(".\\src\\test\\resources\\simple.json"));  
       
        String placeId = jsonPath.getString("place_id");  // Extract value of 'place_id' from JSON
        System.out.println("Place ID: " + placeId);  // Print value for verification
        assertThat(placeId, is("9172e95034d47483d8e0775710eb6a54"));  // Validate expected value
    }

    @Test(priority = 2)  // This test will run second
    public void testNestedJsonParsing() throws FileNotFoundException {

        File myfile = new File(".\\src\\test\\resources\\nested.json");  // Load nested JSON file
        FileReader fileReader = new FileReader(myfile);  // Read the file
        JSONTokener jsonTokener = new JSONTokener(fileReader);  // Convert file to JSON tokens
        JSONObject nestedJsonResponse = new JSONObject(jsonTokener);  // Convert tokens into JSONObject

        JsonPath jsonPath = new JsonPath(nestedJsonResponse.toString());  // Convert to JsonPath format for easy extraction

        int purchaseAmount = jsonPath.getInt("dashboard.purchaseAmount");  // Extract purchase amount from nested JSON
        System.out.println("Purchase Amount: " + purchaseAmount);  
        assertThat(purchaseAmount, is(1060));  // Validate expected amount

        String firstCourseTitle = jsonPath.getString("courses[0].title");  // Get title of the first course
        System.out.println("First Course Title: " + firstCourseTitle);  
        assertThat(firstCourseTitle, is("Selenium"));  // Validate expected title

        int postmanCopies = jsonPath.getInt("courses[3].copies");  // Get number of copies sold by Postman course
        System.out.println("Total Copies of Postman: " + postmanCopies);  
        assertThat(postmanCopies, is(5));  // Validate expected copies

        int totalCourses = jsonPath.getInt("courses.size()");  // Get total number of courses in array
        System.out.println("Total Number of Courses: " + totalCourses);  
        assertThat(totalCourses, is(4));  // Validate expected count

        System.out.print("Course Titles: ");  
        for (int i = 0; i < totalCourses; i++) {  // Loop through each course
            String courseTitle = jsonPath.getString("courses[" + i + "].title");  // Extract title dynamically
            System.out.println(courseTitle);  // Print the title
        }
        System.out.println();  // Print empty line for better readability
    }
}
