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
import org.springframework.web.multipart.MultipartFile;

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
		
		return "notice/notices_list";
	}
	@PostMapping("/list")
	public String noticeSearchList(Model model
			,@RequestParam(value ="page",defaultValue="0") int page,@Valid SearchListForm searchListForm) {
		Page<Notices> noticesList = this.noticesService.noticesSearchList(page, searchListForm.getSearchkeyword());
		model.addAttribute("paging",noticesList);
		
		return "notice/notices_list";
	}
	@GetMapping("/detail/{noticeId}")
	public String detail(Model model,@PathVariable("noticeId") Integer noticeId) {
		Notices notices = this.noticesService.getNotice(noticeId);
		model.addAttribute("fileList",notices.getFileList());
		model.addAttribute("notices" ,notices);
		
		return "notice/notices_detail";
	}
	@GetMapping("/download/{noticeId}")
	public ResponseEntity<Resource> download(@PathVariable("noticeId") Integer noticeId){
		Files files= this.fileService.findByFiles(noticeId);
		return this.fileService.downloadFile(files);
	}
	
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/create")
	public String create(NoticeForm noticeForm) {
		
		return "notice/notices_form";
		
	}
	@PreAuthorize("isAuthenticated()")
	@PostMapping("/create")
	public String create(@Valid NoticeForm noticeForm,BindingResult bindingResult,Principal principal) {
		 
		if(bindingResult.hasErrors()) {
			return "notice/notices_form";
		}
		
		List<Files> fileList = new ArrayList<>();
		if(noticeForm.getMultiPartFile()!=null&&!noticeForm.getMultiPartFile().isEmpty()) {
			for(MultipartFile multipartFile : noticeForm.getMultiPartFile()) {
				Files file = new Files();
				file = this.fileService.uploadFile(multipartFile);
				fileList.add(file);
			}
		}
		Users users = this.userService.getUser(principal.getName());
		this.noticesService.create(noticeForm.getTitle(), noticeForm.getContent(), users,fileList);
		
		return "redirect:/notices/list";
	}
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/update/{noticeId}") 
	public String update(Model model,@PathVariable("noticeId") Integer noticeId,NoticeForm noticeForm,Principal principal) {
		
		Notices notices = this.noticesService.getNotice(noticeId);
		if(notices.getUser().getUsername().equals(principal.getName())) {
			model.addAttribute("notices",notices);
			
			model.addAttribute("fileList",notices.getFileList());
			}else {
				return "redirect:/notices/list";
				}
		return "notice/notices_update";
	}
	
	
	@PreAuthorize("isAuthenticated()")
	@PostMapping("/update/{noticeId}")	
	public String update(@PathVariable("noticeId") Integer noticeId
			,@Valid NoticeForm noticeForm,BindingResult bindingResult,Principal principal) {
		if(bindingResult.hasErrors()) {
			
			return "notice/notices_update";
		}
		Notices notices = this.noticesService.getNotice(noticeId);
		
		List<Files> fileList = notices.getFileList();
		System.out.println("파일 가져오기++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
 		if(noticeForm.getMultiPartFile()!=null&&!noticeForm.getMultiPartFile().isEmpty()) {
 			System.out.println("실행==============================================================");
			for(MultipartFile multipartFile : noticeForm.getMultiPartFile()) {
				Files file = new Files();
				System.out.println("새파일 업로드하기++++++++++++++++++++++++++++++++++++++++++++++++");
				file = this.fileService.uploadFile(multipartFile);
				System.out.println(file+"+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
				fileList.add(file);
				System.out.println("파일 추가됨"+file+"db에 등록+++++++++++++++++++++++++++++++++++++++++++++++++");
				
			}	
		}
 		Users users = this.userService.getUser(principal.getName());
 		this.noticesService.updateNotice(notices , noticeForm.getTitle(), noticeForm.getContent(),users,fileList);
		
		return "redirect:/notices/list";
	}
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/delete/{noticeId}")
	public String delete(@PathVariable("noticeId") Integer noticeId,Principal priclpal) {
		Notices notices =this.noticesService.getNotice(noticeId);
		if(notices.getUser().getUsername().equals(priclpal.getName())) {
		List<Files> files = notices.getFileList();
		if(!files.isEmpty()) {
			for(Files file : files) {
		this.noticesService.deleteNotices(notices);
		System.out.println("게시물 지워우어ㅜㅇ워워우어ㅜ어우");
		this.fileService.delete(file);
		System.out.println("파일 지워어워워워워워워워워워");
			}
		}
	}else {return "redirect:/notices/list";}
		return "redirect:/notices/list";
	}
	@GetMapping("/delete/{noticeId}/{fileId}")
	public String  fileDelete(@PathVariable("noticeId")Integer noticeId, @PathVariable("fileId")Integer fileId) {
		Notices notices = this.noticesService.getNotice(noticeId);
		List<Files> fileList = notices.getFileList();
		Files file = this.fileService.findByFiles(fileId);
		fileList.remove(file);
		this.fileService.delete(file);
		return String.format("redirect:/notices/update/%s",noticeId);
		
	}
	
	
}
