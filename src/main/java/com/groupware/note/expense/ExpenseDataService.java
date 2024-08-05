package com.groupware.note.expense;



import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ExpenseDataService {
	private final ExpenseExcelRepository expenseExcelRepository;
	private final ExpensePhotoRepository expensePhotoRepository;
	
	public void uploadExcelData( XSSFSheet worksheet1) {
	    for (int i = 1; i < worksheet1.getPhysicalNumberOfRows(); i++) {

		      Row row = worksheet1.getRow(i);
		      ExpenseExcel data = new ExpenseExcel();
		      data.setWriter(row.getCell(0).getStringCellValue());
		      data.setExpenseType(row.getCell(1).getStringCellValue());
		      data.setAmount(row.getCell(2).getNumericCellValue());
		      data.setUseDate(row.getCell(3).getLocalDateTimeCellValue());
		      data.setDescription(row.getCell(4).getStringCellValue());
		      this.expenseExcelRepository.save(data);
		      System.out.println( i + "체크");
		    }
	}
	
	public void uploadreceipt() {
		ExpensePhoto expensePhoto = new ExpensePhoto();
		
	}
	
	
}
