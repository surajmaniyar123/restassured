package day10;  // Defines the package location for this class

// Import required libraries
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.testng.annotations.DataProvider;  // TestNG annotation for data providers

import com.fasterxml.jackson.core.type.TypeReference;  // For mapping JSON to Java objects
import com.fasterxml.jackson.databind.ObjectMapper;    // For JSON parsing

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class DataProviders {  // Class providing test data for TestNG tests

    // -------------------- EXCEL DATA PROVIDER --------------------
    @DataProvider  // Marks this method as a TestNG data provider
    public Object[][] excelDataProvider() throws IOException {
        // Path to the Excel file containing test data
        String path = ".\\testdata\\orders_excel_data.xlsx";
        
        // Create an instance of ExcelUtils for the specified sheet
        ExcelUtils xl = new ExcelUtils(path, "Sheet1");

        int rownum = xl.getRowCount();        // Get total number of rows
        int colcount = xl.getCellCount(1);   // Get total number of columns (using row index 1)

        Object dataArray[][] = new Object[rownum][colcount];  // Initialize Object array to hold test data

        // Loop through all rows and columns to populate the Object array
        for (int i = 1; i <= rownum; i++) {            // Start from 1 to skip header row
            for (int j = 0; j < colcount; j++) {
                dataArray[i - 1][j] = xl.getCellData(i, j);  // Read cell data and store in array
            }
        }

        return dataArray;  // Return data array to be used in test methods
    }

    // -------------------- JSON DATA PROVIDER --------------------
    @DataProvider
    public Object[][] jsonDataProvider() throws IOException {
        // Path to JSON test data file
        String filePath = ".\\testdata\\orders_json_data.json";

        // ObjectMapper to parse JSON into Java objects
        ObjectMapper objectMapper = new ObjectMapper();
        
        // Map JSON array into a List of Maps (each Map represents a JSON object)
        List<Map<String, String>> dataList = objectMapper.readValue(
            new File(filePath),
            new TypeReference<List<Map<String, String>>>() {}
        );

        // Convert List<Map<String, String>> to Object[][] for TestNG
        Object[][] dataArray = new Object[dataList.size()][];
        for (int i = 0; i < dataList.size(); i++) {
            dataArray[i] = new Object[] { dataList.get(i) };  // Each row contains one Map
        }

        return dataArray;  // Return JSON data array for tests
    }

    // -------------------- CSV DATA PROVIDER --------------------
    @DataProvider
    public Object[][] csvDataProvider() throws IOException {
        // Path to CSV file containing test data
        String filePath = ".\\testdata\\orders_csv_data.csv";

        // List to store CSV data temporarily
        List<String[]> dataList = new ArrayList<>();
        
        // Read the CSV file using BufferedReader
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            br.readLine();  // Skip the first line (header row)

            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");  // Split each line by comma
                dataList.add(data);               // Add row data to list
            }
        }

        // Convert List<String[]> to Object[][] for TestNG
        Object[][] dataArray = new Object[dataList.size()][];
        for (int i = 0; i < dataList.size(); i++) {
            dataArray[i] = dataList.get(i);  // Assign each row from list to Object array
        }

        return dataArray;  // Return CSV data array for tests
    }
}
