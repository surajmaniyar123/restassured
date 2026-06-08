package day11_json_include;

import com.fasterxml.jackson.databind.ObjectMapper;

public class TestUserIgnore {
    public static void main(String[] args) throws Exception {
        UserIgnore user = new UserIgnore("JohnDoe", "john@example.com", "mypassword");

        ObjectMapper mapper = new ObjectMapper();

        // Serialize to JSON
        String json = mapper.writeValueAsString(user);
        System.out.println("Serialized JSON:");
        System.out.println(json);

        // Deserialize JSON (password will be ignored)
        String jsonInput = "{\"username\":\"Jane\",\"email\":\"jane@example.com\",\"password\":\"secret\"}";
        UserIgnore deserializedUser = mapper.readValue(jsonInput, UserIgnore.class);
        System.out.println("Deserialized Object:");
        System.out.println(deserializedUser);
    }
}
