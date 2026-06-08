package day5; // 👉 Declares that this class belongs to package 'day5'

// 👉 Import TestNG @Test annotation
import org.testng.annotations.Test;

// 👉 Import JsonPath for extracting values from JSON
import io.restassured.path.json.JsonPath;

// 👉 Import ResponseBody to store HTTP response body
import io.restassured.response.ResponseBody;

// 👉 Import RestAssured "given()" and HTTP methods
import static io.restassured.RestAssured.*;

// 👉 Import Hamcrest assertion methods
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * 👉 This class demonstrates: - Sending GET request to Book Store API - Parsing
 * nested JSON object - Extracting array values - Searching for specific items -
 * Calculating totals - Validating JSON data using assertions
 */
public class ParsingJSONObject_BookStoreAPI {

	@Test // 👉 Marks this method as a TestNG test case
	void testJsonResponseBody() {

		// 👉 Send GET request to /store endpoint, ensure HTTP 200, extract body
		ResponseBody responseBody = given()
									.when()
										.get("http://localhost:3000/store")
									.then()
										.statusCode(200)
										.extract().response().body();
				
		// 👉 Convert raw JSON response into JsonPath for easy extraction
		JsonPath jsonpath = new JsonPath(responseBody.asString());
		//JsonPath jsonpath = responseBody.jsonPath(); // Alternative way

		// 👉 Get how many books exist inside store.book array
		int bookCount = jsonpath.getInt("store.book.size()");
		System.out.println("Total books in store: " + bookCount);

		// 👉 Validate that the store contains at least one book
		assertThat(bookCount, greaterThan(0));

		// 👉 Print all book titles one by one
		System.out.println("Book titles in store:");
		for (int i = 0; i < bookCount; i++) {

			// 👉 Extract book title at index i
			String bookTitle = jsonpath.getString("store.book[" + i + "].title");

			// 👉 Print to console
			System.out.println(bookTitle);
		}

		// 👉 Variable to track if "The Lord of the Rings" is found
		boolean status = false;

		// 👉 Loop through all books to search for matching title
		for (int i = 0; i < bookCount; i++) {

			// 👉 Extract title at index i
			String bookTitle = jsonpath.getString("store.book[" + i + "].title");

			// 👉 Check if title matches the expected book
			if (bookTitle.equals("The Lord of the Rings")) {
				status = true; // 👉 Mark as found
				break; // 👉 Stop loop early
			}
		}

		// 👉 Ensure that the book exists in the store
		assertThat(status, is(true));

		// 👉 Variable to accumulate total cost of all books
		double totalPrice = 0;

		// 👉 Loop through each book to add prices
		for (int i = 0; i < bookCount; i++) {

			// 👉 Extract price of book at index i
			double bookPrice = jsonpath.getDouble("store.book[" + i + "].price");

			// 👉 Add to running total
			totalPrice += bookPrice; //totalPrice = totalPrice + bookPrice
		}

		// 👉 Print total price for verification
		System.out.println("Total price of books: " + totalPrice);

		// 👉 Validate final total calculated from JSON data
		assertThat(totalPrice, is(53.92));
	}
}
