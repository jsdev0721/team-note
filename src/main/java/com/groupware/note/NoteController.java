package com.groupware.note;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.groupware.note.leave.LeaveForm;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class NoteController {

	
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/")
	public String defalut(Model model, LeaveForm leaveForm) {
		return "index";
	}

	
}
