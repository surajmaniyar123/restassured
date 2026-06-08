package day1;
	import io.restassured.RestAssured;
	import io.restassured.http.ContentType;
	import org.junit.jupiter.api.Test;

	import static io.restassured.RestAssured.*;
	import static org.hamcrest.Matchers.*;

	public class AllMatchersAnnotatedTest {

	    /*
	    =========================
	    MATCHER → JSON FIELD MAPPING
	    =========================

	    [Equality / Comparison]
	      equalTo()      → status, user.id
	      is()           → user.name, active
	      not()          → user.name, user.roles

	    [String Matchers]
	      containsString() → message
	      startsWith()     → version, user.address.city
	      endsWith()       → version, user.address.city
	      matchesRegex()   → version

	    [Empty / Null String]
	      emptyOrNullString() → user.middleName, user.nickname

	    [Number Matchers]
	      greaterThan() → user.address.zip, user.scores elements
	      lessThan()    → user.scores[0]
	      closeTo()     → user.scores[1]

	    [Collection Matchers]
	      hasItem()     → user.roles
	      hasItems()    → user.roles
	      contains()    → user.roles
	      hasSize()     → user.scores
	      everyItem()   → user.scores

	    [Logical Matchers]
	      allOf()       → user.address.city
	      anyOf()       → status

	    [REST Assured Specific]
	      statusCode()  → whole response
	      header()      → Content-Type
	      time()        → response time
	    */

	    @Test
	    public void testAllMatchers() {

	        RestAssured.baseURI = "https://api.example.com";

	        given()
	            .contentType(ContentType.JSON)

	        .when()
	            .get("/user/details")

	        .then()
	            // ====================
	            // REST Assured Specific
	            // ====================
	            .statusCode(200)  // Verify HTTP status code is 200

	            // ====================
	            // STRING MATCHERS
	            // ====================
	            .body("status", equalTo("success"))                      // exact match
	            .body("message", containsString("fetched"))             // contains substring
	            .body("version", startsWith("v"))                        // starts with "v"
	            .body("version", endsWith("0.0"))                        // ends with "0.0"
	            .body("version", matchesRegex("v\\d\\.\\d\\.\\d"))       // regex match for version
	            .body("user.name", is("John Doe"))                       // shorthand for equalTo

	            // ====================
	            // EMPTY OR NULL STRING MATCHERS
	            // ====================
	            .body("user.middleName", emptyOrNullString())            // empty string
	            .body("user.nickname", emptyOrNullString())              // null value

	            // ====================
	            // NUMBER MATCHERS
	            // ====================
	            .body("user.id", equalTo(101))                           // exact number match
	            .body("user.address.zip", greaterThan(10000))            // greater than
	            .body("user.scores[0]", lessThan(90))                    // less than
	            .body("user.scores[1]", closeTo(90, 1))                  // close to with tolerance

	            // ====================
	            // COLLECTION MATCHERS
	            // ====================
	            .body("user.roles", hasItem("admin"))                    // single item present
	            .body("user.roles", hasItems("editor", "viewer"))        // multiple items present
	            .body("user.roles", contains("admin", "editor", "viewer")) // exact order match
	            .body("user.scores", hasSize(3))                         // array size
	            .body("user.scores", everyItem(greaterThan(80)))         // every item greater than 80

	            // ====================
	            // LOGICAL MATCHERS
	            // ====================
	            .body("status", anyOf(equalTo("success"), equalTo("ok"))) // OR condition
	            .body("active", is(true))                                 // boolean value check
	            .body("user.address.city", allOf(startsWith("New"), endsWith("York"))) // AND condition

	            // ====================
	            // NEGATION MATCHERS
	            // ====================
	            .body("user.name", not(equalTo("Jane Doe")))              // not equal to
	            .body("user.roles", not(hasItem("guest")))               // not contains

	            // ====================
	            // HEADER MATCHERS
	            // ====================
	            .header("Content-Type", containsString("application/json")) // header contains

	            // ====================
	            // RESPONSE TIME MATCHER
	            // ====================
	            .time(lessThan(2000L));                                    // response time less than 2 sec
	    }
	}



