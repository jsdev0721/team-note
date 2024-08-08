package com.groupware.note;

import java.security.Principal;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.groupware.note.files.FileService;
import com.groupware.note.files.Files;
import com.groupware.note.user.UserDetails;
import com.groupware.note.user.UserDetailsService;
import com.groupware.note.user.UserService;
import com.groupware.note.user.Users;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class NoteController {
	
	private final UserService userService;
	private final UserDetailsService userDetailsService;
	private final FileService fileService;
	
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/")
	public String defalut(Model model, Principal principal) {
		Users users = this.userService.getUser(principal.getName());
		UserDetails userDetails = this.userDetailsService.findByUser(users);
		Files file = this.fileService.findByFiles(userDetails.getPhoto().getFileId());
		model.addAttribute("file", file);
		return "index";
	}

	
}
