package day11_json_include;

import com.fasterxml.jackson.databind.ObjectMapper;

public class TestUserNonEmpty {
    public static void main(String[] args) throws Exception {
        UserNonEmpty user = new UserNonEmpty();
        user.setUsername("");   // empty → ignored
        user.setEmail("user@example.com");

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(user);

        System.out.println("NON_EMPTY JSON: " + json);
    }
}
