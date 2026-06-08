package day5;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class jsonobject {

    @Test
    public void validateStoreBooks() {

        // Base URI
        RestAssured.baseURI = "https://your-api-url.com";

        // Step 1: Call API and extract response
        Response response =  given()
        					.when()
        						.get("/store")   // <-- your endpoint
        					.then()
                				.statusCode(200)
                				.extract().response();
        
        // Step 2: Convert response to JsonPath
        JsonPath jsonPath = response.jsonPath();

        // Step 3: Validate book array size
        int bookCount = jsonPath.getInt("store.book.size()");
        assertThat("Book count mismatch", bookCount, equalTo(4));

        // Step 4: Validate first book fields
        assertThat(jsonPath.getString("store.book[0].author"), equalTo("Nigel Rees"));
        assertThat(jsonPath.getString("store.book[0].category"), equalTo("reference"));
        assertThat(jsonPath.getFloat("store.book[0].price"), equalTo(8.95f));
        assertThat(jsonPath.getString("store.book[0].title"), equalTo("Sayings of the Century"));

        // Step 5: Validate last book fields
        assertThat(jsonPath.getString("store.book[3].author"), equalTo("J. R. R. Tolkien"));
        assertThat(jsonPath.getString("store.book[3].category"), equalTo("fiction"));
        assertThat(jsonPath.getString("store.book[3].title"), equalTo("The Lord of the Rings"));
        assertThat(jsonPath.getFloat("store.book[3].price"), equalTo(22.99f));

        // Step 6: Validate all book categories are either 'fiction' or 'reference'
        List<String> categories = jsonPath.getList("store.book.category");
        for (String category : categories) {
            assertThat("Invalid category found: " + category, category, anyOf(equalTo("fiction"), equalTo("reference")));
        }

        // Step 7: Verify specific titles exist
        List<String> titles = jsonPath.getList("store.book.title");
        assertThat(titles, hasItems("Moby Dick", "The Lord of the Rings"));

        // Step 8: Verify author 'Evelyn Waugh' is present
        List<String> authors = jsonPath.getList("store.book.author");
        assertThat(authors, hasItem("Evelyn Waugh"));

        // Step 9: Verify only two books have ISBN
        List<String> isbns = jsonPath.getList("store.book.isbn");
        assertThat(isbns.size(), equalTo(2));
        assertThat(isbns, hasItems("0-553-21311-3", "0-395-19395-8"));
    }
}
