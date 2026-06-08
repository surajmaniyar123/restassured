package day1;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.HashMap;

import org.testng.annotations.Test;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class Practise {
    int postId;

    @Test(priority = 1)
    void createPosts() {

        // Prepare request body
        HashMap<String, Object> data = new HashMap<>();
        data.put("title", "foo");
        data.put("body", "bar");
        data.put("userId", 1);

        // Send POST request
        Response response = given()
                                .header("Content-Type", "application/json")
                                .body(data)
                            .when()
                                .post("https://jsonplaceholder.typicode.com/posts");

        // Parse JSON
        JsonPath json = response.jsonPath();
        postId = json.getInt("id"); // store created post ID

        // Assertions
        assertThat("Title should match", json.getString("title"), equalTo("foo"));
        assertThat("Body should match", json.getString("body"), equalTo("bar"));
        assertThat("UserId should match", json.getInt("userId"), equalTo(1));
        assertThat("ID should not be null", postId, notNullValue());

        System.out.println("✅ Created post with ID: " + postId);
    }

    @Test(priority = 2)
    void getPostByCreatedId() {

        // Send GET request for the post created above
        Response response = given()
                                .header("Content-Type", "application/json")
                            .when()
                                .get("https://jsonplaceholder.typicode.com/posts/" + postId);

        // Assert status code
        assertThat("Status code should be 200", response.getStatusCode(), equalTo(200));

        // Extract JSON body
        JsonPath json = response.jsonPath();

        // Assertions for fields
        assertThat("Post ID should match created ID", json.getInt("id"), equalTo(postId));
        assertThat("User ID should exist", json.getInt("userId"), notNullValue());
        assertThat("Title should not be null", json.getString("title"), notNullValue());
        assertThat("Body should not be null", json.getString("body"), notNullValue());

        System.out.println("✅ Fetched post with ID: " + postId);
        System.out.println("Response body:\n" + response.getBody().asString());
    }
}
