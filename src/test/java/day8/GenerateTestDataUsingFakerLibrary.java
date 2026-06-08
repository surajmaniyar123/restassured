package day8;

import org.testng.annotations.Test;
import com.github.javafaker.Faker;

public class GenerateTestDataUsingFakerLibrary {

	@Test
	void fakeDataGenerator() {

		Faker faker = new Faker(); // Create an instance of Faker to generate test data

		String fullname = faker.name().fullName(); // Generate full name, e.g., "John Doe"
		String firstname = faker.name().firstName(); // Generate first name, e.g., "John"
		String lastname = faker.name().lastName(); // Generate last name, e.g., "Doe"
		String email = faker.internet().safeEmailAddress(); // Generate safe email, not real but valid format
		String password = faker.internet().password(5, 8); // Generate password with min 5 and max 8 characters
		String phoneno = faker.phoneNumber().cellPhone(); // Generate random phone number, e.g., "(555) 123-4567"
		
		//String city=faker.address().cityName(); // Generate city name, e.g., "New York"
		//String number=faker.number().digits(10); // Generate a string of 10 random digits, e.g., "1234567890"
		
		
		System.out.println("Full Name: " + fullname); // Print full name
		System.out.println("First Name: " + firstname); // Print first name
		System.out.println("Last Name: " + lastname); // Print last name
		System.out.println("Email: " + email); // Print email
		System.out.println("Password: " + password); // Print password
		System.out.println("Phone No: " + phoneno); // Print phone number
	}

}
