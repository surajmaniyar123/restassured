package day11_json_include;

import com.fasterxml.jackson.databind.ObjectMapper;

public class TestUserNonNull {
    public static void main(String[] args) throws Exception {
        UserNonNull user = new UserNonNull();
        user.setUsername(null); // null → ignored
        user.setEmail("user@example.com");

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(user);

        System.out.println("NON_NULL JSON: " + json);
    }
}
