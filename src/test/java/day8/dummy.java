package day8;

import static io.restassured.RestAssured.*;

import io.restassured.response.Response;


import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

import org.testng.annotations.Test;

public class dummy {

	@Test
	public void test01() {

		Response response = get("https://reqres.in/api/users?page=2");

		System.out.println(response.getStatusCode());
	}

	@Test(priority = 1)
	void getUsers() {
		
		baseURI = "https://reqres.in/api/users";
		given()
		.header("Content-Type", "application/json")
		.queryParam("page", "2") // optional for GET, keeps consistency
				.when()
				.get() // send GET request to fetch page 2 users
				.then()
				.statusCode(200) // verify HTTP status code is 200 OK
				.header("Content-Type", equalTo("application/json; charset=utf-8")) // verify response header
				.body("page", equalTo(2)) // verify JSON field "page" equals 2
				.body(containsString("email")) // check that response contains "email" field
				.log().all(); // log full response for debugging
	}
}