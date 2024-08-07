package com.groupware.note.approval;

import java.security.Principal;

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

import com.groupware.note.department.DepartmentService;
import com.groupware.note.department.Departments;
import com.groupware.note.files.FileService;
import com.groupware.note.files.Files;
import com.groupware.note.user.UserService;
import com.groupware.note.user.Users;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/approval")
public class ApprovalController {
	private final ApprovalService approvalService;
	private final UserService userService;
	private final FileService fileService;
	private final DepartmentService departmentService;
	
//	@PreAuthorize("isAuthenticated()")
//	@GetMapping("/list")
//	public String approvalList() {
//		return "redirect:/approval/list/queue";
//	}
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/list")
	public String approvalList(Model model , @RequestParam(value = "status" , defaultValue = "queue") String status , @RequestParam(value = "page" , defaultValue = "0")int page , Principal principal) {
		System.out.println("-------------------status: "+status);
		Users user = this.userService.getUser(principal.getName());
		Departments department = user.getPosition().getDepartment();
		model.addAttribute("approvalList", this.approvalService.ApprovalList(page ,department , status));
		return "approvalList";
	}
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/create")
	public String approvalCreate(ApprovalForm approvalForm) {
		return "approvalCreate";
	}
	@PreAuthorize("isAuthenticated()")
	@PostMapping("/create")
	public String approvalCreate(@Valid ApprovalForm approvalForm , BindingResult bindingResult , Principal principal) {
		if(bindingResult.hasErrors()) {
			return "approvalCreate";
		}
		try {
			Approval _approval = new Approval();
			Users user = this.userService.getUser(principal.getName());
			_approval.setUser(user);
			if(approvalForm.getDepartmentName().equals("General")) {
				_approval.setDepartment(user.getPosition().getDepartment());
			}
			else {
				Departments department = this.departmentService.findBydepartmentName(approvalForm.getDepartmentName());
				_approval.setDepartment(department);
			}
			_approval.setTitle(approvalForm.getTitle());
			_approval.setContent(approvalForm.getContent());
			_approval.setFileList(this.fileService.uploadFile(approvalForm.getMultipartFiles()));
			Approval approval = this.approvalService.save(_approval);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "redirect:/approval/list";
	}
	
	@GetMapping("/detail/{id}")
	public String approvalDetail(Model model , @PathVariable("id")Integer id) {
		Approval approval = this.approvalService.findById(id);
		model.addAttribute("approval", approval);
		model.addAttribute("fileList", approval.getFileList());
		return "approvalDetail";
	}
	
	@GetMapping("/download/{id}")
	public ResponseEntity<Resource> download(@PathVariable("id")Integer id) {
		Files file = this.fileService.findByFiles(id);
		return this.fileService.downloadFile(file);
	}
	
	@GetMapping("/update/{id}")
	public String changeStatus(@PathVariable("id")Integer id , @RequestParam(value = "status")String status) {
		Approval approval = this.approvalService.findById(id);
		approval.setStatus(status);
		this.approvalService.save(approval);
		return String.format("redirect:/approval/detail/%s", id);
		
	}
}
