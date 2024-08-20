package com.groupware.note.form;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
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

import com.groupware.note.files.FileService;
import com.groupware.note.files.Files;
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
		Page<Forms> formsList = this.formService.formsList(page , 10);
		model.addAttribute("paging",formsList);
		
		return "forms_list";
	}
	@PostMapping("/list")
	public String formsSerchList(Model model
							,@RequestParam(value="page", defaultValue="0")int page, @Valid SearchListForm searchListForm){
		Page<Forms> formsList = this.formService.searchList(page, searchListForm.getSearchKeyword());
		model.addAttribute("paging", formsList);
		
		return "forms_list";
	}
	@GetMapping("/detail/{formId}")
	public String detail(Model model,@PathVariable("formId") Integer formId) {
		Forms forms = this.formService.getForm(formId);
		model.addAttribute("fileList",forms.getFileList());
		model.addAttribute("forms",forms);
		
		return "forms_detail";
	}
	@GetMapping("/download/{formId}")
	public ResponseEntity<Resource> download(@PathVariable("formId") Integer formsId){
		Files files = this.fileService.findByFiles(formsId);
		return this.fileService.downloadFile(files);
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
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/update/{formId}")
	public String update(Model model,@PathVariable("formId") Integer formId
			,FormsForm formsForm,Principal principal) {
		Forms forms = this.formService.getForm(formId);
		model.addAttribute("forms",forms);
		
		return "forms_update";
	}
	@PreAuthorize("isAuthenticated()")
	@PostMapping("/update/{formId}")
	public String update(@PathVariable("formId") Integer formId
			,@Valid FormsForm formsForm,BindingResult bindingResult,Principal principal) {
		if(bindingResult.hasErrors()) {
			return "forms_update";
		}
		Forms forms = this.formService.getForm(formId);
		List<Files> files = this.fileService.uploadFile(formsForm.getMultiPartFile());
		Users users = this.userService.getUser(principal.getName());
		this.formService.update(forms, formsForm.getTitle(), formsForm.getContent(), users, files);
		
		return "redirect:/forms/list";
	}
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/delete/{formId}")
	public String delete(@PathVariable("formId") Integer formId) {
		Forms forms = this.formService.getForm(formId);
		this.formService.delete(forms);
		
		return "redirect:/forms/list";
	}
	

}