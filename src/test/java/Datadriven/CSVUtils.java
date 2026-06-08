package Datadriven;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class CSVUtils {

    // Generic method to read CSV file as Object[][]
    public static Object[][] getCsvData(String filePath) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(filePath));

        int rows = lines.size() - 1; // skip header
        int cols = lines.get(0).split(",").length;

        Object[][] data = new Object[rows][cols];

        for (int i = 1; i <= rows; i++) {
            String[] values = lines.get(i).split(",");
            for (int j = 0; j < cols; j++) {
                data[i - 1][j] = values[j];
            }
        }

        return data;
    }
}
