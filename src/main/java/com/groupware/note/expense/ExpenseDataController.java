package com.groupware.note.expense;

import java.io.IOException;

import org.apache.commons.io.FilenameUtils;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/expense")
public class ExpenseDataController {
	
	private final ExpenseDataService expenseDataService;
	  @GetMapping("/dataUpload")
	  public String main() {
		  return "expenseUpload";
	  }


	  @PostMapping("/upload")
	  public String readExcel(@RequestParam("excelFile") MultipartFile excelfile, Model model) throws IOException{
		  String extension1 = FilenameUtils.getExtension(excelfile.getOriginalFilename());

		  if (!extension1.equals("xlsx")) {
			  throw new IOException("엑셀파일만 업로드 해주세요.");
		  }

		  XSSFWorkbook excelworkbook =  new XSSFWorkbook(excelfile.getInputStream());
		  XSSFSheet worksheet1 = excelworkbook.getSheetAt(0);
		  
		  expenseDataService.uploadExcelData(worksheet1);
		  return "main";
	  }
}