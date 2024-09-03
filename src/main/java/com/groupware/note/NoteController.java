package com.groupware.note;

import java.security.Principal;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.groupware.note.approval.ApprovalController;
import com.groupware.note.approval.ApprovalService;
import com.groupware.note.department.Departments;
import com.groupware.note.form.FormService;
import com.groupware.note.leave.LeaveForm;
import com.groupware.note.notice.NoticesService;
import com.groupware.note.user.UserService;
import com.groupware.note.user.Users;
import com.groupware.note.welfaremall.WelfareMallService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class NoteController {
	private final FormService formService;
	private final NoticesService noticesService;
	private final ApprovalService approvalService;
	private final UserService userService;
	private final WelfareMallService welfareMallService;
	
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/")
	public String defalut(Model model , Principal principal , @RequestParam(value = "status" , defaultValue = "queue")String status , @RequestParam(value = "page" , defaultValue = "0")int page) {
		model.addAttribute("noticeList", this.noticesService.noticesList(page,4));
		model.addAttribute("formList", this.formService.formsList(page,4));
		Users user = this.userService.getUser(principal.getName());
		Departments department = user.getPosition().getDepartment();
<<<<<<< Updated upstream
		model.addAttribute("approvalList", this.approvalService.ApprovalList(department, status, page , 10));
		return "index";
=======
		model.addAttribute("approvalList", this.approvalService.ApprovalList(user, department, status, page , 10));
		model.addAttribute("status", status);
		model.addAttribute("welfaremallList", this.welfareMallService.findAll(page, 5));
		if(!user.getStatus().equals("출근")) {
			return "attendance/attendanceButton";
		}else {			
			return "index";
		}
>>>>>>> Stashed changes
	}

	
}
