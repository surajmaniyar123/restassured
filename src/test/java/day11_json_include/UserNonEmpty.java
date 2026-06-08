package day11_json_include;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class UserNonEmpty {

    private String username;  // ignored if null or empty

    private String email;     // ignored if ""

    // Constructors
    public UserNonEmpty() {}
    public UserNonEmpty(String username, String email) {
        this.username = username;
        this.email = email;
    }

    // Getters & Setters
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}
