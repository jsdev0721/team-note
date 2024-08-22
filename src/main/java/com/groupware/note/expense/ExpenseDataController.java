package com.groupware.note.expense;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.List;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import com.groupware.note.files.FileService;
import com.groupware.note.files.Files;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
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
	
	@PostMapping("/list")
	public String searchedExpenseList(Model model) {
		List<Expense> list = edService.list();
		model.addAttribute("expenseList", list);
		return "expenstList";
	}
	
	
	@PostMapping("/upload")
	public String upLoadExpense(@Valid ExpenseForm exForm, BindingResult bindingResult) throws IOException{
		if(bindingResult.hasErrors()) {
			return "expenseList";
		}
		
		Files photoReceipt = null;
			InputStream is = null;
		for(MultipartFile multipartFile : exForm.getMultipartFiles()) {
			if(fService.validFileExtension(fService.extendsFile(multipartFile.getOriginalFilename()))) {
				photoReceipt = fService.uploadFile(multipartFile);
			} else if(fService.extendsFile(multipartFile.getOriginalFilename()).equals("xlsx")) {
				is = multipartFile.getInputStream();
			} else {
//				bindingResult.rejectValue(null, null, null); 이거 써서 해결해보기
				return "redirect:/expense/list";
//				throw new IOException("파일을 다시 확인해주세요");
			}
				
		}
		if(photoReceipt==null) {
			throw new IOException("사진 파일을 같이 업로드 해 주세요");
		}
		
		if(is == null) {
			this.fService.delete(photoReceipt);
			throw new IOException("엑셀 파일을 같이 업로드 해 주세요");
		}
				
		
		XSSFWorkbook excelworkbook = new XSSFWorkbook(is);
		XSSFSheet worksheet = excelworkbook.getSheetAt(0);
		edService.uploadExpenseData(worksheet, photoReceipt);
		
		
		return "redirect:/expense/list";
  }
	
	@GetMapping("/photo/{id}")
	public ResponseEntity<Resource> photo(@PathVariable("id") Integer id) throws MalformedURLException {
		Files file = this.fService.findByFiles(id);
		return this.fService.photoView(file);
	}
}