package com.infy.utility;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.util.List;

public class ExcelUtility {

	private static FileInputStream fis;
	private static FileOutputStream fileOut;
	private static Workbook workbook;
	private static Sheet sheet;
	private static Row row;
	private static Cell cell;
	private static String excelFilePath;
	private Map<String, Integer> columns = new HashMap<>();

	// Creation and initialization of the Excel file and sheet
	public void setExcelFile(String ExcelPath, String SheetName) throws Exception {
		FileInputStream fis = null;
		try {
			File file = new File(ExcelPath);

			if (!file.exists()) {
				file.createNewFile();
				System.out.println("File doesn't exist, so created!");
			}

			fis = new FileInputStream(ExcelPath);
			workbook = WorkbookFactory.create(fis);
			sheet = workbook.getSheet(SheetName);
			if (sheet == null) {
				sheet = workbook.createSheet(SheetName);
			}

			ExcelUtility.excelFilePath = ExcelPath;

			// adding all the column header names to the map 'columns'
			Row headerRow = sheet.getRow(0);
			if (headerRow != null) {
				headerRow.forEach(cell -> {
					columns.put(cell.getStringCellValue(), cell.getColumnIndex());
				});
			}

		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
					System.out.println("Error closing FileInputStream: " + e.getMessage());
				}
			}
		}
	}

	// Method to read the excel cell data - we are overloading the getCellData
	// method.
	public String getCellData(int rownum, int colnum) throws Exception {
		try {
			cell = sheet.getRow(rownum).getCell(colnum);
			String CellData = null;
			switch (cell.getCellType()) {
			case STRING:
				CellData = cell.getStringCellValue();
				break;
			case NUMERIC:
				if (DateUtil.isCellDateFormatted(cell)) {
					CellData = String.valueOf(cell.getDateCellValue());
				} else {
					CellData = String.valueOf((long) cell.getNumericCellValue());
				}
				break;
			case BOOLEAN:
				CellData = Boolean.toString(cell.getBooleanCellValue());
				break;
			case BLANK: // This represents NULL in Excel
				CellData = "";
				break;
			}
			return CellData;
		} catch (Exception e) {
			return "";
		}
	}

	public String getCellData(String columnName, int rownum) throws Exception {
		return getCellData(rownum, columns.get(columnName));
	}

	public String readCellData(String sheetName, int rowNum, int colNum) throws Exception {
		Sheet sheet = workbook.getSheet(sheetName);
		Row row = sheet.getRow(rowNum);
		if (row == null) {
			return null; // Return null if row doesn't exist
		}
		Cell cell = row.getCell(colNum);
		return getCellData(rowNum, colNum);
	}

	// To retrieves the number of rows in a specified row of an Excel sheet.
	public int getRowCount(String sheetName) {
		Sheet sheet = workbook.getSheet(sheetName);
		return (sheet != null) ? sheet.getLastRowNum() + 1 : 0;
	}

	// To retrieves the number of columns in a specified row of an Excel sheet.
	public int getColumnCount(String sheetName, int rowNum) {
		Sheet sheet = workbook.getSheet(sheetName);
		Row row = sheet.getRow(rowNum);
		return (row != null) ? row.getPhysicalNumberOfCells() : 0;
	}

	// Checks if a cell in the specified sheet is empty by using the getCellData
	// method.
	public boolean isCellEmpty(String sheetName, int rowNum, int colNum) throws Exception {
		String cellValue = getCellData(rowNum, colNum);
		return cellValue == null || cellValue.isEmpty();
	}

	// Method to write data to a cell
	public void setCellData(String Result, int RowNum, int ColNum) throws Exception {
		FileOutputStream fileOut = null;
		try {
			row = sheet.getRow(RowNum);
			cell = row.getCell(ColNum, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
			if (cell == null) {
				cell = row.createCell(ColNum);
				cell.setCellValue(Result);
			} else {
				cell.setCellValue(Result);
			}
			fileOut = new FileOutputStream(excelFilePath);
			workbook.write(fileOut);
		} catch (Exception e) {
			throw (e);
		} finally {
			if (fileOut != null) {
				fileOut.flush();
				fileOut.close();
			}
		}
	}

	public static List<Map<String, String>> readExcelMap(String filePath) throws IOException {
		List<Map<String, String>> data = new ArrayList<>();
		FileInputStream file = new FileInputStream(new File(filePath));
		Workbook workbook = new XSSFWorkbook(file);
		Sheet sheet = workbook.getSheetAt(0);

		Row headerRow = sheet.getRow(0);
		for (int i = 1; i <= sheet.getLastRowNum(); i++) {
			Row currentRow = sheet.getRow(i);
			Map<String, String> rowData = new HashMap<>();
			for (int j = 0; j < currentRow.getLastCellNum(); j++) {
				Cell currentCell = currentRow.getCell(j);
				String cellValue = "";
				if (currentCell != null) {
					switch (currentCell.getCellType()) {
					case STRING:
						cellValue = currentCell.getStringCellValue();
						break;
					case NUMERIC:
						if (DateUtil.isCellDateFormatted(currentCell)) {
							cellValue = currentCell.getDateCellValue().toString();
						} else {
							cellValue = String.valueOf(currentCell.getNumericCellValue());
						}
						break;
					case BOOLEAN:
						cellValue = String.valueOf(currentCell.getBooleanCellValue());
						break;
					case FORMULA:
						cellValue = currentCell.getCellFormula();
						break;
					default:
						cellValue = "";
					}
				}
				rowData.put(headerRow.getCell(j).getStringCellValue(), cellValue);
			}
			data.add(rowData);
		}
		workbook.close();
		return data;
	}

	public static void writeExcelMap(String filePath, List<Map<String, String>> data) throws IOException {
	    Workbook workbook;
	    
	    // Read existing file
	    try (FileInputStream fileInputStream = new FileInputStream(filePath)) {
	        workbook = new XSSFWorkbook(fileInputStream);
	    }

	    Sheet sheet = workbook.getSheetAt(0); // Assuming the first sheet contains the data

	    // Ensure headers are set correctly; assume first row is headers
	    Row headerRow = sheet.getRow(0);
	    Map<String, Integer> columnMap = new HashMap<>();
	    
	    // Create a mapping of column headers to column indices
	    for (int cellIndex = 0; cellIndex < headerRow.getPhysicalNumberOfCells(); cellIndex++) {
	        Cell cell = headerRow.getCell(cellIndex);
	        if (cell != null) {
	            columnMap.put(cell.getStringCellValue(), cellIndex);
	        }
	    }

	    // Update existing rows based on data size
	    for (int rowIndex = 0; rowIndex < data.size(); rowIndex++) {
	        Row row;
	        // Check if the row already exists; if so, update it; otherwise, create a new row
	        if (rowIndex + 1 < sheet.getPhysicalNumberOfRows()) {
	            row = sheet.getRow(rowIndex + 1); // +1 to skip the header row
	        } else {
	            row = sheet.createRow(rowIndex + 1); // Create a new row if it doesn't exist
	        }

	        Map<String, String> userDetails = data.get(rowIndex);

	        // Write each value in the correct column based on the header
	        for (Map.Entry<String, String> entry : userDetails.entrySet()) {
	            String columnName = entry.getKey();
	            String value = entry.getValue();

	            // Find the correct column index
	            Integer columnIndex = columnMap.get(columnName);
	            if (columnIndex != null) {
	                Cell cell = row.createCell(columnIndex);
	                cell.setCellValue(value);
	            }
	        }
	    }

	    // Write to the output file
	    try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
	        workbook.write(fileOut);
	    } finally {
	        workbook.close();
	    }
	}


	/**
	 * Utility class for comparing two Excel sheets and printing the differences
	 * into a third Excel sheet. Provided methods for reading data from Excel files,
	 * comparing the data, and writing the differences into a new Excel file. Reads
	 * data from the specified Excel file.
	 * 
	 * @param filePath the path to the Excel file
	 * @return a list of string arrays, where each array represents a row of data
	 * @throws IOException if an I/O error occurs while reading the file
	 */

	public static List<String[]> readExcelList(String filePath) throws IOException {
		List<String[]> data = new ArrayList<>();
		try (FileInputStream file = new FileInputStream(filePath)) {
			Workbook workbook = new XSSFWorkbook(file);
			Sheet sheet = workbook.getSheetAt(0);
			for (Row row : sheet) {
				String[] rowData = new String[row.getLastCellNum()];
				for (int i = 0; i < row.getLastCellNum(); i++) {
					Cell cell = row.getCell(i);
					rowData[i] = cell != null ? cell.toString() : "";
				}
				data.add(rowData);
			}
		}
		return data;
	}

	/**
	 * Writes data to the specified Excel file.
	 * 
	 * @param filePath the path to the Excel file
	 * @param data     a list of string arrays, where each array represents a row of
	 *                 data
	 * @throws IOException if an I/O error occurs while writing to the file
	 */
	public static void writeExcel(String filePath, List<String[]> data) throws IOException {
		Workbook workbook = new XSSFWorkbook();
		Sheet sheet = workbook.createSheet("Differences");

		int rowNum = 0;
		for (String[] rowData : data) {
			Row row = sheet.createRow(rowNum++);
			int cellNum = 0;
			for (String cellData : rowData) {
				Cell cell = row.createCell(cellNum++);
				cell.setCellValue(cellData);
			}
		}

		try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
			workbook.write(fileOut);
		}
		workbook.close();
	}

	/**
	 * Compares data from two Excel files and returns the differences.
	 * 
	 * @param data1 a list of string arrays representing the first Excel sheet data
	 * @param data2 a list of string arrays representing the second Excel sheet data
	 * @return a list of string arrays representing the differences, where each
	 *         array contains the row index, column index, value from the first
	 *         sheet, and value from the second sheet
	 */
	public static List<String[]> compareExcelData(List<String[]> data1, List<String[]> data2) {
		List<String[]> differences = new ArrayList<>();
		int maxRows = Math.max(data1.size(), data2.size());

		for (int i = 0; i < maxRows; i++) {
			String[] row1 = i < data1.size() ? data1.get(i) : new String[0];
			String[] row2 = i < data2.size() ? data2.get(i) : new String[0];

			int maxCols = Math.max(row1.length, row2.length);
			for (int j = 0; j < maxCols; j++) {
				String cell1 = j < row1.length ? row1[j] : "";
				String cell2 = j < row2.length ? row2[j] : "";
				if (!cell1.equals(cell2)) {
					differences.add(new String[] { Integer.toString(i), Integer.toString(j), cell1, cell2 });
				}
			}
		}
		return differences;
	}

	/**
	 * Reads data from the specified Excel file.
	 * 
	 * @param filePath the path to the Excel file
	 * @return a map where the key is the primary key and the value is an array of
	 *         data
	 * @throws IOException if an I/O error occurs while reading the file
	 */
	public static Map<String, String[]> readExcel(String filePath, int primaryKeyIndex) throws IOException {
		Map<String, String[]> data = new HashMap<>();
		try (FileInputStream file = new FileInputStream(filePath)) {
			Workbook workbook = new XSSFWorkbook(file);
			Sheet sheet = workbook.getSheetAt(0);
			for (Row row : sheet) {
				String primaryKey = row.getCell(primaryKeyIndex).toString();
				String[] rowData = new String[row.getLastCellNum()];
				for (int i = 0; i < row.getLastCellNum(); i++) {
					Cell cell = row.getCell(i);
					rowData[i] = cell != null ? cell.toString() : "";
				}
				data.put(primaryKey, rowData);
			}
		}
		return data;
	}

	/**
	 * Compares data from two Excel files based on a primary key and returns the
	 * differences.
	 * 
	 * @param data1 a map where the key is the primary key and the value is an array
	 *              of data from the first Excel sheet
	 * @param data2 a map where the key is the primary key and the value is an array
	 *              of data from the second Excel sheet
	 * @return a list of string arrays representing the differences, where each
	 *         array contains the primary key, column index, value from the first
	 *         sheet, and value from the second sheet
	 */
	public static List<String[]> compareExcelData(Map<String, String[]> data1, Map<String, String[]> data2) {
		List<String[]> differences = new ArrayList<>();

		for (String primaryKey : data1.keySet()) {
			String[] row1 = data1.get(primaryKey);
			String[] row2 = data2.getOrDefault(primaryKey, new String[0]);

			int maxCols = Math.max(row1.length, row2.length);
			for (int j = 0; j < maxCols; j++) {
				String cell1 = j < row1.length ? row1[j] : "";
				String cell2 = j < row2.length ? row2[j] : "";
				if (!cell1.equals(cell2)) {
					differences.add(new String[] { primaryKey, Integer.toString(j), cell1, cell2 });
				}
			}
		}

		for (String primaryKey : data2.keySet()) {
			if (!data1.containsKey(primaryKey)) {
				String[] row2 = data2.get(primaryKey);
				for (int j = 0; j < row2.length; j++) {
					differences.add(new String[] { primaryKey, Integer.toString(j), "", row2[j] });
				}
			}
		}

		return differences;
	}

	 public static void writeDataToExcel(Map<String, String[]> data, String filePath) throws Exception {
	        try (Workbook workbook = new XSSFWorkbook()) {
	            Sheet sheet = workbook.createSheet("Account Data");
	            createHeaderRow(sheet);

	            int rowCount = 1;  // Start from row 1 since row 0 will be the header
	            for (Map.Entry<String, String[]> entry : data.entrySet()) {
	                Row row = sheet.createRow(rowCount++);
	                writeRowData(row, entry.getKey(), entry.getValue());
	            }

	            saveExcelFile(workbook, filePath);
	        }
	    }

	    private static void createHeaderRow(Sheet sheet) {
	        Row headerRow = sheet.createRow(0);
	        headerRow.createCell(0).setCellValue("Account Number");
	        headerRow.createCell(1).setCellValue("Balance");
	        headerRow.createCell(2).setCellValue("Available Balance");
	    }

	    private static void writeRowData(Row row, String accountNumber, String[] values) {
	        row.createCell(0).setCellValue(accountNumber);  // Account Number
	        if (values.length >= 2) {
	            row.createCell(1).setCellValue(values[0]);  // Balance
	            row.createCell(2).setCellValue(values[1]);  // Available Balance
	        }
	    }

	    private static void saveExcelFile(Workbook workbook, String filePath) throws Exception {
	        try (FileOutputStream outputStream = new FileOutputStream(filePath)) {
	            workbook.write(outputStream);
	            System.out.println("Excel file written successfully: " + filePath);
	        }
	    }
	
	
	
	
	/*
	 * public static void main(String args[]) throws Exception { excelReader er =
	 * new excelReader(); er.setExcelFile("./TestData.xlsx", "TestData");
	 * System.out.println(er.getColumnCount("TestData", 0));
	 * System.out.println(er.isCellEmpty("TestData", 1, 1));
	 * System.out.println(er.getRowCount("TestData"));
	 * System.out.println(er.readCellData("TestData", 1, 1));
	 * er.setCellData("Tamil", 1, 1); System.out.println(er.readCellData("TestData",
	 * 1, 1)); }
	 */
}
