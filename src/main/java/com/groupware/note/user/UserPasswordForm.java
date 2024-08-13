package com.groupware.note.user;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserPasswordForm {
	
	@NotEmpty
	private String username;
	
	@NotEmpty(message = "새로운 비밀번호를 입력해 주세요.")
	private String password;
	
	@NotEmpty(message = "비밀번호 확인을 위해 입력해 주세요.")
	private String passwordCheck;

}
