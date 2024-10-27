package com.readExcel;

import java.io.File;
import java.io.FileInputStream;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.testng.annotations.Test;

public class ReadExcel {

	@Test
	public void readExcel() throws Exception {
		
		String excelPath = System.getProperty("user.dir")+"/TestData/TestData.xlsx";
		String sheetName="Sheet1";
		//Create the object of File Class to get the excel path
		
		//FileInputStream fis= new FileInputStream(new File(excelPath));
		
		File file= new File(excelPath);
		//To read the file
		FileInputStream fis= new FileInputStream(file);
		XSSFWorkbook wb= new XSSFWorkbook(fis);
		XSSFSheet sheet=wb.getSheet(sheetName);
		//Get Total Rows in Excel Sheet
		int rowCount=sheet.getLastRowNum();
		System.out.println("Get First Header : "+sheet.getFirstHeader());
		//Print a particular cell value
		try {
			String data=sheet.getRow(0).getCell(1).getStringCellValue();
			System.out.println("Particular cell value: "+data);
		} catch (Exception e) {
			System.out.println("Particular cell value has no data " + e);
		}
		
		//Loop to print all values of the excel sheet
		for(int i=0; i<=rowCount;i++) {
			Row row=sheet.getRow(i);
			for(int j=0; j<row.getLastCellNum();j++) {
				String data1=sheet.getRow(i).getCell(j).getStringCellValue();
				System.out.print(data1+" ");
			}
			System.out.println();
		}
		wb.close();
	}

}