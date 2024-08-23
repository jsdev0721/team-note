package com.groupware.note.email;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.groupware.note.user.UserDetails;
import com.groupware.note.user.UserDetailsService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/email")
public class EmailController {
	
	private final EmailService emailService;
	private final UserDetailsService userDetailsService;
	
	@ResponseBody
	@PostMapping("/mailSend")
    public String mailSend(@RequestParam(value = "email") String email) {
        int number = emailService.sendMail(email);
        String code = "" + number;
        return code;
    }
	
	@ResponseBody
	@GetMapping("/findEmail")
	public String findEmail(@RequestParam(value = "email") String email) {
		UserDetails userDetails = this.userDetailsService.findID(email);
		String username = userDetails.getUser().getUsername();
		return username;
	}

}
