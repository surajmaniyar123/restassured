package day9;   // Package declaration

//Alt+Shift+s =>//shortcut for getters & setters

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;   // Import annotation to ignore unknown JSON properties

@JsonIgnoreProperties(ignoreUnknown=true)   // Ignores any extra fields in JSON input not mapped to class variables
public class Student {

	// -------------------- Variables --------------------
	String name;        // Student's name
	String location;    // Student's location
    String phone;       // Student's phone number
    String courses[];   // Array of courses enrolled by the student
    
	
	// -------------------- Constructors --------------------
    
    public Student() {}   // Default constructor (no arguments)
    
    public Student(String name, String location, String phone, String[] courses)   // Parameterized constructor
    {
    	this.name = name;             // Initialize name
    	this.location = location;     // Initialize location
    	this.phone = phone;           // Initialize phone
    	this.courses = courses;       // Initialize courses array
    }	
       
	// -------------------- Getters and Setters --------------------
    
    public String getName() {              // Getter for name
		return name;
	}

	public void setName(String name) {     // Setter for name
		this.name = name;
	}

	public String getLocation() {          // Getter for location
		return location;
	}

	public void setLocation(String location) {   // Setter for location
		this.location = location;
	}

	public String getPhone() {             // Getter for phone
		return phone;
	}

	public void setPhone(String phone) {   // Setter for phone
		this.phone = phone;
	}

	public String[] getCourses() {         // Getter for courses array
		return courses;
	}

	public void setCourses(String[] courses) {   // Setter for courses array
		this.courses = courses;
	}
 
	// -------------------- toString() Method --------------------
	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();    // Using StringBuilder for efficient string concatenation
		sb.append(name).append(" ").append(location).append(" ").append(phone).append("  ");
		
		if(courses != null && courses.length > 0)  // Check if courses exist
		{
			for(String course : courses)           // Append each course to StringBuilder
			{
				sb.append(course).append("  ");
			}
		}
		
		return sb.toString();                      // Return final string representation
	}
}
