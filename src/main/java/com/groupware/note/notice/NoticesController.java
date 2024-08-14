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
		Page<Notices> noticesList = this.noticesService.noticesList(page,10);
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
	public ResponseEntity<Resource> download(@PathVariable("noticeId") Integer noticeId){
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
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/update/{noticeId}") 
	public String update(Model model,@PathVariable("noticeId") Integer noticeId,NoticeForm noticeForm,Principal principal) {
		
		Notices notices = this.noticesService.getNotice(noticeId);
		noticeForm.setTitle(notices.getTitle());
		noticeForm.setContent(notices.getContent());
		model.addAttribute("notices",notices);
		
		return "notices_update";
	}
	
	@PreAuthorize("isAuthenticated()")
	@PostMapping("/update/{noticeId}")	
	public String update(Notices notices,@PathVariable("noticeId") Integer noticeId
			,@Valid NoticeForm noticeForm,BindingResult bindingResult,Principal principal) {
		if(bindingResult.hasErrors()) {
			
			return "notices_update";
		}
		Notices notices2 = this.noticesService.getNotice(noticeId);
		List<Files> files = new ArrayList<>();
		files = this.fileService.uploadFile(noticeForm.getMultiPartFile());
		Users users = this.userService.getUser(principal.getName());
		this.noticesService.updateNotice(notices , noticeForm.getTitle(), noticeForm.getContent(),users,files);
		
		return "redirect:/notices/list";
	}
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/delete/{noticeId}")
	public String delete(@PathVariable("noticeId") Integer noticeId) {
		Notices notices =this.noticesService.getNotice(noticeId);
		List<Files> files = notices.getFileList();
		if(!files.isEmpty()) {
			for(Files file : files) {
		this.noticesService.deleteNotices(notices);
		System.out.println("게시물 지워우어ㅜㅇ워워우어ㅜ어우");
		this.fileService.delete(file);
		System.out.println("파일 지워어워워워워워워워워워");
			}
		}
		return "redirect:/notices/list";
	}
	
	
}
