package com.groupware.note.approval;

import java.security.Principal;
import java.time.Period;
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

import com.groupware.note.department.DepartmentService;
import com.groupware.note.department.Departments;
import com.groupware.note.files.FileService;
import com.groupware.note.files.Files;
import com.groupware.note.leave.LeaveForm;
import com.groupware.note.leave.LeaveService;
import com.groupware.note.user.UserDetails;
import com.groupware.note.user.UserDetailsService;
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
	private final UserDetailsService userDetailsService;
	private final LeaveService leaveService;
	private final CommentService commentService;
	
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/list")
	public String approvalList(Model model , @RequestParam(value = "status" , defaultValue = "queue") String status , @RequestParam(value = "page" , defaultValue = "0")int page , Principal principal) {
		Users user = this.userService.getUser(principal.getName());
		Departments department = user.getPosition().getDepartment();
		Page<Approval> approvalList = this.approvalService.ApprovalList(department, status , page , 10);
		model.addAttribute("approvalList", approvalList);
		return "approvalList";
	}
	@PostMapping("/list")
	public String serch(Model model , @RequestParam(value = "status") String status , @RequestParam(value = "page" , defaultValue = "0")int page , Principal principal , @RequestParam(value = "search")String search) {
		Users user = this.userService.getUser(principal.getName());
		Departments department = user.getPosition().getDepartment();
		Page<Approval> approvalList =  this.approvalService.findByLike(search , department , status , page , 10);
		model.addAttribute("approvalList" , approvalList);
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
			if(!approvalForm.getDepartmentName().equals("General")) {
				Departments department = this.departmentService.findBydepartmentName(approvalForm.getDepartmentName());
				_approval.setDepartment(department);
			}
			else {
				Departments department = this.userService.getUser(principal.getName()).getPosition().getDepartment();
				_approval.setDepartment(department);
			}
			_approval.setTitle(approvalForm.getTitle());
			_approval.setContent(approvalForm.getContent());
			_approval.setUserSign(new String[3]);
			if(!approvalForm.getMultipartFiles().isEmpty()) {
				_approval.setFileList(this.fileService.uploadFile(approvalForm.getMultipartFiles()));
			}
			Approval approval = this.approvalService.save(_approval);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "redirect:/approval/list";
	}
	
	@GetMapping("/create/HR")
	public String approvalCreateLeave(LeaveForm leaveForm) { //휴가폼
		return "approvalCreate_leave";
	}
	@PreAuthorize("isAuthenticated()")
	@PostMapping("/create/HR")
	public String approvalCreateLeave(@Valid LeaveForm leaveForm, BindingResult bindingResult, Principal principal) {
		if(bindingResult.hasErrors()) {
			return "approvalCreate_leave";
		}
		try {
			Approval _approval = new Approval();
			Users users = this.userService.getUser(principal.getName());
			_approval.setUser(users);
			Departments department = this.departmentService.findBydepartmentName(leaveForm.getDepartmentName());
			_approval.setDepartment(department);
			_approval.setTitle(leaveForm.getTitle());
			_approval.setContent(leaveForm.getReason());
			_approval.setStartDate(leaveForm.getStartDate());
			_approval.setEndDate(leaveForm.getEndDate());
			_approval.setUserSign(new String[3]);
			if(!leaveForm.getAttachment().isEmpty()) {
				_approval.setFileList(this.fileService.uploadFile(leaveForm.getAttachment()));
			}
			this.approvalService.save(_approval);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "redirect:/approval/list";
	}
	
	@GetMapping("/detail/{id}")
	public String approvalDetail(Model model , @PathVariable("id")Integer id , Principal principal , Comments comments) {
		Approval approval = this.approvalService.findById(id);
		model.addAttribute("approval", approval);
		model.addAttribute("fileList", approval.getFileList());
		model.addAttribute("commentList", approval.getCommentList());
		model.addAttribute("userInfo", this.userService.getUser(principal.getName()));
		return "approvalDetail";
	}
	
	@GetMapping("/download/{id}")
	public ResponseEntity<Resource> download(@PathVariable("id")Integer id) {
		Files file = this.fileService.findByFiles(id);
		return this.fileService.downloadFile(file);
	}
	
	@GetMapping("/update/{id}")
	public String changeStatus(@PathVariable("id")Integer id , @RequestParam(value = "status")String status , Principal principal) {
		Approval approval = this.approvalService.findById(id);
		String[] signArray = new String[3];
		approval.setStatus(status);
		if(status.equals("queue")) {
			approval.setUserSign(signArray);
			return "redirect:/approval/list";
		}
		Users user = this.userService.getUser(principal.getName());
		UserDetails userDetail = this.userDetailsService.findByUser(user);
		String[] userSign = approval.getUserSign();
		for(int i=0 ; i<userSign.length ; i++) {
			if(userSign[i]!=null) {
				signArray[i]=userSign[i];
			}
			if(status.equals("complete")) {
				signArray[i]=userDetail.getName();
			}
			else {
				signArray[i]=userDetail.getName();
				break;
			}
		}
		approval.setUserSign(signArray);
		
		if(approval.getDepartment().equals("HR")&&status.equals("complete")) {
			UserDetails userDetails = this.userDetailsService.findByUser(approval.getUser());
			Users users = userDetails.getUser();
			Period leaveDate = Period.between(approval.getStartDate(), approval.getEndDate());
			this.userDetailsService.minusLeave(userDetails, leaveDate.getDays());
			this.leaveService.create(users, approval.getTitle(), approval.getContent(), approval.getStartDate(), approval.getEndDate(), status, approval.getFileList());
		}
		this.approvalService.save(approval);
		return "redirect:/approval/list";
	}

	@GetMapping("/edit/{id}")
	public String edit(ApprovalForm approvalForm , @PathVariable("id")Integer id , Model model) {
		Approval approval = this.approvalService.findById(id);
		model.addAttribute("approval", approval);
		model.addAttribute("fileList", approval.getFileList());
		return "approvalEdit";
	}
	@PostMapping("/edit/{id}")
	public String edit(Model model , @PathVariable("id")Integer id , @Valid ApprovalForm approvalForm , BindingResult bindingResult , Principal principal) {
		if(bindingResult.hasErrors()) {
			model.addAttribute("approval", this.approvalService.findById(id));
			return "approvalEdit";
		}
		Approval approval = this.approvalService.findById(id);
		approval.setTitle(approvalForm.getTitle());
		approval.setContent(approvalForm.getContent());
			List<Files> fileList = this.fileService.uploadFile(approvalForm.getMultipartFiles());
			if(fileList!=null) {
				for(Files file : fileList) {
					approval.getFileList().add(file);
				}
			}
		if(!approvalForm.getDepartmentName().equals("General")) {
			Departments department = this.departmentService.findBydepartmentName(approvalForm.getDepartmentName());
			approval.setDepartment(department);
		}
		else {
			Departments department = this.userService.getUser(principal.getName()).getPosition().getDepartment();
			approval.setDepartment(department);
		}
		this.approvalService.save(approval);
		return String.format("redirect:/approval/detail/%s", id);
	}
	
	@GetMapping("/delete/{id}")
	public String delete(@PathVariable("id")Integer id) {
		Approval approval = this.approvalService.findById(id);
		List<Files> fileList = approval.getFileList();
		this.approvalService.deleteById(approval);
		if(!fileList.isEmpty()) {
			for(Files file : fileList) {
				this.fileService.delete(file);
			}
		}
		return "redirect:/approval/list";
	}
	
	@GetMapping("/deleteFile/{approvalId}/{fileId}")
	public String fileDelete(@PathVariable("approvalId")Integer id , @PathVariable("fileId")Integer fileId) {
		Approval approval = this.approvalService.findById(id);
		List<Files> fileList = approval.getFileList();
		Files file = this.fileService.findByFiles(fileId);
		fileList.remove(file);
		this.fileService.delete(file);
		return String.format("redirect:/approval/edit/%s", id);
	}
	@PostMapping("/revoke/{id}")
	public String revoke(@PathVariable("id") Integer id , Comments comment , @RequestParam(value = "status")String status) {
		Approval _approval = this.approvalService.findById(id);
		_approval.setStatus(status);
		Approval approval = this.approvalService.save(_approval);
		comment.setApproval(approval);
		this.commentService.save(comment);
		return "redirect:/approval/list";
	}
}
