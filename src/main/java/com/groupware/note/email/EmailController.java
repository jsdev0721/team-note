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

	private String email; //이메일 주소 저장 (인증 실패 후 재시도할 때 사용)
	
	@PostMapping("/mailSend")
    public String mailSend(@RequestParam(value = "email") String email, Model model) {
        Boolean status = false;
        System.out.println("controller=============");
        try {
        	this.email = email;
            int number = emailService.sendMail(email);
            status = true;
            model.addAttribute("status", status);
            model.addAttribute("mailCode", number);
        } catch (Exception e) {
        	e.getMessage();
        }
        return "findID";
    }
	
	@GetMapping("/mailSend")
	public String againMailSend(Model model) { //이메일 인증 실패 후 재시도
		Boolean status = false;
        System.out.println("controller=============");
        try {
            int number = emailService.sendMail(email);
            status = true;
            model.addAttribute("status", status);
            model.addAttribute("mailCode", number);
        } catch (Exception e) {
        	e.getMessage();
        }
		return "findID";
	}
	
	@ResponseBody
	@GetMapping("/findEmail")
	public String findEmail() {
		UserDetails userDetails = this.userDetailsService.findID(email);
		String username = userDetails.getUser().getUsername();
		return username;
	}

}
