package com.groupware.note.expense;

import java.net.MalformedURLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import com.groupware.note.department.DepartmentRepository;
import com.groupware.note.department.DepartmentService;
import com.groupware.note.department.Departments;
import com.groupware.note.files.FileService;
import com.groupware.note.files.Files;
import com.groupware.note.welfaremall.Purchase;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
@RequestMapping("/expense")
public class ExpenseController {
	private final FileService fService;
	private final ExpenseDataService edService;
	private final PurchaseDataService pdService;
	private final WellfareInputService wfiService;
	private final DepartmentService dService;
	
	@GetMapping("/menu")
	public String expenseMenu() {
		return "expense/expenseMenu";
	}
	
	@GetMapping("/purchaseList")
	public String purchaseList(Model model, @RequestParam(value="pt" , defaultValue = "") String pt) {
		List<Purchase> list = new ArrayList<>();
		if(!pt.equals("")) {
			list= this.pdService.ListByType(pt);
		}else {
			list = this.pdService.basicList();
		}
		model.addAttribute("purchaseList", list);
		return "expense/purchaseList";
	}
	
	@GetMapping("/wellfarePoint")
	public String wellfarepointInput(Model model, PointInputForm pointInputForm) {
		List<String> depList = new ArrayList<>();
		depList = this.wfiService.calDepPoint();
		model.addAttribute("DeppList", depList);
		WellfarePointInput wfpi = this.wfiService.wellfarePointInput();
		model.addAttribute("wfPointInput", wfpi);
		List<Departments> dep = this.dService.findAll();
		model.addAttribute("dep", dep);
		return "expense/wellfarepoint";
	}
	
	@PostMapping("/wellfarePoint")
	public String wellfarepointInput(@Valid PointInputForm pointInputForm, BindingResult bindingResult) {
		if(bindingResult.hasErrors()) {
			System.out.println("=======================================================");
			return "expense/wellfarepoint";
		}
		this.wfiService.updatePoint(pointInputForm.getDepPointPer(), pointInputForm.getDepPointPlus(), pointInputForm.getIndividualPoint());
		System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
		return "redirect:/expense/wellfarePoint";
	}
	
	@GetMapping("/lists")
	public String dateList(Model model, @RequestParam(value="date", defaultValue = "") LocalDateTime dt) {
		List<Expense> list = new ArrayList<>();
		list = this.edService.dateList(dt);
		model.addAttribute("expenseList", list);
		return "expense/expenseList";
	}
	
	@PostMapping("/list/date")
	public String dateSetList(Model model, @Valid DateSetListForm dslForm) {
		List<Expense>list = new ArrayList<>();
		
		LocalDateTime startDate = dslForm.getStartDate().atStartOfDay();
		LocalDateTime endDate = dslForm.getEndDate().atStartOfDay();
		
		list = edService.dateSetList(startDate, endDate);
		
		model.addAttribute("expenseList", list);
		return "expense/expenseList";
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
		return "expense/expenseList";
	}
	
	@PostMapping("/list")
	public String barSearchrList(Model model, @Valid ExpenseBarSearchedListForm ebslForm ) {
		List<Expense> list = edService.barSearchedList(ebslForm.getBrowse(), ebslForm.getSearch());
		model.addAttribute("expenseList", list);
		return "expense/expenseList";
	}
	
	
//	@PostMapping("/upload")
//	public String upLoadExpense(@Valid ExpenseForm exForm,  Model model) throws IOException{
//		System.out.println("=============1==========");
//		Files photoReceipt = null;
//		InputStream is = null;
//		String errorMessage = null;
//			
//		for(MultipartFile multipartFile : exForm.getMultipartFiles()) {
//			if(fService.validFileExtension(fService.extendsFile(multipartFile.getOriginalFilename()))) {
//				System.out.println("=============2==========");
//				photoReceipt = fService.uploadFile(multipartFile);
//			} else if(fService.extendsFile(multipartFile.getOriginalFilename()).equals("xlsx")) {
//				System.out.println("=============3==========");
//				is = multipartFile.getInputStream();
//			} else {
//				errorMessage = "파일 종류를 다시 확인해주세요";
//				model.addAttribute("errorMessage", errorMessage);
//				return "expense/expenseError";
//			}
//				
//		}
//		if(photoReceipt==null) {
//			errorMessage = "사진 파일을 같이 업로드 해 주세요";
//			model.addAttribute("errorMessage", errorMessage);
//			return "expense/expenseError";
//		}
//		
//		if(is == null) {
//			this.fService.delete(photoReceipt);
//			errorMessage = "엑셀 파일을 같이 업로드 해 주세요";
//			model.addAttribute("errorMessage", errorMessage);
//			return "expense/expenseError";
//		}
//				
//		System.out.println("=======================================실행====================");
//		XSSFWorkbook excelworkbook = new XSSFWorkbook(is);
//		XSSFSheet worksheet = excelworkbook.getSheetAt(0);
//		edService.uploadExpenseData(worksheet, photoReceipt);
//		System.out.println("======================================3");		
//		return "redirect:/expense/list";
//  }
	
	
	@GetMapping("/photo/{fileId}")
	public ResponseEntity<Resource> photo(@PathVariable("fileId") Integer id) throws MalformedURLException {
		Files file = this.fService.findByFiles(id);
		return this.fService.photoView(file);
	}
}