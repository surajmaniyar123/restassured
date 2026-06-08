package day11_json_include;

import com.fasterxml.jackson.databind.ObjectMapper;

public class TestUserNonDefault {
    public static void main(String[] args) throws Exception {
        UserNonDefault user = new UserNonDefault();
        user.setUsername("John");
        user.setAge(0);  // default → ignored

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(user);

        System.out.println("NON_DEFAULT JSON: " + json);
    }
}
