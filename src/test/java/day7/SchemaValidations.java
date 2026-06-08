package day7;

import static io.restassured.RestAssured.given;

//import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;// For json schema validation
//import static io.restassured.matcher.RestAssuredMatchers.matchesXsdInClasspath;    // For xml/xsd validation

import io.restassured.module.jsv.JsonSchemaValidator; // For JSON Schema validation
import io.restassured.matcher.RestAssuredMatchers; // For XML/XSD validation
import org.testng.annotations.Test;

public class SchemaValidations {

	/**
	 * Test case to validate JSON response against a JSON Schema
	 * Validations performed:
	 * - Sends a GET request to the endpoint
	 * - Validates that the JSON response matches the schema defined in 'jsonSchema.json'
	 *
	 * Note:
	 * - The JSON schema file must be present in the classpath (e.g., src/test/resources folder)
	 */
	// @Test(priority=1)
	void testJsonSchema() {
				given() 
				.when() 
					.get("https://mocktarget.apigee.net/json") 
				.then() 
					.assertThat().body(JsonSchemaValidator.matchesJsonSchemaInClasspath("jsonSchema.json"));
					// .body(matchesJsonSchemaInClasspath("jsonSchema.json")); Alternative way if static import is used

	}

	/**
	 * Test case to validate XML response against an XML Schema (XSD)
	 * Validations performed:
	 * - Sends a GET request to the endpoint
	 * - Validates that the XML response matches the schema defined in 'xmlSchema.xsd'
	 *
	 * Note:
	 * - The XSD file must be present in the classpath (e.g., src/test/resources folder)
	 */
	@Test(priority = 1)
	void testxmlSchema() {
				given() 
				.when() 
					.get("https://mocktarget.apigee.net/xml") 
				.then() 
					.assertThat().body(RestAssuredMatchers.matchesXsdInClasspath("xmlSchema.xsd"));
					// .body(matchesXsdInClasspath("xmlSchema.xsd")); Alternative way if static import is used
	}
}
