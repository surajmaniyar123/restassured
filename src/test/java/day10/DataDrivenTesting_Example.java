package day10;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class DataDrivenTesting_Example {

    // Data provider with multiple user data sets
    @DataProvider(name = "userData")
    public Object[][] getUserData() {
        return new Object[][] {
            {"John Doe", "Developer"},
            {"Jane Smith", "Tester"},
            {"Alice Johnson", "Manager"}
        };
    }

    @Test(dataProvider = "userData")
    public void createUserAPI(String name, String job) {

        String payload = "{ \"name\": \"" + name + "\", \"job\": \"" + job + "\" }";

        given()
            .header("Content-Type", "application/json")
            .body(payload)
        .when()
            .post("https://reqres.in/api/users")
        .then()
            .statusCode(201)
            .body("name", equalTo(name))
            .body("job", equalTo(job))
            .body("id", notNullValue()) // API returns generated id
            .body("createdAt", notNullValue()); // API returns timestamp
    }
}
