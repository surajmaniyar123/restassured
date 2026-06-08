package day11_json_include;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class UserIgnore {

    private String username;

    private String email;

    @JsonIgnore
    private String password;  // This field will be ignored in JSON

    // Constructors
    public UserIgnore() {}

    public UserIgnore(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    // Getters & Setters
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    @Override
    public String toString() {
        return "UserIgnore{" +
                "username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
