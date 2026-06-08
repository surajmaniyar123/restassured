package day10;  // Defines the package location for this class

// Importing required libraries for file handling and Excel operations
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelUtils {  // Utility class for reading, writing, and formatting Excel files (.xlsx)

    // Class variables to store Excel file path and sheet name
    public String filePath;
    public String sheetName;
    
    // Constructor to initialize file path and sheet name
    public ExcelUtils(String filePath, String sheetName) {
        this.filePath = filePath;
        this.sheetName = sheetName;
    }
    
    // Method to get the total number of rows in the Excel sheet
    public int getRowCount() throws IOException {
        // Use try-with-resources to auto-close file streams
        try (FileInputStream fi = new FileInputStream(filePath); 
             XSSFWorkbook workbook = new XSSFWorkbook(fi)) {
            
            XSSFSheet sheet = workbook.getSheet(sheetName); // Get the specified sheet
            return sheet.getLastRowNum();                   // Return last row index (zero-based)
        }
    }
    
    // Method to get the total number of cells (columns) in a given row
    public int getCellCount(int rownum) throws IOException {
        try (FileInputStream fi = new FileInputStream(filePath); 
             XSSFWorkbook workbook = new XSSFWorkbook(fi)) {
            
            XSSFSheet sheet = workbook.getSheet(sheetName); // Access the sheet
            XSSFRow row = sheet.getRow(rownum);             // Get the specified row
            return row.getLastCellNum();                    // Return number of cells in the row
        }
    }
    
    // Method to read a cell’s data as a string, regardless of its type (string, number, etc.)
    public String getCellData(int rownum, int colnum) throws IOException {
        try (FileInputStream fi = new FileInputStream(filePath); 
             XSSFWorkbook workbook = new XSSFWorkbook(fi)) {
            
            XSSFSheet sheet = workbook.getSheet(sheetName); // Get the sheet
            XSSFRow row = sheet.getRow(rownum);             // Get the specified row
            XSSFCell cell = row.getCell(colnum);            // Get the specified cell
            
            // DataFormatter helps convert different cell types (numeric, string, etc.) to string
            DataFormatter formatter = new DataFormatter();
            return formatter.formatCellValue(cell);         // Return cell value as string
        } catch (Exception e) {
            return ""; // Return empty string if any exception occurs (e.g., null cell)
        }
    }
    
    // Method to write data into a specific cell in the Excel sheet
    public void setCellData(int rownum, int colnum, String data) throws IOException {
        File xlfile = new File(filePath);  // Create file reference
        
        // If the Excel file doesn’t exist, create a new one
        if (!xlfile.exists()) {
            try (XSSFWorkbook workbook = new XSSFWorkbook(); 
                 FileOutputStream fo = new FileOutputStream(filePath)) {
                workbook.write(fo);  // Write empty workbook to file
            }
        }
        
        // Open existing file, write data, and save changes
        try (FileInputStream fi = new FileInputStream(filePath);
             XSSFWorkbook workbook = new XSSFWorkbook(fi);
             FileOutputStream fo = new FileOutputStream(filePath)) {
            
            XSSFSheet sheet = workbook.getSheet(sheetName);  // Access sheet
            
            // If sheet doesn’t exist, create it
            if (sheet == null) {
                sheet = workbook.createSheet(sheetName);
            }
            
            XSSFRow row = sheet.getRow(rownum);  // Access the specific row
            if (row == null) {
                row = sheet.createRow(rownum);   // Create new row if not found
            }
            
            XSSFCell cell = row.createCell(colnum);  // Create or overwrite cell
            cell.setCellValue(data);                 // Write new data into the cell
            
            workbook.write(fo);  // Save changes to the file
        }
    }
    
    // Method to fill a cell with green background color (e.g., for "Pass" results)
    public void fillGreenColor(int rownum, int colnum) throws IOException {
        try (FileInputStream fi = new FileInputStream(filePath);
             XSSFWorkbook workbook = new XSSFWorkbook(fi);
             FileOutputStream fo = new FileOutputStream(filePath)) {
            
            XSSFSheet sheet = workbook.getSheet(sheetName); // Get sheet
            XSSFRow row = sheet.getRow(rownum);             // Get row
            XSSFCell cell = row.getCell(colnum);            // Get cell
            
            CellStyle style = workbook.createCellStyle();   // Create cell style
            style.setFillForegroundColor(IndexedColors.GREEN.getIndex()); // Set green color
            style.setFillPattern(FillPatternType.SOLID_FOREGROUND);       // Fill the color
            
            cell.setCellStyle(style);  // Apply style to cell
            workbook.write(fo);        // Save changes
        }
    }
    
    // Method to fill a cell with red background color (e.g., for "Fail" results)
    public void fillRedColor(int rownum, int colnum) throws IOException {
        try (FileInputStream fi = new FileInputStream(filePath);
             XSSFWorkbook workbook = new XSSFWorkbook(fi);
             FileOutputStream fo = new FileOutputStream(filePath)) {
            
            XSSFSheet sheet = workbook.getSheet(sheetName); // Get sheet
            XSSFRow row = sheet.getRow(rownum);             // Get row
            XSSFCell cell = row.getCell(colnum);            // Get cell
            
            CellStyle style = workbook.createCellStyle();   // Create cell style
            style.setFillForegroundColor(IndexedColors.RED.getIndex());   // Set red color
            style.setFillPattern(FillPatternType.SOLID_FOREGROUND);       // Fill the color
            
            cell.setCellStyle(style);  // Apply style to cell
            workbook.write(fo);        // Save changes
        }
    }
}
