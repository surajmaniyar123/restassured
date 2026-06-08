package day11_json_include;

import com.fasterxml.jackson.databind.ObjectMapper;

public class TestUserIgnoreUnknown {
    public static void main(String[] args) throws Exception {

        String jsonInput = "{"
                + "\"username\":\"JohnDoe\","
                + "\"email\":\"john@example.com\","
                + "\"password\":\"mypassword\","
                + "\"age\":30"
                + "}";

        ObjectMapper mapper = new ObjectMapper();

        // Deserialize JSON ignoring unknown fields (password, age)
        UserIgnoreUnknown user = mapper.readValue(jsonInput, UserIgnoreUnknown.class);

        System.out.println("Deserialized Object:");
        System.out.println(user);
    }
}
