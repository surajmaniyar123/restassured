package day11_json_include;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class UserNonDefault {

    private String username;

    private int age;  // default = 0, ignored if 0

    // Constructors
    public UserNonDefault() {}
    public UserNonDefault(String username, int age) {
        this.username = username;
        this.age = age;
    }

    // Getters & Setters
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }
}
