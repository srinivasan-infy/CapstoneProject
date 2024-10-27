package WriteExcel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class writeExcel {
	
	public void WriteExcel(String SheetName, String cellValue, int row, int col) throws Exception {
		
		String excelPath = System.getProperty("user.dir")+"/TestData/TestData.xlsx";

		File file= new File(excelPath);
		FileInputStream fis = new FileInputStream(file);
		XSSFWorkbook wb = new XSSFWorkbook(fis);
		XSSFSheet sheet = wb.getSheet(SheetName);
		
		sheet.getRow(row).createCell(col).setCellValue(cellValue);
		wb.close();
	}

}
