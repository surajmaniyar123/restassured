package day11_json_include;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserNonNull {

    private String username;  // ignored if null

    private String email;     // included normally

    // Constructors
    public UserNonNull() {}

    public UserNonNull(String username, String email) {
        this.username = username;
        this.email = email;
    }

    // Getters & Setters
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}
