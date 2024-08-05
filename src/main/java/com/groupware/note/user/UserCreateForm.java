package com.groupware.note.user;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserCreateForm {
	
	@NotEmpty(message = "아이디를 입력해 주세요")
	private String username;
	
	@NotEmpty(message = "비밀번호를 입력해 주세요")
	private String password;
	
	@NotEmpty(message = "비밀번호 확인을 위해 한 번 더 입력해 주세요")
	private String passwordCheck;
	
	@NotEmpty(message = "이름을 입력해 주세요")
	private String name;
	
	@NotNull(message = "생년월일을 입력해 주세요")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate birthdate;
	
	@NotEmpty(message = "이메일을 입력해 주세요")
	private String email;

}
