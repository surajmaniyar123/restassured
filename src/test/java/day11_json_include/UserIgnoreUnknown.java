package day11_json_include;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true) // Ignore unknown JSON fields
public class UserIgnoreUnknown {

    private String username;
    private String email;

    // Constructors
    public UserIgnoreUnknown() {}

    public UserIgnoreUnknown(String username, String email) {
        this.username = username;
        this.email = email;
    }

    // Getters & Setters
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    @Override
    public String toString() {
        return "UserIgnoreUnknown{" +
                "username='" + username + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
