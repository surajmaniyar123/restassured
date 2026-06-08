package Datadriven;

import java.io.IOException;
import org.testng.annotations.DataProvider;

public class DataProviders {

    @DataProvider(name = "excelDataProvider")
    public static Object[][] excelDataProvider() throws IOException {
        return ExcelUtils.getExcelData(".\\testdata\\orders_excel_data.xlsx", "Sheet1");
    }

    @DataProvider(name = "jsonDataProvider")
    public static Object[][] jsonDataProvider() throws IOException {
        return JSONUtils.getJsonData(".\\testdata\\orders_json_data.json");
    }

    @DataProvider(name = "csvDataProvider")
    public static Object[][] csvDataProvider() throws IOException {
        return CSVUtils.getCsvData(".\\testdata\\orders_csv_data.csv");
    }
}
