package com.groupware.note.user;

import lombok.Getter;

@Getter
public enum UserRole {
	
	ADMIN("ROLE_ADMIN"), //관리자
	HR("ROLE_HR"), //인사
	ACCOUNTING("ROLE_ACCOUNTING"), //회계
	MARKETING("ROLE_MARKETING"), //영업
	USER("ROLE_USER"), //사용자(임시)
	SECTIONCHEIF("ROLE_SECTIONCHEIF"),
	DEPUTY("ROLE_DEPUTY"),
	WORKER("ROLE_WORKER"),
	INTERN("ROLE_INTERN");
	UserRole(String value){
		this.value = value;
	}
	
	private String value;

}
