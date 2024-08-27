package com.groupware.note;

import java.security.Principal;
import java.util.List;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.groupware.note.approval.Approval;
import com.groupware.note.approval.ApprovalService;
import com.groupware.note.files.FileService;
import com.groupware.note.files.Files;
import com.groupware.note.user.UserDetails;
import com.groupware.note.user.UserDetailsService;
import com.groupware.note.user.UserService;
import com.groupware.note.user.Users;

import lombok.RequiredArgsConstructor;

@ControllerAdvice
@RequiredArgsConstructor
public class NoteModelAttribute {
	
	private final UserService userService;
	private final UserDetailsService userDetailsService;
	private final FileService fileService;
	private final ApprovalService approvalService;
	
	@ModelAttribute
	public void photoAttributes(Model model, Principal principal) {
		if(principal != null) {
			Users users = this.userService.getUser(principal.getName());
			UserDetails userDetails = this.userDetailsService.findByUser(users);
			if(this.fileService.fileExists(userDetails)) {
				Files file = this.fileService.findByFiles(userDetails.getPhoto().getFileId());
				model.addAttribute("file", file);			// 컨트롤러의 메소드마다 해당 모델을 넣어준다. 
			}
			else {
				model.addAttribute("file", null);
			}
			model.addAttribute("queue", this.approvalService.findByUser(users, "queue"));
			model.addAttribute("process", this.approvalService.findByUser(users, "process"));
			model.addAttribute("complete", this.approvalService.findByUser(users, "complete"));
		}
	}
	
}
