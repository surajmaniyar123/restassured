package day2;

//POJO - Plain Old Java Object  
//This class holds student data for serialization/deserialization

public class StudentPojo {

    private String name;
    private String location;
    private String phone;
    private String[] courses;

    // Default constructor (required for deserialization)
    public StudentPojo() {}

    // Optional: Parameterized constructor
    public StudentPojo(String name, String location, String phone, String[] courses) {
        this.name = name;
        this.location = location;
        this.phone = phone;
        this.courses = courses;
    }

    // Getters & setters
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }
    public void setLocation(String location) {
        this.location = location;
    }

    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String[] getCourses() {
        return courses;
    }
    public void setCourses(String[] courses) {
        this.courses = courses;
    }
}
