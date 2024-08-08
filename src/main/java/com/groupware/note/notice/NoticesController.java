package com.groupware.note.notice;

import java.security.Principal;

import java.util.ArrayList;
import java.util.List;

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

import org.springframework.core.io.Resource;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
@RequestMapping("/notices")
public class NoticesController {
	
	private final NoticesService noticesService;
	private final UserService userService;
	private final FileService fileService;
	
	
	@GetMapping("/list")
	public String noticeList(Model model
			,@RequestParam(value ="page",defaultValue="0") int page, SearchListForm searchListForm) {
		Page<Notices> noticesList = this.noticesService.noticesList(page);
		model.addAttribute("paging",noticesList);
		
		return "notices_list";
	}
	@PostMapping("/list")
	public String noticeSearchList(Model model
			,@RequestParam(value ="page",defaultValue="0") int page,@Valid SearchListForm searchListForm) {
		Page<Notices> noticesList = this.noticesService.noticesSearchList(page, searchListForm.getSearchkeyword());
		model.addAttribute("paging",noticesList);
		
		return "notices_list";
	}
	@GetMapping("/detail/{noticeId}")
	public String detail(Model model,@PathVariable("noticeId") Integer noticeId) {
		Notices notices = this.noticesService.getNotice(noticeId);
		model.addAttribute("fileList",notices.getFileList());
		model.addAttribute("notices" ,notices);
		
		return "notices_detail";
	}
	@GetMapping("/download/{noticeId}")
	public ResponseEntity<Resource> download(@PathVariable("noticedId") Integer noticeId){
		Files files= this.fileService.findByFiles(noticeId);
		return this.fileService.downloadFile(files);
	}
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/create")
	public String create(NoticeForm noticeForm) {
		
		return "notices_form";
		
	}
	@PreAuthorize("isAuthenticated()")
	@PostMapping("/create")
	public String create(@Valid NoticeForm noticeForm,BindingResult bindingResult,Principal principal) {
		 
		if(bindingResult.hasErrors()) {
			return "notices_form";
		}
		
		List<Files> files = new ArrayList<>();
		files = this.fileService.uploadFile(noticeForm.getMultiPartFile());
		Users users = this.userService.getUser(principal.getName());
		this.noticesService.create(noticeForm.getTitle(), noticeForm.getContent(), users,files);
		
		return "redirect:/notices/list";
	}

}
