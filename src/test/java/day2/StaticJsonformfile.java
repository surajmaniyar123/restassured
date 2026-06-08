package day2;

import static io.restassured.RestAssured.given;

import java.io.File;
import java.net.URISyntaxException;

import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;

public class StaticJsonformfile {

	@Test
	public void addBook() throws URISyntaxException {

		RestAssured.baseURI = "http://216.10.245.166";

		File jsonFile = new File(getClass()
								.getClassLoader()
								.getResource("Addbookdetails.json")
								//.getFile()); //src/test/resources/Addbookdetails.json
								.toURI());   // ✅ safer than getFile()


		JsonPath jsonPath = given()
								.contentType(ContentType.JSON)
								// .body(new File("src/test/resources/Addbookdetails.json"))
								.body(jsonFile)
							.when()
								.post("/Library/Addbook.php")
							.then()
								.statusCode(200).extract().jsonPath();

		String id = jsonPath.getString("ID");
		System.out.println(id);

		
	}
}
