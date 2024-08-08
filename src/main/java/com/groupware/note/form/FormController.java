package com.groupware.note.form;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.groupware.note.files.FileService;
import com.groupware.note.files.Files;
import com.groupware.note.notice.SearchListForm;
import com.groupware.note.user.UserService;
import com.groupware.note.user.Users;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/forms")
public class FormController {
	
	private final FormService formService;
	private final UserService userService;
	private final FileService fileService;
	
	@GetMapping("/list")
	public String formsList(Model model
			,@RequestParam(value = "page",defaultValue="0") int page, SearchListForm searchListForm) {
		Page<Forms> formsList = this.formService.formsList(page);
		model.addAttribute("paging",formsList);
		
		return "forms_list";
	}
	@PostMapping("/list")
	public String formsSerchList(Model model
							,@RequestParam(value="page", defaultValue="0")int page,@Valid SearchListForm searchListForm){
		Page<Forms> formsList = this.formService.SearchList(page, searchListForm.getSearchkeyword());
		model.addAttribute("paging", formsList);
		
		return "forms_list";
	}
	@GetMapping("/detail/{formsId}")
	public String detail(Model model,@PathVariable("formsId") Integer formsId) {
		Forms forms = this.formService.getForm(formsId);
		model.addAttribute("forms",forms);
		
		return "forms_detail";
	}
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/create")
	public String create(FormsForm formsForm) {
		
		return "forms_form";
	}
	@PreAuthorize("isAuthenticated()")
	@PostMapping("/create")
	public String create(@Valid FormsForm formsForm,BindingResult bindingResult,Principal principal) {
		if(bindingResult.hasErrors()) {
			
		return "forms_form";
		}
		List<Files> files = new ArrayList<>();
		files = this.fileService.uploadFile(formsForm.getMultiPartFile());
		Users users = this.userService.getUser(principal.getName());
		this.formService.create(formsForm.getTitle(), formsForm.getContent(),users,files);
		
		return "redirect:/forms/list";
		
	}
	

}
