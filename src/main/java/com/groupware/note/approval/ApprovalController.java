package com.groupware.note.approval;

import java.security.Principal;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.groupware.note.department.Departments;
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
	private final ApprovalFileService approvalFileService;
	
	@GetMapping("/list")
	public String approvalList() {
		return "redirect:/approval/list/queue";
	}
	
	@GetMapping("/list/{status}")
	public String approvalList(Model model , @PathVariable(value = "status") String status , @RequestParam(value = "page" , defaultValue = "0")int page , Principal principal) {
		Users user = this.userService.getUser(principal.getName());
		Departments department = user.getPosition().getDepartment();
		model.addAttribute("approvalList", this.approvalService.ApprovalList(page ,department , status));
		return "approvalList";
	}
	
	@GetMapping("/create")
	public String approvalCreate(ApprovalForm approvalForm) {
		return "approvalCreate";
	}
	@PostMapping("/create")
	public String approvalCreate(@Valid ApprovalForm approvalForm , BindingResult bindingResult) {
		if(bindingResult.hasErrors()) {
			return "approvalCreate";
		}
		try {
			Approval approval = new Approval();
			approval.setTitle(approvalForm.getTitle());
			approval.setContent(approvalForm.getContent());
			Approval savedApproval = this.approvalService.save(approval);
			this.approvalFileService.uploadFile(approvalForm.getMultipartFiles() , savedApproval);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "redirect:/";
	}
	
	@GetMapping("/detail/{id}")
	public String approvalDetail(Model model , @PathVariable("id")Integer id) {
		Approval approval = this.approvalService.findById(id);
		model.addAttribute("approval", approval);
		model.addAttribute("fileList", this.approvalFileService.findByApproval(approval));
		return "approvalDetail";
	}
	
	@GetMapping("/download/{id}")
	public ResponseEntity<Resource> download(@PathVariable("id")Integer id) {
		ApprovalFile approvalFile = this.approvalFileService.findById(id);
		return this.approvalFileService.downloadFile(approvalFile);
		
	}
	
	@PostMapping("/detail/{id}")
	public String changeStatus(@PathVariable(value = "id")Integer id , @RequestParam(value = "status")String status) {
		Approval approval = this.approvalService.findById(id);
		approval.setStatus(status);
		this.approvalService.save(approval);
		return "redirect:/approval/detail/{id}";
	}
}
