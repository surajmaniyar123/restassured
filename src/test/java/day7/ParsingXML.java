package day7;

import static io.restassured.RestAssured.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import java.util.List;

import org.testng.annotations.Test;

import io.restassured.response.Response;
import io.restassured.path.xml.XmlPath;

public class ParsingXML {

    /**
     * Test case to validate XML response data from a mock API
     * Validations performed:
     * - HTTP status code is 200
     * - Content type is application/xml
     * - Specific XML element values (city, firstName, lastName, state)
     */
    //@Test(priority=1)
    void testXMLResponse1() {
        given() // Given section (request specifications can go here)

        .when() // When section (performing HTTP action)
            .get("https://mocktarget.apigee.net/xml") // Sending GET request
        .then() // Then section (response validations)
            .statusCode(200) // Validate HTTP status code is 200
            .contentType("application/xml") // Validate Content-Type
            .header("Content-Type", "application/xml; charset=utf-8") // Validate header
            .header("Content-Type", equalTo("application/xml; charset=utf-8")) // Using matcher for header
            .body("root.city", equalTo("San Jose")) // Validate XML element <city>
            .body("root.firstName", equalTo("John")) // Validate <firstName>
            .body("root.lastName", equalTo("Doe")) // Validate <lastName>
            .body("root.state", equalTo("CA")) // Validate <state>
            .log().body(); // Print response body to console
    }

    /**
     * Test case to validate attributes in an XML response
     * Validations performed:
     * - HTTP status code is 200
     * - Content type is application/xml
     * - Specific XML attributes using '@' notation
     */
    //@Test(priority=2)
    void testXMLResponse2() {
        given()

        .when()
            .get("https://httpbin.org/xml") // GET request to fetch XML
        .then()
            .statusCode(200) // Validate HTTP status code
            .contentType("application/xml") // Validate content type
            .body("slideshow.@title", equalTo("Sample Slide Show")) // Validate XML attribute 'title'
            .body("slideshow.@date", equalTo("Date of publication")) // Validate XML attribute 'date'
            .body("slideshow.@author", equalTo("Yours Truly")) // Validate XML attribute 'author'
            .log().body(); // Print response body
    }

    /**
     * Test case to extract and parse XML response using XmlPath
     * Validations performed:
     * - Number of slides in the XML
     * - Titles of each slide
     * - Number of items in slides
     * - Specific item values
     */
    @Test(priority=3)
    void testParsingXMLResponse() {
        // Send GET request and extract the response
        Response response = given()
        
        .when()
            .get("https://httpbin.org/xml") // Fetch XML response
        .then()
            .statusCode(200) // Validate status code
            .contentType("application/xml") // Validate content type
            .extract().response(); // Extract response for further parsing

        // Initialize XmlPath with the response string for XML parsing
        XmlPath xmlPath = new XmlPath(response.asString());

        // Capture the titles of all slides
        List<String> slideTitles = xmlPath.getList("slideshow.slide.title");

        // Validate the number of slides
        assertThat(slideTitles.size(), is(2));

        // Validate individual slide titles
        assertThat(slideTitles.get(0), is("Wake up to WonderWidgets!"));
        assertThat(slideTitles.get(1), is("Overview"));

        // Validate multiple slide titles exist
        assertThat(slideTitles, hasItems("Wake up to WonderWidgets!", "Overview"));

        // Capture all slide items
        List<String> items = xmlPath.getList("slideshow.slide.item");

        // Print and validate the number of items
        System.out.println("Number of Items: " + items.size());
        assertThat(items.size(), is(3));

        // Validate specific items
        assertThat(items.get(0), is("WonderWidgets"));
        assertThat(items.get(2), is("buys"));

        // Validate multiple items
        assertThat(items, hasItems("WonderWidgets", "buys"));

        // Check dynamically if "WonderWidgets" exists in items
        boolean status = false;
        for (String item : items) {
            if (item.equals("WonderWidgets")) {
                status = true;
                break;
            }
        }
        assertThat(status, is(true)); // Assert that "WonderWidgets" exists
    }
}
