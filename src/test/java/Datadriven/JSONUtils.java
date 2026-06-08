package Datadriven;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JSONUtils {

    // Generic method to read JSON array file as Object[][]
    public static Object[][] getJsonData(String filePath) throws IOException {
        String json = new String(Files.readAllBytes(Paths.get(filePath)));
        ObjectMapper mapper = new ObjectMapper();
        Map<String, String>[] arr = mapper.readValue(json, Map[].class);

        Object[][] data = new Object[arr.length][1]; // each map goes in one column
        for (int i = 0; i < arr.length; i++) {
            data[i][0] = arr[i];
        }
        return data;
    }
}
