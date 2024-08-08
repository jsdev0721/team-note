package com.groupware.note;

import java.security.Principal;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

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
	
	@ModelAttribute
	public void photoAttributes(Model model, Principal principal) {
		if(principal != null) {
			Users users = this.userService.getUser(principal.getName());
			UserDetails userDetails = this.userDetailsService.findByUser(users);
			Files file = this.fileService.findByFiles(userDetails.getPhoto().getFileId());
			model.addAttribute("file", file);			
		}
	}
	
}
