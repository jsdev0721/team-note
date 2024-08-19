package com.groupware.note.user;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/email")
public class EmailController {
	
	private final EmailService emailService;
	private int number; //이메일 인증 숫자 저장
	
	@PostMapping("/mailSend")
    public String mailSend(@RequestParam(value = "email") String email, Model model) {
        Boolean status = false;
        System.out.println("controller=============");
        try {
            number = emailService.sendMail(email);
            System.out.println("email: " + email);
            System.out.println("number: " + number);
            status = true;
            System.out.println("전송 성공");
            model.addAttribute("status", status);
        } catch (Exception e) {
        	e.getMessage();
            System.out.println("전송 실패");
        }
        return "findID";
    }
	
	@GetMapping("/mailCheck")
    public ResponseEntity<?> mailCheck(@RequestParam String userNumber) {
        boolean isMatch = userNumber.equals(String.valueOf(number));
        System.out.println("인증 성공 여부: " + isMatch);
        return ResponseEntity.ok(isMatch);
    }

}
