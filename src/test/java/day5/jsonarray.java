package day5;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class jsonarray {

	@Test
	public void validateEmployeesJsonArray() {

		// Base URI
		RestAssured.baseURI = "https://your-api-url.com";

		// Step 1: Call API and extract response
		Response response = given()
							.when()
								.get("/employees") // <-- your endpoint
							.then()
								.statusCode(200)
								.extract().response();

		// Step 2: Convert response to JsonPath
		JsonPath jsonPath = response.jsonPath();

		// Step 3: Validate JSON array size
		int employeeCount = jsonPath.getInt("size()");
		assertThat("Employee count mismatch", employeeCount, equalTo(10));

		// Step 4: Validate first employee
		assertThat(jsonPath.getInt("[0].id"), equalTo(1));
		assertThat(jsonPath.getString("[0].first_name"), equalTo("Donald"));
		assertThat(jsonPath.getString("[0].last_name"), equalTo("OConnell"));
		assertThat(jsonPath.getString("[0].email"), equalTo("Donald@gmail.com"));
		assertThat(jsonPath.getString("[0].gender"), equalTo("Male"));

		// Step 5: Validate last employee
		assertThat(jsonPath.getInt("[9].id"), equalTo(10));
		assertThat(jsonPath.getString("[9].first_name"), equalTo("Laura"));
		assertThat(jsonPath.getString("[9].gender"), equalTo("Female"));

		// Step 6: Validate all genders
		List<String> genders = jsonPath.getList("gender");
		for (String gender : genders) {
			assertThat("Invalid gender found: " + gender, gender, anyOf(equalTo("Male"), equalTo("Female")));
		}

		// Step 7: Validate specific first names exist
		List<String> firstNames = jsonPath.getList("first_name");
		assertThat(firstNames, hasItems("virat", "Donald", "Steve"));

		// Step 8: Optional case-insensitive check for Steve
		boolean isStevePresentIgnoreCase = firstNames.stream().anyMatch(name -> name.equalsIgnoreCase("Steve"));
		assertThat("Steve (case-insensitive) not found", isStevePresentIgnoreCase, is(true));
	}
}
