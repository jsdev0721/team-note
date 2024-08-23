package com.groupware.note.expense;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.groupware.note.files.FileService;
import com.groupware.note.files.Files;
import com.groupware.note.user.UserDetails;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
@RequestMapping("/expense")
public class ExpenseDataController {
	private final FileService fService;
	private final ExpenseDataService edService;
	
	@GetMapping("/lists")
	public String dateList(Model model, @RequestParam(value="date", defaultValue = "") LocalDateTime dt) {
		List<Expense> list = new ArrayList<>();
		list = this.edService.dateList(dt);
		model.addAttribute("expenseList", list);
		return "expenseList";
	}
	

	
	@GetMapping("/list")
	public String expenseList(Model model, @RequestParam(value="st" , defaultValue = "") String st) {
		List<Expense>list = new ArrayList<>();
		if(st.contains("@")) {
			list = edService.nameList(st);
		} else if(!st.equals("")) {
			list = edService.depList(st);
		}else {
			list = edService.list();	
		}
		model.addAttribute("expenseList", list);
		return "expenseList";
	}
	
	@PostMapping("/list")
	public String barSearchrList(Model model, @Valid ExpenseBarSearchedListForm ebslForm ) {
		List<Expense> list = edService.barSearchedList(ebslForm.getBrowse(), ebslForm.getSearch());
		model.addAttribute("expenseList", list);
		return "expenseList";
	}
	
	
	@PostMapping("/upload")
	public String upLoadExpense(@Valid ExpenseForm exForm,  Model model) throws IOException{
		
		Files photoReceipt = null;
		InputStream is = null;
		String errorMessage = null;
			
		for(MultipartFile multipartFile : exForm.getMultipartFiles()) {
			if(fService.validFileExtension(fService.extendsFile(multipartFile.getOriginalFilename()))) {
				photoReceipt = fService.uploadFile(multipartFile);
			} else if(fService.extendsFile(multipartFile.getOriginalFilename()).equals("xlsx")) {
				is = multipartFile.getInputStream();
			} else {
				errorMessage = "파일 종류를 다시 확인해주세요";
				model.addAttribute("errorMessage", errorMessage);
				return "expenseError";
			}
				
		}
		if(photoReceipt==null) {
			errorMessage = "사진 파일을 같이 업로드 해 주세요";
			model.addAttribute("errorMessage", errorMessage);
			return "expenseError";
		}
		
		if(is == null) {
			this.fService.delete(photoReceipt);
			errorMessage = "엑셀 파일을 같이 업로드 해 주세요";
			model.addAttribute("errorMessage", errorMessage);
			return "expenseError";
		}
				
		
		XSSFWorkbook excelworkbook = new XSSFWorkbook(is);
		XSSFSheet worksheet = excelworkbook.getSheetAt(0);
		edService.uploadExpenseData(worksheet, photoReceipt);
		
		
		return "redirect:/expense/list";
  }
	
	
	@GetMapping("/photo/{fileId}")
	public ResponseEntity<Resource> photo(@PathVariable("fileId") Integer id) throws MalformedURLException {
		Files file = this.fService.findByFiles(id);
		return this.fService.photoView(file);
	}
}