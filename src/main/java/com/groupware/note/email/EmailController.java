package com.groupware.note.email;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.groupware.note.DataNotFoundException;
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
    	int domain = email.indexOf("@");
		String domainCheck = email.substring(domain+1);
		if(domainCheck.equals("naver.com") || domainCheck.equals("daum.net") || domainCheck.equals("gmail.com")
				|| domainCheck.equals("icloud.com") || domainCheck.equals("kakao.com") || domainCheck.equals("nate.com")) {    			
			UserDetails userDetails = this.userDetailsService.findID(email);
			int number = emailService.sendMail(userDetails.getEmail());
			String code = "" + number;
			return code;
		}else {
			throw new DataNotFoundException("데이터를 찾을 수 없습니다");
		}
    }
	
	@ResponseBody
	@GetMapping("/findEmail")
	public String findEmail(@RequestParam(value = "email") String email) {
		UserDetails userDetails = this.userDetailsService.findID(email);
		String username = userDetails.getUser().getUsername();
		return username;
	}

}
