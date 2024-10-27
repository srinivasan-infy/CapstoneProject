package com.readExcel;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelLibrary {
	
	XSSFWorkbook wb;
	XSSFSheet sheet;
	
	//@SuppressWarnings("unused")
	public ExcelLibrary() throws IOException {
		
		String excelPath = System.getProperty("user.dir")+"\\TestData\\TestData.xlsx";
		File file= new File(excelPath);
		FileInputStream fis = new FileInputStream(file);
		wb = new XSSFWorkbook(fis);
	}
	
	public String readData(String sheetName, int row, int col) throws IOException {
		sheet = wb.getSheet(sheetName);
		String data = sheet.getRow(row).getCell(col).getStringCellValue();
		wb.close();
		return data;
	}
	
}

