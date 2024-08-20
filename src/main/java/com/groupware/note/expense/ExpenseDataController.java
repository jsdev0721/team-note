package com.groupware.note.expense;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import com.groupware.note.files.FileService;
import com.groupware.note.files.Files;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/expense")
public class ExpenseDataController {
	private final FileService fService;
	private final ExpenseDataService edService;

	
	@GetMapping("/list")
	public String expenseList(Model model) {
		List<Expense> list = edService.list();
		model.addAttribute("expenseList", list);
		return "expenseList";
	}
	
	@GetMapping("/Upload")
	public String upLoad(ExpenseForm exForm) {
		return "expenseUpload";
	}

	@PostMapping("/upload")
	public String upLoadExpense(@Valid ExpenseForm exForm) throws IOException{
		
		Files photoReceipt = null;
			InputStream is = null;
		for(MultipartFile multipartFile : exForm.getMultipartFiles()) {
			if(fService.validFileExtension(fService.extendsFile(multipartFile.getName()))) {
				photoReceipt = fService.uploadPhoto(multipartFile);
			} else if(fService.extendsFile(multipartFile.getName()).equals("xlsx")) {
				is = multipartFile.getInputStream();
			} else {
				throw new IOException("파일을 다시 확인해주세요");
			}
				
		}
		if(photoReceipt==null) {
			throw new IOException("사진 파일을 같이 업로드 해 주세요");
		}
		
		if(is == null) {
			throw new IOException("엑셀 파일을 같이 업로드 해 주세요");
		}
				
		
		XSSFWorkbook excelworkbook = new XSSFWorkbook(is);
		XSSFSheet worksheet = excelworkbook.getSheetAt(0);
		edService.uploadExpenseData(worksheet, photoReceipt);
		
		
		return "expenseList";
  }
}



//String _extension1 = FilenameUtils.getExtension(exForm.getExcelfile().getOriginalFilename());
//
//if (!_extension1.equals("xlsx")) {
//	throw new IOException("엑셀파일(xlsx)만 업로드 해주세요.");
//}