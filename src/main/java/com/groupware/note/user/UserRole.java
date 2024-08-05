package com.groupware.note.user;

import lombok.Getter;

@Getter
public enum UserRole {
	
	ADMIN("ROLE_ADMIN"), //관리자
	USER("ROLE_USER"); //사용자
	
	UserRole(String value){
		this.value = value;
	}
	
	private String value;

}
