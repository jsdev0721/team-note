package com.groupware.note.user;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequestMapping("/user")
@Controller
@RequiredArgsConstructor
public class UserController {
	
	private final UserService userService;
	private final UserDetailsService userDetailsService;
	
	@GetMapping("/login")
	public String login() {
		return "login";
	}
	
	@GetMapping("/regist")
	public String regist(UserCreateForm userCreateForm) {
		return "regist";
	}
	
	@PostMapping("/regist")
	public String regist(@Valid UserCreateForm userCreateForm, BindingResult bindingResult) {
		if(bindingResult.hasErrors()) {
			return "regist";
		}
		if(!userCreateForm.getPassword().equals(userCreateForm.getPasswordCheck())) { //비밀번호와 비밀번호 확인이 일치하지 않으면
			bindingResult.rejectValue("passwordCheck", "passwordInCorrect", "2개의 비밀번호가 일치하지 않습니다.");
			//bindingResult.rejectValue(잘못입력 된 값(필드명), 에러코드(내가 지정함), 에러 메세지) => bindingResult에서 에러 메세지를 하나 더 추가하여 넘겨주어야 할 때
			return "regist";
		}
		try { //중복검사
			Users users = this.userService.create(userCreateForm.getUsername(), userCreateForm.getPassword());
			this.userDetailsService.create(users, userCreateForm.getName(), userCreateForm.getBirthdate(), userCreateForm.getEmail());
		} catch (DataIntegrityViolationException e) { //SiteUser에서 주었던 unique 제약조건 위반시 해당 에러클래스가 처리함 
			e.printStackTrace();
			bindingResult.reject("signupFailed", "이미 등록된 사용자입니다.");
			return "regist";
		} catch (Exception e) {
			e.printStackTrace();
			bindingResult.reject("signupFailed", e.getMessage());
			//bindingResult.reject(에러코드, 에러메세지)
			return "regist";
		}
		
		return "index";
	}
	
	@GetMapping("/find/id")
	public String findID() {
		return "findID";
	}
	
	@PostMapping("/find/id")
	public String findID(Model model, @RequestParam("email") String email) {
		System.out.println("email"+email);
		UserDetails userDetails = this.userDetailsService.findID(email);
		Users users = userDetails.getUser();
		model.addAttribute("users", users);
		return "findID";
	}
	
	@GetMapping("/find/pw")
	public String findPW() {
		return "findPW";
	}
	
	@PostMapping("/find/pw")
	public String findPW(Model model, @RequestParam(value = "username") String username) {
		Users users = this.userService.getUser(username);
		model.addAttribute("users", users);
		return "findPW";
	}

}