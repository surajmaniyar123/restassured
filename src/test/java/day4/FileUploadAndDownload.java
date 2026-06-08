package day4;

import org.testng.annotations.Test;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import java.io.File;

/**
 * This class demonstrates how to perform file upload and download operations
 * using REST Assured in a TestNG test.
 * 
 * It includes: 1. Uploading a single file 2. Uploading multiple files 3.
 * Downloading a file
 * 
 * Make sure the backend API endpoints (upload/download) are running locally.
 */
public class FileUploadAndDownload {

	// 1️⃣ Upload a Single File --This test uploads one file to the server using multipart/form-data. 
	//The server endpoint (http://localhost:8080/uploadFile) should be configured to accept file uploads via POST requests.

	@Test
	void uploadSingleFile() {
		// Create a File object pointing to the file to upload
		File myfile = new File("C:\\Automation\\automationFiles\\Test1.txt");

				given()
					.multiPart("file", myfile) // Attach the file as a multipart form data
					.contentType("multipart/form-data") // Set content type for file upload //not required as RestAssured will set it automatically
				.when()
					.post("http://localhost:8080/uploadFile") // POST request to upload the file
				.then()
					.statusCode(200) // Expect HTTP 200 OK on success
					.body("fileName", equalTo("Test1.txt")) // Validate that the response contains uploaded file name
					.log().body(); // Print the response body for verification
	}

	// 2️⃣ Upload Multiple Files --Demonstrates how to upload multiple files at once using REST Assured. 
	// The backend must be able to handle an array of files (multipart/form-data).

	@Test
	void uploadMultipleFiles() {
		// Prepare two files to upload
		File myfile1 = new File("C:\\Automation\\automationFiles\\Test1.txt");
		File myfile2 = new File("C:\\Automation\\automationFiles\\Test2.txt");

				given()
					.multiPart("files", myfile1) // Attach first file
					.multiPart("files", myfile2) // Attach second file
					.contentType("multipart/form-data") // Set content type
				.when()
					.post("http://localhost:8080/uploadMultipleFiles") // API endpoint for multiple file upload
				.then()
					.statusCode(200) // Expect success response
					.body("[0].fileName", equalTo("Test1.txt")) // Validate first file in response
					.body("[1].fileName", equalTo("Test2.txt")) // Validate second file in response
					.log().body(); // Print full response
	}

	// 3️⃣ Download a File ---This test downloads a file from the server using a GET request. 
	//The file name is passed as a path parameter. The endpoint should serve the file content for the given filename.

	@Test
	void downloadFile() {
				given()
					.pathParam("filename", "Test1.txt") // Pass the file name dynamically in the URL
				.when()
					.get("http://localhost:8080/downloadFile/{filename}") // GET request to download file
				.then()
					.statusCode(200) // Verify download success
					.log().body(); // Log response (shows file content or metadata)
	}
}
